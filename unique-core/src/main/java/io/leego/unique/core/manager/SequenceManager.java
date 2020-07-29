package io.leego.unique.core.manager;


import io.leego.unique.core.entity.Sequence;

import java.util.List;

/**
 * @author Yihleego
 */
public interface SequenceManager {

    List<Sequence> query();

    int updateValue(String key, long value);

    int save(Sequence sequence);

    int update(Sequence sequence);

    int delete(String key);

}
