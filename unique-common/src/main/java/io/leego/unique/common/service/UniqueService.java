package io.leego.unique.common.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;

import java.util.Set;

/**
 * @author Yihleego
 */
public interface UniqueService {

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
     * Returns the keys if they are present.
     * @return the present keys.
     */
    Result<Set<String>> contains(Set<String> keys);

}
