package io.leego.unique.server.console.controller;

import io.leego.unique.common.Monitor;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.dto.SequenceSaveDTO;
import io.leego.unique.core.dto.SequenceUpdateDTO;
import io.leego.unique.core.service.SequenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yihleego
 */
@RestController
@RequestMapping("consoles")
public class ConsoleController {
    @Autowired
    private SequenceService sequenceService;

    @GetMapping("monitors")
    public Result<Monitor> getMonitor() {
        return sequenceService.getMonitor();
    }

    @PatchMapping("configurations")
    public Result<Void> load() {
        return sequenceService.load();
    }

    @PatchMapping("sequences/{key}/skip/{size}")
    public Result<Segment> skip(@PathVariable String key, @PathVariable int size) {
        return sequenceService.next(key, size);
    }

    @PostMapping("sequences")
    public Result<Void> save(@RequestBody SequenceSaveDTO dto) {
        return sequenceService.save(dto);
    }

    @PutMapping("sequences/{key}")
    public Result<Void> update(@PathVariable String key, @RequestBody SequenceUpdateDTO dto) {
        dto.setKey(key);
        return sequenceService.update(dto);
    }

    @DeleteMapping("sequences/{key}")
    public Result<Void> delete(@PathVariable String key) {
        return sequenceService.delete(key);
    }

}
