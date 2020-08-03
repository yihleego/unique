package io.leego.unique.client.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;

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

}
