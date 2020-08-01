package io.leego.unique.client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public interface UniqueClient {

    /**
     * Obtains the next sequence.
     * @param key the sequence key.
     */
    Long next(String key);

    /**
     * Obtains the next segment.
     * @param key  the sequence key.
     * @param size the size.
     */
    LinkedList<Long> next(String key, int size);

    /**
     * Obtains the next segment.
     * @param key               the sequence key.
     * @param size              the size.
     * @param collectionFactory a supplier providing a new empty {@code Collection}
     *                          into which the sequences will be inserted.
     */
    <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

}
