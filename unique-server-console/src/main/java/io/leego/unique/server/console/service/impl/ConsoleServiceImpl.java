package io.leego.unique.server.console.service.impl;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.Snapshot;
import io.leego.unique.common.enums.Mode;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.service.SequenceService;
import io.leego.unique.server.console.dto.SequenceSaveDTO;
import io.leego.unique.server.console.dto.SequenceSearchDTO;
import io.leego.unique.server.console.dto.SequenceUpdateDTO;
import io.leego.unique.server.console.service.ConsoleService;

import java.time.LocalDateTime;

/**
 * @author Yihleego
 */
public class ConsoleServiceImpl implements ConsoleService {
    private static final int DEFAULT_VERSION = 1;
    private final SequenceService sequenceService;

    public ConsoleServiceImpl(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public Result<Void> save(SequenceSaveDTO dto) {
        return sequenceService.save(new Sequence(
                dto.getKey().trim(),
                dto.getValue(),
                dto.getIncrement(),
                dto.getCache(),
                DEFAULT_VERSION,
                LocalDateTime.now(),
                null));
    }

    @Override
    public Result<Void> update(SequenceUpdateDTO dto) {
        return sequenceService.update(new Sequence(
                dto.getKey().trim(),
                null,
                dto.getIncrement(),
                dto.getCache(),
                null,
                null,
                LocalDateTime.now()));
    }

    @Override
    public Result<Void> delete(String key) {
        return sequenceService.delete(key);
    }

    @Override
    public Result<Page<Sequence>> list(SequenceSearchDTO dto) {
        return sequenceService.list();
    }

    @Override
    public Result<Sequence> get(String key) {
        return sequenceService.get(key);
    }

    @Override
    public Result<Snapshot> getSnapshot(String key) {
        return sequenceService.getSnapshot(key);
    }

    @Override
    public Result<Segment> skip(String key, int size) {
        return sequenceService.next(key, size);
    }

    @Override
    public Result<Void> load(String key) {
        return sequenceService.load(key);
    }

    @Override
    public Result<Mode> getMode() {
        return sequenceService.getMode();
    }

}
