package io.leego.unique.client;

import java.time.Duration;

/**
 * @author Yihleego
 */
public final class UniqueClientConstants {
    public static final float FACTOR = 0.2f;
    public static final int CACHE_SIZE = 1000;
    public static final Duration TIMEOUT = Duration.ofSeconds(5L);

    private UniqueClientConstants() {
    }
}
