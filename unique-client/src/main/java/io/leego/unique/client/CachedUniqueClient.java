package io.leego.unique.client;

import io.leego.unique.client.service.UniqueService;
import io.leego.unique.common.Result;
import io.leego.unique.common.Segment;
import io.leego.unique.common.exception.KeyNotFoundException;
import io.leego.unique.common.exception.ObtainErrorException;
import io.leego.unique.common.exception.ObtainTimeoutException;
import io.leego.unique.common.util.NamedThreadFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @author Yihleego
 */
public class CachedUniqueClient extends AbstractUniqueClient {
    protected final ConcurrentMap<String, CachedSeq> seqMap = new ConcurrentHashMap<>(32);
    protected final ExecutorService executor = Executors.newFixedThreadPool(5, NamedThreadFactory.build("unique", "cache-sync", true));
    protected final UniqueService uniqueService;
    protected final int cacheSize;
    protected final Duration timeout;
    protected final float factor;

    public CachedUniqueClient(UniqueService uniqueService) {
        Objects.requireNonNull(uniqueService);
        this.uniqueService = uniqueService;
        this.cacheSize = UniqueClientConstants.CACHE_SIZE;
        this.timeout = UniqueClientConstants.TIMEOUT;
        this.factor = UniqueClientConstants.FACTOR;
    }

    public CachedUniqueClient(UniqueService uniqueService, int cacheSize, Duration timeout) {
        Objects.requireNonNull(uniqueService);
        Objects.requireNonNull(timeout);
        this.uniqueService = uniqueService;
        this.cacheSize = cacheSize;
        this.timeout = timeout;
        this.factor = UniqueClientConstants.FACTOR;
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public Duration getTimeout() {
        return timeout;
    }

    @Override
    public Long next(String key) {
        CachedSeq seq = getSeq(key);
        try {
            Long value = seq.poll();
            if (value != null) {
                return value;
            } else {
                if (seq.isPresent()) {
                    throw new ObtainTimeoutException("Obtain values timeout");
                } else {
                    throw new KeyNotFoundException("The sequence named \"" + key + "\" was not found");
                }
            }
        } catch (InterruptedException e) {
            throw new ObtainErrorException(e);
        }
    }

    @Override
    public <C extends Collection<Long>> C next(String key, int size, Supplier<C> collectionFactory) {
        if (size <= 0) {
            throw new IllegalArgumentException("The size cannot be negative or zero.");
        }
        C collection = collectionFactory.get();
        CachedSeq seq = getSeq(key);
        try {
            for (int i = 0; i < size; i++) {
                if (i != 0 && seq.isEmpty()) {
                    trySync(seq);
                }
                Long value = seq.poll();
                if (value != null) {
                    collection.add(value);
                } else {
                    if (seq.isPresent()) {
                        throw new ObtainTimeoutException("Obtain values timeout");
                    } else {
                        throw new KeyNotFoundException("The sequence named \"" + key + "\" was not found");
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new ObtainErrorException(e);
        }
        return collection;
    }

    protected CachedSeq getSeq(final String key) {
        CachedSeq seq = seqMap.computeIfAbsent(key,
                k -> new CachedSeq(k, cacheSize, factor, timeout.toMillis(), timeout.toMillis()));
        trySync(seq);
        return seq;
    }

    private void trySync(final CachedSeq seq) {
        if (!seq.isSyncable()) {
            return;
        }
        executor.execute(() -> {
            if (!seq.isSyncable()) {
                return;
            }
            synchronized (seq) {
                if (!seq.isSyncable()) {
                    return;
                }
                seq.setSyncing(true);
                try {
                    int requiredSize = seq.getCapacity() - seq.getSize();
                    if (requiredSize > 0) {
                        Result<Segment> result = uniqueService.next(seq.getKey(), requiredSize);
                        if (Result.isSuccessful(result)) {
                            if (!seq.isPresent()) {
                                seq.setPresent(true);
                            }
                            Segment segment = result.getData();
                            for (long i = segment.getBegin(); i <= segment.getEnd(); i++) {
                                seq.offer(i);
                            }
                        }
                    }
                } finally {
                    seq.setLastSyncTime(System.currentTimeMillis());
                    seq.setSyncing(false);
                }
            }
        });
    }

    protected static final class CachedSeq {
        private final String key;
        private final int capacity;
        private final float factor;
        private final int threshold;
        /** interval (milliseconds) */
        private final long interval;
        /** timeout (milliseconds) */
        private final long timeout;
        private final BlockingQueue<Long> queue;
        private volatile boolean syncing;
        private volatile boolean present;
        private volatile long lastSyncTime;

        public CachedSeq(String key, int capacity, float factor, long interval, long timeout) {
            if (capacity <= 0 || factor >= 1 || factor < 0) {
                throw new IllegalArgumentException();
            }
            this.key = key;
            this.capacity = capacity;
            this.factor = factor;
            this.threshold = (int) (capacity * factor);
            this.interval = interval;
            this.timeout = timeout;
            this.queue = new LinkedBlockingQueue<>();
            this.syncing = false;
            this.present = false;
            this.lastSyncTime = 0L;
        }

        public Long poll() throws InterruptedException {
            return queue.poll(timeout, TimeUnit.MILLISECONDS);
        }

        public Long poll(long timeout, TimeUnit unit) throws InterruptedException {
            return queue.poll(timeout, unit);
        }

        public boolean offer(Long value) {
            return queue.offer(value);
        }

        public boolean isSyncable() {
            return queue.size() <= threshold
                    && !syncing
                    && (present || lastSyncTime + interval < System.currentTimeMillis());
        }

        public boolean isEmpty() {
            return queue.isEmpty();
        }

        public int getSize() {
            return queue.size();
        }

        public String getKey() {
            return key;
        }

        public int getCapacity() {
            return capacity;
        }

        public float getFactor() {
            return factor;
        }

        public int getThreshold() {
            return threshold;
        }

        public long getInterval() {
            return interval;
        }

        public BlockingQueue<Long> getQueue() {
            return queue;
        }

        public boolean isSyncing() {
            return syncing;
        }

        public void setSyncing(boolean syncing) {
            this.syncing = syncing;
        }

        public boolean isPresent() {
            return present;
        }

        public void setPresent(boolean present) {
            this.present = present;
        }

        public long getLastSyncTime() {
            return lastSyncTime;
        }

        public void setLastSyncTime(long lastSyncTime) {
            this.lastSyncTime = lastSyncTime;
        }

    }

}
