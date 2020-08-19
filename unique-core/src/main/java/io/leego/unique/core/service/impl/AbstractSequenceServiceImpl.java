package io.leego.unique.core.service.impl;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.manager.SequenceManager;
import io.leego.unique.core.service.SequenceService;

/**
 * @author Yihleego
 */
public abstract class AbstractSequenceServiceImpl implements SequenceService {
    protected final SequenceManager sequenceManager;

    public AbstractSequenceServiceImpl(SequenceManager sequenceManager) {
        this.sequenceManager = sequenceManager;
    }

    @Override
    public Result<Void> save(Sequence sequence) {
        sequenceManager.save(sequence);
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> update(Sequence sequence) {
        sequenceManager.update(sequence);
        return Result.buildSuccess();
    }

    @Override
    public Result<Void> delete(String key) {
        sequenceManager.delete(key);
        return Result.buildSuccess();
    }

    @Override
    public Result<Page<Sequence>> list() {
        return Result.buildSuccess(Page.of(sequenceManager.findAll()));
    }

    @Override
    public Result<Sequence> get(String key) {
        return Result.buildSuccess(sequenceManager.findByKey(key));
    }

}
