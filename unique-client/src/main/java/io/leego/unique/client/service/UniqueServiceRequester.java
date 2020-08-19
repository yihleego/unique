package io.leego.unique.client.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.service.UniqueService;

import java.util.Set;

/**
 * @author Yihleego
 */
public interface UniqueServiceRequester extends UniqueService {

    @Override
    @Headers("Content-Type: application/json")
    @RequestLine("GET /sequences/{key}")
    Result<Long> next(@Param("key") String key);

    @Override
    @Headers("Content-Type: application/json")
    @RequestLine("GET /sequences/{key}/segments?size={size}")
    Result<Segment> next(@Param("key") String key, @Param("size") int size);

    @Override
    @Headers("Content-Type: application/json")
    @RequestLine("POST /sequences/keys")
    Result<Set<String>> contains(Set<String> keys);

}
