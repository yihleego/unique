package io.leego.unique.server.console.dto;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceSaveDTO implements Serializable {
    private static final long serialVersionUID = -7722494547043387459L;
    private String key;
    private Long value;
    private Integer increment;
    private Integer cache;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public Integer getCache() {
        return cache;
    }

    public void setCache(Integer cache) {
        this.cache = cache;
    }

}
