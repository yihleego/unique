package io.leego.unique.server.controller;

import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author Yihleego
 */
@RestController
@RequestMapping("sequences")
public class UniqueController {
    @Autowired
    private SequenceService sequenceService;

    @GetMapping("{key}")
    public Result<Long> next(@PathVariable String key) {
        return sequenceService.next(key);
    }

    @GetMapping("{key}/segments")
    public Result<Segment> next(@PathVariable String key, @RequestParam int size) {
        return sequenceService.next(key, size);
    }

    @PostMapping("keys")
    public Result<Set<String>> contains(@RequestBody Set<String> keys) {
        return sequenceService.contains(keys);
    }

}
