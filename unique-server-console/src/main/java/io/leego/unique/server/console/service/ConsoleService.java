package io.leego.unique.server.console.service;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.entity.SnapshotSequence;
import io.leego.unique.server.console.dto.SequenceSaveDTO;
import io.leego.unique.server.console.dto.SequenceSearchDTO;
import io.leego.unique.server.console.dto.SequenceUpdateDTO;

/**
 * @author Yihleego
 */
public interface ConsoleService {

    Result<Void> save(SequenceSaveDTO dto);

    Result<Void> update(SequenceUpdateDTO dto);

    Result<Void> delete(String key);

    Result<Page<Sequence>> list(SequenceSearchDTO dto);

    Result<Sequence> get(String key);

    Result<SnapshotSequence> getSnapshot(String key);

    Result<Segment> skip(String key, int size);

    Result<Void> load(String key);

}
