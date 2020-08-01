package io.leego.unique.client;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.okhttp.OkHttpClient;
import io.leego.unique.client.codec.ResponseDecoder;
import io.leego.unique.client.codec.ResponseErrorDecoder;
import io.leego.unique.client.service.UniqueService;
import io.leego.unique.client.service.UniqueServiceRequester;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Yihleego
 */
public final class UniqueClients {
    private UniqueClients() {
    }

    /**
     * Creates a {@link SimpleUniqueClient}.
     **/
    public static UniqueClient newSimple(UniqueService uniqueService) {
        return new SimpleUniqueClient(uniqueService);
    }

    /**
     * Creates a {@link CachedUniqueClient} that caches a fixed number of sequences.
     **/
    public static UniqueClient newCached(UniqueService uniqueService) {
        return new CachedUniqueClient(uniqueService);
    }

    /**
     * Creates a {@link CachedUniqueClient} that caches a fixed number of sequences.
     **/
    public static UniqueClient newCached(UniqueService uniqueService, Integer cacheSize) {
        return new CachedUniqueClient(uniqueService, cacheSize);
    }

    /**
     * Creates a {@link CachedUniqueClient} that caches a fixed number of sequences.
     **/
    public static UniqueClient newCached(UniqueService uniqueService, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(uniqueService, cacheSize, timeout);
    }

    /**
     * Convenience method to create a Builder.
     */
    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private static final String HTTP = "http://";
        private static final String HTTPS = "https://";
        private static final String COLON = ":";
        /** Default URL used when the configured URL is {@code null}. */
        private static final String URL = "http://localhost";
        /** Default host used when the configured host is {@code null}. */
        private static final String HOST = "localhost";
        /** Default port used when the configured port is {@code null}. */
        private static final Integer PORT = 80;
        /** Default timeout used when the configured timeout is {@code null}. */
        private static final Duration TIMEOUT = Duration.ofSeconds(3L);
        /** Request URL. Cannot be set with {@link #host}, {@link #port}, {@link #ssl}. */
        private String url = URL;
        /** Server host. Cannot be set with {@link #url}. */
        private String host = HOST;
        /** Server port. Cannot be set with {@link #url}. */
        private Integer port = PORT;
        /** Whether to enable SSL. Cannot be set with {@link #url}. */
        private boolean ssl = false;
        /** Request timeout. */
        private Duration timeout = TIMEOUT;
        /** Whether to enable caching. */
        private boolean cached = false;
        /** Cache size. */
        private Integer cacheSize;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder host(String host) {
            this.host = host;
            return this;
        }

        public Builder port(Integer port) {
            this.port = port;
            return this;
        }

        public Builder ssl(boolean ssl) {
            this.ssl = ssl;
            return this;
        }

        public Builder timeout(Duration timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder cached(boolean cached) {
            this.cached = cached;
            return this;
        }

        public Builder cacheSize(Integer cacheSize) {
            this.cacheSize = cacheSize;
            return this;
        }

        public UniqueClient build() {
            if (this.url == null && host == null) {
                throw new IllegalArgumentException("Url or host cannot be empty");
            }
            String targetUrl = this.url != null
                    ? this.url
                    : (ssl ? HTTPS : HTTP) + host + COLON + (port != null && port > 0 ? port : PORT);
            Duration timeout = this.timeout != null ? this.timeout : TIMEOUT;
            Request.Options options = new Request.Options(
                    timeout.toMillis(),
                    TimeUnit.MILLISECONDS,
                    timeout.toMillis(),
                    TimeUnit.MILLISECONDS,
                    true);
            UniqueService uniqueService = Feign.builder()
                    .client(new OkHttpClient())
                    .decoder(new ResponseDecoder())
                    .errorDecoder(new ResponseErrorDecoder())
                    .retryer(Retryer.NEVER_RETRY)
                    .options(options)
                    .target(UniqueServiceRequester.class, targetUrl);
            if (cached) {
                return new CachedUniqueClient(uniqueService, cacheSize, timeout);
            } else {
                return new SimpleUniqueClient(uniqueService);
            }
        }

    }

}
