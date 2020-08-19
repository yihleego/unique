package io.leego.unique.client.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

/**
 * Configuration properties for unique-client.
 * @author Yihleego
 */
@ConfigurationProperties("spring.unique")
public class UniqueClientProperties {
    /** Whether to enable auto-configure. */
    private boolean enabled = true;
    /** The absolute URL or resolvable hostname (the protocol is optional). */
    private String uri;
    /** The name of the service with optional protocol prefix. */
    private String serviceId;
    /** Request timeout. */
    private Duration timeout;
    @NestedConfigurationProperty
    private Cache cache = new Cache();
    @NestedConfigurationProperty
    private Hystrix hystrix = new Hystrix();
    @NestedConfigurationProperty
    private Mybatis mybatis = new Mybatis();
    @NestedConfigurationProperty
    private Validation validation = new Validation();

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

    public Hystrix getHystrix() {
        return hystrix;
    }

    public void setHystrix(Hystrix hystrix) {
        this.hystrix = hystrix;
    }

    public Mybatis getMybatis() {
        return mybatis;
    }

    public void setMybatis(Mybatis mybatis) {
        this.mybatis = mybatis;
    }

    public Validation getValidation() {
        return validation;
    }

    public void setValidation(Validation validation) {
        this.validation = validation;
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
            /** Whether to enable mybatis-plugin. */
            private boolean enabled = false;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }
        }
    }

    protected static class Hystrix {
        /** Whether to enable Hystrix. */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    protected static class Cache {
        /** Whether to enable caching. Please use cached mode if the servers are clustered. */
        private boolean enabled = false;
        /** Cache size. */
        private Integer size;

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

    protected static class Validation {
        /** Whether to enable validation. */
        private boolean enabled = true;
        /** Keys to be validated. */
        private List<String> keys = Collections.emptyList();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getKeys() {
            return keys;
        }

        public void setKeys(List<String> keys) {
            this.keys = keys;
        }
    }

}
