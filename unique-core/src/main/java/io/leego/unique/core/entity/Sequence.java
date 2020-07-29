package io.leego.unique.core.entity;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class Sequence implements Serializable {
    private static final long serialVersionUID = 2155768537646933406L;
    private String key;
    private Long value;
    private Integer increment;
    private Integer cache;
    private Integer version;

    public Sequence() {
    }

    public Sequence(String key, Long value, Integer increment, Integer cache, Integer version) {
        this.key = key;
        this.value = value;
        this.increment = increment;
        this.cache = cache;
        this.version = version;
    }

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

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

}
