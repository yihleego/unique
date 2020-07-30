package io.leego.unique.server.console.dto;

import io.leego.unique.common.Pageable;
import io.leego.unique.server.console.enums.SequenceSort;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceSearchDTO extends Pageable implements Serializable {
    private static final long serialVersionUID = -6617259018536613108L;
    private String key;
    private SequenceSort sort;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public SequenceSort getSort() {
        return sort;
    }

    public void setSort(SequenceSort sort) {
        this.sort = sort;
    }

}
