package io.leego.unique.common.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Yihleego
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final String DEFAULT_GROUP_NAME = "system";
    private final ThreadGroup group;
    private final AtomicLong threadNumber = new AtomicLong(0);
    private final String namePrefix;
    private final boolean daemon;

    public NamedThreadFactory(String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.group = new ThreadGroup(DEFAULT_GROUP_NAME);
    }

    public NamedThreadFactory(String groupName, String namePrefix, boolean daemon) {
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.group = new ThreadGroup(groupName);
    }

    public static ThreadFactory build(String groupName, String namePrefix, boolean daemon) {
        return new NamedThreadFactory(groupName, namePrefix, daemon);
    }

    public static ThreadFactory build(String namePrefix, boolean daemon) {
        return new NamedThreadFactory(namePrefix, daemon);
    }

    public static ThreadFactory build(boolean daemon) {
        return new NamedThreadFactory(null, daemon);
    }

    public static ThreadFactory build() {
        return new NamedThreadFactory(null, false);
    }

    private String nextName() {
        if (namePrefix == null || namePrefix.length() == 0) {
            return group.getName() + "-" + threadNumber.getAndIncrement();
        } else {
            return group.getName() + "-" + namePrefix + "-" + threadNumber.getAndIncrement();
        }
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = new Thread(
                group,
                runnable,
                nextName());
        thread.setDaemon(daemon);
        if (thread.getPriority() != Thread.NORM_PRIORITY) {
            thread.setPriority(Thread.NORM_PRIORITY);
        }
        return thread;
    }

}
