package io.leego.unique.server.console.service.impl;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.util.ErrorCode;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;
import io.leego.unique.core.manager.SequenceManager;
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
    private final SequenceManager sequenceManager;
    private final SequenceService sequenceService;

    public ConsoleServiceImpl(SequenceManager sequenceManager, SequenceService sequenceService) {
        this.sequenceManager = sequenceManager;
        this.sequenceService = sequenceService;
    }

    @Override
    public Result<Void> save(SequenceSaveDTO dto) {
        sequenceManager.save(new Sequence(dto.getKey(), dto.getValue(), dto.getIncrement(), dto.getCache(), DEFAULT_VERSION, LocalDateTime.now(), null));
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> update(SequenceUpdateDTO dto) {
        sequenceManager.update(new Sequence(dto.getKey(), null, dto.getIncrement(), dto.getCache(), null, null, LocalDateTime.now()));
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> delete(String key) {
        sequenceManager.delete(key);
        return Result.buildSuccess();
    }

    @Override
    public Result<Page<Sequence>> list(SequenceSearchDTO dto) {
        return Result.buildSuccess(Page.of(sequenceManager.findAll()));
    }

    @Override
    public Result<Sequence> get(String key) {
        return Result.buildSuccess(sequenceManager.findByKey(key));
    }

    @Override
    public Result<SnapshotSequence> getSnapshot(String key) {
        return Result.buildSuccess(sequenceService.getSnapshot(key));
    }

    @Override
    public Result<Segment> skip(String key, int size) {
        return sequenceService.next(key, size);
    }

    @Override
    public Result<Void> load(String key) {
        Sequence sequence = sequenceManager.findByKey(key);
        if (sequence == null) {
            return Result.buildFailure(ErrorCode.NOT_FOUND, "There is no sequence named \"" + key + '\"');
        }
        sequenceService.merge(sequence);
        return Result.buildSuccess();
    }

}
