package io.leego.unique.core.service.impl;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.util.ErrorCode;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;
import io.leego.unique.core.manager.SequenceManager;
import io.leego.unique.core.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Yihleego
 */
public class SequenceServiceImpl implements SequenceService {
    private static final Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);
    private final ConcurrentMap<String, CachedSeq> seqMap = new ConcurrentHashMap<>(32);
    private final SequenceManager sequenceManager;
    private volatile boolean initialized = false;

    public SequenceServiceImpl(SequenceManager sequenceManager) {
        this.sequenceManager = sequenceManager;
    }

    @Override
    public Result<Long> next(String key) {
        CachedSeq seq = seqMap.get(key);
        if (seq == null) {
            logger.error("There is no sequence named \"{}\".", key);
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence named \"" + key + '\"');
        }
        long value = seq.getCur().addAndGet(seq.getIncrement());
        check(key, seq);
        return Result.buildSuccess(value);
    }

    @Override
    public Result<Segment> next(String key, int size) {
        if (size <= 0) {
            logger.error("The size cannot be negative or zero.");
            return Result.buildFailure(ErrorCode.ILLEGAL_ARGUMENT, "The size cannot be negative or zero");
        }
        CachedSeq seq = seqMap.get(key);
        if (seq == null) {
            logger.error("There is no sequence named \"{}\".", key);
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence named \"" + key + '\"');
        }
        int increment = seq.getIncrement();
        long end = seq.getCur().addAndGet(size * increment);
        long begin = end - (size - 1) * increment;
        check(key, seq);
        return Result.buildSuccess(new Segment(begin, end, increment));
    }

    @Override
    public void init() {
        if (initialized) {
            logger.warn("The service has already been initialized.");
            return;
        }
        logger.info("Starting loading data from the database.");
        sequenceManager.findAll().forEach(this::merge);
        initialized = true;
        logger.info("Finished loading data from the database.");
    }

    @Override
    public void merge(final Sequence sequence) {
        if (sequence == null) {
            return;
        }
        CachedSeq cached = seqMap.computeIfAbsent(sequence.getKey(), key -> {
            logger.debug("The sequence \"{}\" has been loaded.", key);
            return new CachedSeq(key, sequence.getValue(), sequence.getIncrement(), sequence.getCache(), sequence.getVersion());
        });
        if (sequence.getVersion() != cached.getVersion()) {
            logger.debug("The sequence \"{}\" has been modified.", sequence.getKey());
            cached.update(sequence.getIncrement(), sequence.getCache(), sequence.getVersion());
        }
    }

    @Override
    public SnapshotSequence getSnapshot(String key) {
        if (key == null) {
            return null;
        }
        CachedSeq cached = seqMap.get(key);
        if (cached == null) {
            return null;
        }
        return new SnapshotSequence(
                cached.getKey(),
                cached.getCur().get(),
                cached.getMax().get(),
                cached.getIncrement(),
                cached.getCache(),
                cached.getVersion(),
                LocalDateTime.now());
    }

    private void check(final String key, final CachedSeq seq) {
        AtomicLong cur = seq.getCur();
        AtomicLong max = seq.getMax();
        long expectedValue;
        long newValue;
        while (cur.get() > max.get()) {
            expectedValue = max.get();
            newValue = expectedValue + seq.getStep();
            if (max.compareAndSet(expectedValue, newValue)) {
                int affectedRows = sequenceManager.updateValue(key, newValue);
                logger.debug("Modified sequence \"{}\" value, expected-value: {}, new-value: {}, affected-rows: {}.", key, expectedValue, newValue, affectedRows);
            }
        }
    }

    protected static final class CachedSeq {
        private final String key;
        private final AtomicLong cur;
        private final AtomicLong max;
        private int increment;
        private int cache;
        private int step;
        private volatile int version;

        public CachedSeq(String key, long value, int increment, int cache, int version) {
            this.key = key;
            this.cur = new AtomicLong(value);
            this.max = new AtomicLong(value);
            this.increment = increment;
            this.cache = cache;
            this.step = increment * cache;
            this.version = version;
        }

        public void update(int increment, int cache, int version) {
            this.version = version;
            if (this.increment != increment) {
                this.increment = increment;
            }
            if (this.cache != cache) {
                this.cache = cache;
            }
            int step = increment * cache;
            if (this.step != step) {
                this.step = step;
            }
        }

        public String getKey() {
            return key;
        }

        public int getIncrement() {
            return increment;
        }

        public int getCache() {
            return cache;
        }

        public int getStep() {
            return step;
        }

        public AtomicLong getCur() {
            return cur;
        }

        public AtomicLong getMax() {
            return max;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }
    }

}
