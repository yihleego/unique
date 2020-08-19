package io.leego.unique.core.service;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.Snapshot;
import io.leego.unique.common.enums.Mode;
import io.leego.unique.common.service.UniqueService;
import io.leego.unique.core.entity.Sequence;

import java.util.Set;

/**
 * @author Yihleego
 */
public interface SequenceService extends UniqueService {

    /**
     * Obtains the next sequence.
     * @param key the key.
     * @return the next sequence.
     */
    @Override
    Result<Long> next(String key);

    /**
     * Obtains the next segment.
     * @param key  the key.
     * @param size the size.
     * @return the next segment.
     */
    @Override
    Result<Segment> next(String key, int size);

    /**
     * Returns the keys if they are present.
     * @param keys the keys.
     * @return the present keys.
     */
    @Override
    Result<Set<String>> contains(Set<String> keys);

    /**
     * Obtains the snapshot.
     * @param key the key.
     * @return the snapshot.
     */
    Result<Snapshot> getSnapshot(String key);

    /**
     * Loads data if the specified sequence is absent or has been modified.
     * @param key the key.
     */
    Result<Void> load(String key);

    /**
     * Returns mode.
     * @return the mode.
     */
    Result<Mode> getMode();

    /**
     * Saves the sequence.
     * @param sequence the sequence to be saved.
     */
    Result<Void> save(Sequence sequence);

    /**
     * Updates the sequence.
     * @param sequence the sequence to be updated.
     */
    Result<Void> update(Sequence sequence);

    /**
     * Deletes the sequence.
     * @param key the sequence key to be deleted.
     */
    Result<Void> delete(String key);

    /**
     * Queries sequences.
     * @return sequences
     */
    Result<Page<Sequence>> list();

    /**
     * Returns the sequence of the specified key.
     * @param key the sequence key to be queried.
     * @return the sequence of the specified key.
     */
    Result<Sequence> get(String key);
}
