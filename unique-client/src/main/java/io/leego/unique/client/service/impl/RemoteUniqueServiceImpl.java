package io.leego.unique.client.service.impl;

import io.leego.unique.client.service.UniqueService;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.service.SequenceService;

/**
 * @author Yihleego
 */
public class RemoteUniqueServiceImpl implements UniqueService {
    private final SequenceService sequenceService;

    public RemoteUniqueServiceImpl(SequenceService sequenceService) {
        this.sequenceService = sequenceService;
    }

    @Override
    public Result<Long> next(String key) {
        return sequenceService.next(key);
    }

    @Override
    public Result<Segment> next(String key, int size) {
        return sequenceService.next(key, size);
    }

}
