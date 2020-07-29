package io.leego.unique.client.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;

/**
 * @author Yihleego
 */
public interface UniqueService {

    Result<Long> next(String key);

    Result<Segment> next(String key, int size);

}
