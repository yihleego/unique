package io.leego.unique.common;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Yihleego
 */
public class Segment implements Serializable {
    private static final long serialVersionUID = 1559729212695281422L;
    private long begin;
    private long end;
    private int increment;

    public Segment() {
    }

    public Segment(long begin, long end, int increment) {
        this.begin = begin;
        this.end = end;
        this.increment = increment;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    @Override
    public String toString() {
        return "Segment{" + "begin=" + begin +
                ", end=" + end +
                ", increment=" + increment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Segment))
            return false;
        Segment segment = (Segment) o;
        return begin == segment.begin &&
                end == segment.end &&
                increment == segment.increment;
    }

    @Override
    public int hashCode() {
        return Objects.hash(begin, end, increment);
    }

}
