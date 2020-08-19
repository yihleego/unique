package io.leego.unique.client;

import io.leego.unique.common.Validation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public interface UniqueClient {

    /**
     * Obtains the next sequence.
     * @param key the sequence key.
     * @return the next sequence.
     */
    Long next(String key);

    /**
     * Obtains the next segment of the sequence.
     * @param key  the sequence key.
     * @param size the size.
     * @return the next segment of the sequence.
     */
    LinkedList<Long> next(String key, int size);

    /**
     * Obtains the next segment of the sequence.
     * @param key               the sequence key.
     * @param size              the size.
     * @param collectionFactory a supplier providing a new empty {@code Collection}
     *                          into which the sequences will be inserted.
     * @return the next segment of the sequence.
     */
    <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

    /**
     * Returns the result of validation.
     * @param keys the keys.
     * @return the result of validation.
     */
    Validation validateKeys(Set<String> keys);

}
