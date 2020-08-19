package io.leego.unique.client.service;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.service.UniqueService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

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

    @Override
    @PostMapping("sequences/keys")
    Result<Set<String>> contains(@RequestBody Set<String> keys);

}
