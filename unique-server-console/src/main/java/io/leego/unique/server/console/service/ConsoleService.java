package io.leego.unique.server.console.service;

import io.leego.unique.common.Page;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.Snapshot;
import io.leego.unique.common.enums.Mode;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.server.console.dto.SequenceSaveDTO;
import io.leego.unique.server.console.dto.SequenceSearchDTO;
import io.leego.unique.server.console.dto.SequenceUpdateDTO;

/**
 * @author Yihleego
 */
public interface ConsoleService {

    /** Saves the sequence. */
    Result<Void> save(SequenceSaveDTO dto);

    /** Updates the sequence. */
    Result<Void> update(SequenceUpdateDTO dto);

    /** Deletes the sequence. */
    Result<Void> delete(String key);

    /** Queries sequences. */
    Result<Page<Sequence>> list(SequenceSearchDTO dto);

    /** Returns the sequence of the specified key. */
    Result<Sequence> get(String key);

    /** Returns a sequence snapshot. */
    Result<Snapshot> getSnapshot(String key);

    /** Skips a segment of sequence. */
    Result<Segment> skip(String key, int size);

    /** Loads sequence. */
    Result<Void> load(String key);

    /** Returns mode. */
    Result<Mode> getMode();

}
