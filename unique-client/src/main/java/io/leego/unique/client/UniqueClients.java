package io.leego.unique.client;

import feign.Feign;
import feign.Request;
import feign.Retryer;
import feign.okhttp.OkHttpClient;
import io.leego.unique.client.codec.ResponseDecoder;
import io.leego.unique.client.codec.ResponseErrorDecoder;
import io.leego.unique.client.service.UniqueService;
import io.leego.unique.client.service.UniqueServiceRequester;
import io.leego.unique.client.service.impl.RemoteUniqueServiceImpl;
import io.leego.unique.core.service.SequenceService;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Yihleego
 */
public final class UniqueClients {
    private static final String HTTP = "http://";
    private static final String HTTPS = "https://";
    private static final String COLON = ":";

    private UniqueClients() {
    }

    public static UniqueClient newSimple(UniqueService uniqueService) {
        return new SimpleUniqueClient(uniqueService);
    }

    public static UniqueClient newSimple(SequenceService sequenceService) {
        return new SimpleUniqueClient(newUniqueService(sequenceService));
    }

    public static UniqueClient newSimple(String url) {
        return new SimpleUniqueClient(newUniqueService(url));
    }

    public static UniqueClient newSimple(String host, Integer port) {
        return new SimpleUniqueClient(newUniqueService(host, port, false));
    }

    public static UniqueClient newSimple(String host, Integer port, boolean ssl) {
        return new SimpleUniqueClient(newUniqueService(host, port, ssl));
    }

    public static UniqueClient newSimple(String url, Duration timeout) {
        return new SimpleUniqueClient(newUniqueService(url, timeout));
    }

    public static UniqueClient newSimple(String host, Integer port, Duration timeout) {
        return new SimpleUniqueClient(newUniqueService(host, port, false, timeout));
    }

    public static UniqueClient newSimple(String host, Integer port, boolean ssl, Duration timeout) {
        return new SimpleUniqueClient(newUniqueService(host, port, ssl, timeout));
    }


    public static UniqueClient newCached(UniqueService uniqueService) {
        return new CachedUniqueClient(uniqueService);
    }

    public static UniqueClient newCached(UniqueService uniqueService, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(uniqueService, cacheSize, timeout);
    }

    public static UniqueClient newCached(SequenceService sequenceService) {
        return new CachedUniqueClient(newUniqueService(sequenceService));
    }

    public static UniqueClient newCached(SequenceService sequenceService, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(newUniqueService(sequenceService), cacheSize, timeout);
    }

    public static UniqueClient newCached(String url) {
        return new CachedUniqueClient(newUniqueService(url));
    }

    public static UniqueClient newCached(String host, Integer port) {
        return new CachedUniqueClient(newUniqueService(host, port, false));
    }

    public static UniqueClient newCached(String host, Integer port, boolean ssl) {
        return new CachedUniqueClient(newUniqueService(host, port, ssl));
    }

    public static UniqueClient newCached(String url, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(newUniqueService(url, timeout), cacheSize, timeout);
    }

    public static UniqueClient newCached(String host, Integer port, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(newUniqueService(host, port, false, timeout), cacheSize, timeout);
    }

    public static UniqueClient newCached(String host, Integer port, boolean ssl, Integer cacheSize, Duration timeout) {
        return new CachedUniqueClient(newUniqueService(host, port, ssl, timeout), cacheSize, timeout);
    }


    private static UniqueService newUniqueService(String url, Duration timeout) {
        Request.Options options = new Request.Options(
                timeout.toMillis(),
                TimeUnit.MILLISECONDS,
                timeout.toMillis(),
                TimeUnit.MILLISECONDS,
                true);
        return Feign.builder()
                .client(new OkHttpClient())
                .decoder(new ResponseDecoder())
                .errorDecoder(new ResponseErrorDecoder())
                .retryer(Retryer.NEVER_RETRY)
                .options(options)
                .target(UniqueServiceRequester.class, url);
    }

    private static UniqueService newUniqueService(String url) {
        return newUniqueService(url, UniqueClientConstants.TIMEOUT);
    }

    private static UniqueService newUniqueService(String host, Integer port, boolean ssl, Duration timeout) {
        return newUniqueService(buildUrl(host, port, ssl), timeout);
    }

    private static UniqueService newUniqueService(String host, Integer port, boolean ssl) {
        return newUniqueService(buildUrl(host, port, ssl), UniqueClientConstants.TIMEOUT);
    }

    private static UniqueService newUniqueService(SequenceService sequenceService) {
        return new RemoteUniqueServiceImpl(sequenceService);
    }

    private static String buildUrl(String host, Integer port, boolean ssl) {
        return (ssl ? HTTPS : HTTP) + host + COLON + port;
    }

}
