package io.leego.unique.client.service;

import feign.hystrix.FallbackFactory;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Yihleego
 */
@Component
public class UniqueServiceFallbackFactory implements FallbackFactory<UniqueServiceFeignClient> {
    private static final Logger logger = LoggerFactory.getLogger(UniqueServiceFallbackFactory.class);

    @Override
    public UniqueServiceFeignClient create(Throwable cause) {
        return new UniqueServiceFeignClient() {
            @Override
            public Result<Long> next(String key) {
                logger.error("", cause);
                return Result.buildFailure(cause.getMessage());
            }

            @Override
            public Result<Segment> next(String key, int size) {
                logger.error("", cause);
                return Result.buildFailure(cause.getMessage());
            }
        };
    }

}