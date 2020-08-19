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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yihleego
 */
public class ClusterSequenceServiceImpl extends AbstractSequenceServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(ClusterSequenceServiceImpl.class);
    private static final int DEFAULT_RETRIES = 10;
    private final int retries;
    private final boolean allowSingle;

    public ClusterSequenceServiceImpl(SequenceManager sequenceManager) {
        super(sequenceManager);
        this.retries = DEFAULT_RETRIES;
        this.allowSingle = false;
    }

    public ClusterSequenceServiceImpl(SequenceManager sequenceManager, int retries) {
        this(sequenceManager, retries, false);
    }

    public ClusterSequenceServiceImpl(SequenceManager sequenceManager, int retries, boolean allowSingle) {
        super(sequenceManager);
        if (retries < 1) {
            throw new IllegalArgumentException("Invalid retries: " + retries);
        }
        this.retries = retries;
        this.allowSingle = allowSingle;
    }

    @Override
    public Result<Long> next(String key) {
        if (allowSingle) {
            return next(key, 1).map(segment -> segment != null ? segment.getEnd() : null);
        } else {
            return Result.buildFailure(ErrorCode.UNSUPPORTED, "Obtain a single sequence is unsupported");
        }
    }

    @Override
    public Result<Segment> next(String key, int size) {
        if (size <= 0) {
            logger.error("The size cannot be negative or zero.");
            return Result.buildFailure(ErrorCode.ILLEGAL_ARGUMENT, "The size cannot be negative or zero");
        }
        for (int i = 1; i <= retries; i++) {
            Sequence sequence = sequenceManager.findByKey(key);
            if (sequence == null) {
                logger.error("There is no sequence \"{}\".", key);
                return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence \"" + key + '\"');
            }
            int increment = sequence.getIncrement();
            int version = sequence.getVersion();
            long expectedValue = sequence.getValue();
            long newValue = expectedValue + size * increment;
            int affectedRows = sequenceManager.compareAndUpdateValue(key, expectedValue, newValue, version);
            if (affectedRows > 0) {
                logger.debug("Modified sequence \"{}\" value, expected-value: {}, new-value: {}, affected-rows: {}.", key, expectedValue, newValue, affectedRows);
                return Result.buildSuccess(new Segment(expectedValue + increment, newValue, increment));
            } else {
                logger.debug("Retry to modify sequence \"{}\", time(s): {}.", key, i);
            }
        }
        logger.error("Failed to modify sequence \"{}\", time(s): {}.", key, retries);
        return Result.buildFailure(ErrorCode.ERROR, "Oops! An error has occurred while obtaining the sequence \"" + key + "\"!");
    }

    @Override
    public Result<Set<String>> contains(Set<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return Result.buildSuccess(Collections.emptySet());
        }
        return Result.buildSuccess(new HashSet<>(sequenceManager.findKeys(keys)));
    }

    @Override
    public Result<Snapshot> getSnapshot(String key) {
        return Result.buildFailure(ErrorCode.UNSUPPORTED, "Unsupported");
    }

    @Override
    public Result<Void> load(String key) {
        return Result.buildFailure(ErrorCode.UNSUPPORTED, "Unsupported");
    }

    @Override
    public Result<Mode> getMode() {
        return Result.buildSuccess(Mode.CLUSTER);
    }

}
