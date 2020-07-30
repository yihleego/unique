package io.leego.unique.core.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    public Sequence() {
    }

    public Sequence(String key, Long value, Integer increment, Integer cache, Integer version, LocalDateTime createTime, LocalDateTime updateTime) {
        this.key = key;
        this.value = value;
        this.increment = increment;
        this.cache = cache;
        this.version = version;
        this.createTime = createTime;
        this.updateTime = updateTime;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
