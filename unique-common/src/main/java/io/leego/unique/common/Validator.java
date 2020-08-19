package io.leego.unique.common;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Yihleego
 */
public class Validator {
    private final Set<String> keys;

    public Validator() {
        this.keys = new HashSet<>();
    }

    public Validator(Set<String> keys) {
        this.keys = keys;
    }

    public Set<String> getKeys() {
        return keys;
    }

    public Validator add(String key) {
        if (key != null) {
            this.keys.add(key);
        }
        return this;
    }

    public Validator add(String... keys) {
        if (keys != null) {
            Collections.addAll(this.keys, keys);
        }
        return this;
    }

    public Validator add(Collection<String> keys) {
        if (keys != null) {
            this.keys.addAll(keys);
        }
        return this;
    }
}
