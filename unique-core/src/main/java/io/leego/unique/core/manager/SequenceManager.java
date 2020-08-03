package io.leego.unique.core.manager;

import io.leego.unique.core.entity.Sequence;

import java.util.List;

/**
 * @author Yihleego
 */
public interface SequenceManager {

    /**
     * Retrieves an entity by the key.
     * @param key must not be {@code null}.
     * @return the entity with the given key or {@code null} if none found.
     */
    Sequence findByKey(String key);

    /**
     * Returns all entities of the {@link Sequence}.
     * @return all entities
     */
    List<Sequence> findAll();

    /**
     * Updates the value.
     * @param key   must not be {@code null}.
     * @param value target value.
     * @return the modified count.
     */
    int updateValue(String key, long value);

    /**
     * Saves a given entity.
     * @param sequence must not be {@code null}.
     * @return the acknowledged count.
     */
    int save(Sequence sequence);

    /**
     * Updates a given entity.
     * @param sequence must not be {@code null}.
     * @return the modified count.
     */
    int update(Sequence sequence);

    /**
     * Deletes a given entity.
     * @param key must not be {@code null}.
     * @return the deleted count.
     */
    int delete(String key);

}
