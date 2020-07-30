package io.leego.unique.core.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;

/**
 * @author Yihleego
 */
public interface SequenceService {

    Result<Long> next(String key);

    Result<Segment> next(String key, int size);

    void init();

    void merge(Sequence sequence);

    SnapshotSequence getSnapshot(String key);

}
