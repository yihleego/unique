package io.leego.unique.server.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * @author Yihleego
 */
@ConfigurationProperties("spring.unique")
public class UniqueServerProperties {
    @NestedConfigurationProperty
    private Jdbc jdbc = new Jdbc();
    @NestedConfigurationProperty
    private MongoDB mongodb = new MongoDB();

    public Jdbc getJdbc() {
        return jdbc;
    }

    public void setJdbc(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public MongoDB getMongodb() {
        return mongodb;
    }

    public void setMongodb(MongoDB mongodb) {
        this.mongodb = mongodb;
    }

    protected static class Jdbc {
        private boolean enabled = false;
        private String driverClassName;
        private String url;
        private String username;
        private String password;
        private String table = UniqueServerConstants.TABLE_NAME;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getTable() {
            return table;
        }

        public void setTable(String table) {
            this.table = table;
        }
    }

    protected static class MongoDB {
        private boolean enabled = false;
        private String uri = UniqueServerConstants.MONGODB_URI;
        private String username;
        private String password;
        private String database = UniqueServerConstants.DATABASE_NAME;
        private String collection = UniqueServerConstants.COLLECTION_NAME;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDatabase() {
            return database;
        }

        public void setDatabase(String database) {
            this.database = database;
        }

        public String getCollection() {
            return collection;
        }

        public void setCollection(String collection) {
            this.collection = collection;
        }
    }

    protected static class Console {
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
