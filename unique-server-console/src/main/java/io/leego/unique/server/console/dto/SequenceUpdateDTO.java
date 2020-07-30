package io.leego.unique.server.console.dto;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceUpdateDTO implements Serializable {
    private static final long serialVersionUID = -7602590318738910759L;
    private String key;
    private Integer increment;
    private Integer cache;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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
