package io.leego.unique.client.autoconfigure;

import io.leego.unique.client.UniqueClientConstants;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

/**
 * @author Yihleego
 */
@ConfigurationProperties("spring.unique")
public class UniqueClientProperties {
    private boolean enabled = true;
    private String uri;
    private String serviceId;
    private Duration timeout = UniqueClientConstants.TIMEOUT;
    @NestedConfigurationProperty
    private Cache cache = new Cache();
    @NestedConfigurationProperty
    private Mybatis mybatis = new Mybatis();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Duration getTimeout() {
        return timeout;
    }

    public void setTimeout(Duration timeout) {
        this.timeout = timeout;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    public Mybatis getMybatis() {
        return mybatis;
    }

    public void setMybatis(Mybatis mybatis) {
        this.mybatis = mybatis;
    }

    protected static class Mybatis {
        @NestedConfigurationProperty
        private Plugin plugin = new Plugin();

        public Plugin getPlugin() {
            return plugin;
        }

        public void setPlugin(Plugin plugin) {
            this.plugin = plugin;
        }

        public static class Plugin {
            private boolean enabled = false;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    protected static class Cache {
        private boolean enabled = false;
        private Integer size = UniqueClientConstants.CACHE_SIZE;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Integer getSize() {
            return size;
        }

        public void setSize(Integer size) {
            this.size = size;
        }
    }

}
