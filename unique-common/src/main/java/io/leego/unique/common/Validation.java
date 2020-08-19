package io.leego.unique.common;

import java.util.Collections;
import java.util.Set;

/**
 * @author Yihleego
 */
public class Validation {
    private final boolean success;
    private final Set<String> absentKeys;

    public Validation(boolean success, Set<String> absentKeys) {
        this.success = success;
        this.absentKeys = absentKeys;
    }

    public static Validation buildSuccess() {
        return new Validation(true, Collections.emptySet());
    }

    public static Validation buildFailure(Set<String> keys) {
        return new Validation(false, keys);
    }

    public boolean isSuccess() {
        return success;
    }

    public Set<String> getAbsentKeys() {
        return absentKeys;
    }

}
