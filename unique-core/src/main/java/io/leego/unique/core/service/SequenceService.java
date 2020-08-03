package io.leego.unique.core.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;

/**
 * @author Yihleego
 */
public interface SequenceService {

    /**
     * Obtains the next sequence.
     * @param key the sequence key.
     * @return the next sequence.
     */
    Result<Long> next(String key);

    /**
     * Obtains the next segment.
     * @param key  the sequence key.
     * @param size the size.
     * @return the next segment.
     */
    Result<Segment> next(String key, int size);

    /**
     * Initializes
     */
    void init();

    /**
     * Merges data if the specified sequence is not present or has been modified.
     */
    void merge(Sequence sequence);

    /**
     * Obtains the snapshot.
     * @return the snapshot.
     */
    SnapshotSequence getSnapshot(String key);

}
