package io.leego.unique.client;

import java.util.Collection;
import java.util.LinkedList;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public interface UniqueClient {

    Long next(String key);

    LinkedList<Long> next(String key, int size);

    <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory);

}
