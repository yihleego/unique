package io.leego.unique.server.console.dto;

import io.leego.unique.common.Message;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceSaveDTO implements Serializable {
    private static final long serialVersionUID = -7722494547043387459L;
    @NotEmpty(message = Message.KEY_REQUIRED)
    private String key;
    @NotNull(message = Message.VALUE_REQUIRED)
    @Min(value = 0, message = Message.VALUE_INVALID)
    private Long value;
    @NotNull(message = Message.INCREMENT_REQUIRED)
    @Min(value = 1, message = Message.INCREMENT_INVALID)
    private Integer increment;
    @NotNull(message = Message.CACHE_REQUIRED)
    @Min(value = 1, message = Message.CACHE_INVALID)
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
