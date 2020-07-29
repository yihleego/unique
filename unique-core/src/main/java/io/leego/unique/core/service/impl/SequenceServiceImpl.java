package io.leego.unique.core.service.impl;

import io.leego.unique.common.Monitor;
import io.leego.unique.common.MonitoredSequence;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.util.ErrorCode;
import io.leego.unique.core.dto.SequenceSaveDTO;
import io.leego.unique.core.dto.SequenceUpdateDTO;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.manager.SequenceManager;
import io.leego.unique.core.service.SequenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Yihleego
 */
public class SequenceServiceImpl implements SequenceService {
    private static final Logger logger = LoggerFactory.getLogger(SequenceServiceImpl.class);
    private static final int DEFAULT_VERSION = 1;
    private final ConcurrentMap<String, CachedSeq> seqMap = new ConcurrentHashMap<>(32);
    private final SequenceManager sequenceManager;

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
    public Result<Void> save(SequenceSaveDTO dto) {
        Sequence save = new Sequence(dto.getKey(), dto.getValue(), dto.getIncrement(), dto.getCache(), DEFAULT_VERSION);
        sequenceManager.save(save);
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> update(SequenceUpdateDTO dto) {
        Sequence update = new Sequence(dto.getKey(), null, dto.getIncrement(), dto.getCache(), null);
        sequenceManager.update(update);
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> delete(String key) {
        sequenceManager.delete(key);
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> load() {
        logger.info("Starting loading data from the database.");
        for (Sequence sequence : sequenceManager.query()) {
            CachedSeq o = seqMap.computeIfAbsent(sequence.getKey(), key -> {
                logger.debug("The sequence \"{}\" has been loaded.", key);
                return new CachedSeq(key, sequence.getValue(), sequence.getIncrement(), sequence.getCache(), sequence.getVersion());
            });
            if (sequence.getVersion() != o.getVersion()) {
                logger.debug("The sequence \"{}\" has been modified.", sequence.getKey());
                o.update(sequence.getIncrement(), sequence.getCache(), sequence.getVersion());
            }
        }
        logger.info("Finished loading data from the database.");
        return Result.buildSuccess();
    }

    @Override
    public Result<Monitor> getMonitor() {
        if (seqMap.isEmpty()) {
            return Result.buildSuccess(Monitor.empty());
        }
        Set<String> keys = seqMap.keySet();
        List<MonitoredSequence> sequences = new ArrayList<>(keys.size());
        for (String key : keys) {
            CachedSeq cached = seqMap.get(key);
            if (cached == null) {
                continue;
            }
            sequences.add(new MonitoredSequence(
                    cached.getKey(),
                    cached.getCur().get(),
                    cached.getMax().get(),
                    cached.getIncrement(),
                    cached.getCache(),
                    cached.getVersion()));
        }
        return Result.buildSuccess(new Monitor(sequences));
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

    private static final class CachedSeq {
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

        public CachedSeq update(int increment, int cache, int version) {
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
            return this;
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
