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
     */
    Result<Long> next(String key);

    /**
     * Obtains the next segment.
     * @param key  the sequence key.
     * @param size the size.
     */
    Result<Segment> next(String key, int size);

}
