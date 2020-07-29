package io.leego.unique.core.dto;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceSkipDTO implements Serializable {
    private static final long serialVersionUID = -2120880524756386546L;
    private String key;
    private Long delta;

    public SequenceSkipDTO() {
    }

    public SequenceSkipDTO(String key, Long delta) {
        this.key = key;
        this.delta = delta;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Long getDelta() {
        return delta;
    }

    public void setDelta(Long delta) {
        this.delta = delta;
    }
}
