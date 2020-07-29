package io.leego.unique.common;

import java.io.Serializable;

/**
 * @author Yihleego
 */
public class MonitoredSequence implements Serializable {
    private static final long serialVersionUID = 7672981058505022894L;
    private String key;
    private long cur;
    private long max;
    private int increment;
    private int cache;
    private int version;

    public MonitoredSequence() {
    }

    public MonitoredSequence(String key, long cur, long max, int increment, int cache, int version) {
        this.key = key;
        this.cur = cur;
        this.max = max;
        this.increment = increment;
        this.cache = cache;
        this.version = version;
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
}
