package io.leego.unique.client.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Yihleego
 */
@FeignClient(value = "${spring.unique.service-id:unique-server}", url = "${spring.unique.uri:}",
        fallbackFactory = UniqueServiceFallbackFactory.class)
public interface UniqueServiceFeignClient extends UniqueService {

    @Override
    @GetMapping("sequences/{key}")
    Result<Long> next(@PathVariable("key") String key);

    @Override
    @GetMapping("sequences/{key}/segments")
    Result<Segment> next(@PathVariable("key") String key, @RequestParam("size") int size);

}
