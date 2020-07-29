package io.leego.unique.core.service;

import io.leego.unique.common.Monitor;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.dto.SequenceSaveDTO;
import io.leego.unique.core.dto.SequenceUpdateDTO;

/**
 * @author Yihleego
 */
public interface SequenceService {

    Result<Long> next(String key);

    Result<Segment> next(String key, int size);

    Result<Void> save(SequenceSaveDTO dto);

    Result<Void> update(SequenceUpdateDTO dto);

    Result<Void> delete(String key);

    Result<Void> load();

    Result<Monitor> getMonitor();

}
