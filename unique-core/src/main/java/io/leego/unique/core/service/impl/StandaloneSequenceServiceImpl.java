package io.leego.unique.core.service.impl;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.Snapshot;
import io.leego.unique.common.enums.Mode;
import io.leego.unique.common.util.ErrorCode;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.manager.SequenceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Yihleego
 */
public class StandaloneSequenceServiceImpl extends AbstractSequenceServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(StandaloneSequenceServiceImpl.class);
    private final ConcurrentMap<String, CachedSeq> seqMap = new ConcurrentHashMap<>(32);

    public StandaloneSequenceServiceImpl(SequenceManager sequenceManager) {
        super(sequenceManager);
        this.init();
    }

    @Override
    public Result<Long> next(String key) {
        CachedSeq seq = seqMap.get(key);
        if (seq == null) {
            logger.error("There is no sequence \"{}\".", key);
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence \"" + key + '\"');
        }
        long value = seq.getCur().addAndGet(seq.getIncrement());
        check(seq);
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
            logger.error("There is no sequence \"{}\".", key);
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence \"" + key + '\"');
        }
        int increment = seq.getIncrement();
        long end = seq.getCur().addAndGet(size * increment);
        long begin = end - (size - 1) * increment;
        check(seq);
        return Result.buildSuccess(new Segment(begin, end, increment));
    }

    @Override
    public Result<Set<String>> contains(Set<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Result.buildSuccess(Collections.emptySet());
        }
        Set<String> presents = new HashSet<>();
        for (String key : keys) {
            if (seqMap.containsKey(key)) {
                presents.add(key);
            }
        }
        return Result.buildSuccess(presents);
    }

    @Override
    public Result<Snapshot> getSnapshot(String key) {
        CachedSeq seq = seqMap.get(key);
        if (seq == null) {
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence \"" + key + '\"');
        }
        return Result.buildSuccess(
                new Snapshot(
                        seq.getKey(),
                        seq.getCur().get(),
                        seq.getMax().get(),
                        seq.getIncrement(),
                        seq.getCache(),
                        seq.getVersion(),
                        LocalDateTime.now()));
    }

    @Override
    public Result<Void> load(String key) {
        Sequence sequence = sequenceManager.findByKey(key);
        if (sequence == null) {
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence \"" + key + '\"');
        }
        merge(sequence);
        return Result.buildSuccess();
    }

    @Override
    public Result<Mode> getMode() {
        return Result.buildSuccess(Mode.STANDALONE);
    }

    private void init() {
        logger.info("Starting loading data from the database.");
        sequenceManager.findAll().forEach(this::merge);
        logger.info("Finished loading data from the database.");
    }

    private void merge(final Sequence sequence) {
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

    private void check(final CachedSeq seq) {
        String key = seq.getKey();
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
