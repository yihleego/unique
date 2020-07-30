package io.leego.unique.server.console.controller;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;
import io.leego.unique.server.console.dto.SequenceSaveDTO;
import io.leego.unique.server.console.dto.SequenceSearchDTO;
import io.leego.unique.server.console.dto.SequenceUpdateDTO;
import io.leego.unique.server.console.service.ConsoleService;
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
    @Autowired(required = false)
    private ConsoleService consoleService;

    @PostMapping("sequences")
    public Result<Void> save(@RequestBody SequenceSaveDTO dto) {
        return consoleService.save(dto);
    }

    @PutMapping("sequences/{key}")
    public Result<Void> update(@PathVariable String key, @RequestBody SequenceUpdateDTO dto) {
        dto.setKey(key);
        return consoleService.update(dto);
    }

    @DeleteMapping("sequences/{key}")
    public Result<Void> delete(@PathVariable String key) {
        return consoleService.delete(key);
    }

    @GetMapping("sequences")
    public Result<Page<Sequence>> list(SequenceSearchDTO dto) {
        return consoleService.list(dto);
    }

    @GetMapping("sequences/{key}/snapshot")
    public Result<SnapshotSequence> getSnapshot(@PathVariable String key) {
        return consoleService.getSnapshot(key);
    }

    @PatchMapping("sequences/{key}/skip/{size}")
    public Result<Segment> skip(@PathVariable String key, @PathVariable int size) {
        return consoleService.skip(key, size);
    }

    @PutMapping("configurations/{key}")
    public Result<Void> load(@PathVariable String key) {
        return consoleService.load(key);
    }

}