package io.leego.unique.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yihleego
 */
public enum HttpMethod {
    GET,
    HEAD,
    POST,
    PUT,
    PATCH,
    DELETE,
    OPTIONS,
    TRACE;

    private static final Map<String, HttpMethod> map = new HashMap<>(16);

    static {
        for (HttpMethod e : values()) {
            map.put(e.name(), e);
        }
    }

    public static HttpMethod get(String method) {
        return method != null ? map.get(method) : null;
    }

}
