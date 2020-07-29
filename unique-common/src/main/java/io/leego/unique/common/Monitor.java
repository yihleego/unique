package io.leego.unique.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author Yihleego
 */
public class Monitor implements Serializable {
    private static final long serialVersionUID = -2764233130794441552L;
    private List<MonitoredSequence> sequences;

    public Monitor() {
    }

    public Monitor(List<MonitoredSequence> sequences) {
        this.sequences = sequences;
    }

    public static Monitor empty() {
        return new Monitor(Collections.emptyList());
    }

    public List<MonitoredSequence> getSequences() {
        return sequences;
    }

    public void setSequences(List<MonitoredSequence> sequences) {
        this.sequences = sequences;
    }

}
