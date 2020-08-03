package io.leego.unique.server.console.dto;

import io.leego.unique.common.Pageable;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class SequenceSearchDTO extends Pageable implements Serializable {
    private static final long serialVersionUID = -6617259018536613108L;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

}
