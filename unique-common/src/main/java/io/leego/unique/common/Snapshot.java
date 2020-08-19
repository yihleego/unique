package io.leego.unique.common;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Yihleego
 */
public class Snapshot implements Serializable {
    private static final long serialVersionUID = 7486120717801149583L;
    private String key;
    private long cur;
    private long max;
    private int increment;
    private int cache;
    private int version;
    private LocalDateTime snapshotTime;

    public Snapshot() {
    }

    public Snapshot(String key, long cur, long max, int increment, int cache, int version, LocalDateTime snapshotTime) {
        this.key = key;
        this.cur = cur;
        this.max = max;
        this.increment = increment;
        this.cache = cache;
        this.version = version;
        this.snapshotTime = snapshotTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCur() {
        return cur;
    }

    public void setCur(long cur) {
        this.cur = cur;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public int getCache() {
        return cache;
    }

    public void setCache(int cache) {
        this.cache = cache;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public LocalDateTime getSnapshotTime() {
        return snapshotTime;
    }

    public void setSnapshotTime(LocalDateTime snapshotTime) {
        this.snapshotTime = snapshotTime;
    }
}
