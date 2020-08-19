package io.leego.unique.client.service;

import feign.hystrix.FallbackFactory;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.exception.ObtainErrorException;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Yihleego
 */
@Component
public class UniqueServiceFallbackFactory implements FallbackFactory<UniqueServiceFeignClient> {

    @Override
    public UniqueServiceFeignClient create(Throwable cause) {
        return new UniqueServiceFeignClient() {
            @Override
            public Result<Long> next(String key) {
                throw new ObtainErrorException("Failed to obtain the sequence \"" + key + "\"", cause);
            }

            @Override
            public Result<Segment> next(String key, int size) {
                throw new ObtainErrorException("Failed to obtain the sequence \"" + key + "\", size: " + size, cause);
            }

            @Override
            public Result<Set<String>> contains(Set<String> keys) {
                throw new ObtainErrorException("Failed to obtain keys", cause);
            }
        };
    }

}