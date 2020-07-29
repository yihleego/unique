package io.leego.unique.client.service;

import feign.Param;
import feign.RequestLine;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;

/**
 * @author Yihleego
 */
public interface UniqueServiceRequester extends UniqueService {

    @Override
    @RequestLine("GET /sequences/{key}")
    Result<Long> next(@Param("key") String key);

    @Override
    @RequestLine("GET /sequences/{key}/segments?size={size}")
    Result<Segment> next(@Param("key") String key, @Param("size") int size);

}
