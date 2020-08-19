package io.leego.unique.server.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * Configuration properties for unique-server.
 * @author Yihleego
 */
@ConfigurationProperties("spring.unique")
public class UniqueServerProperties {
    /** Default table used when the configured table is {@code null}. */
    public static final String TABLE_NAME = "sequence";
    /** Default URI used when the configured URI is {@code null}. */
    public static final String MONGODB_URI = "mongodb://localhost:27017";
    /** Default database used when the configured database is {@code null}. */
    public static final String DATABASE_NAME = "unique";
    /** Default collection used when the configured collection is {@code null}. */
    public static final String COLLECTION_NAME = "sequence";

    @NestedConfigurationProperty
    private Jdbc jdbc = new Jdbc();
    @NestedConfigurationProperty
    private MongoDB mongodb = new MongoDB();
    @NestedConfigurationProperty
    private Cluster cluster = new Cluster();
    @NestedConfigurationProperty
    private Console console = new Console();

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

    public Cluster getCluster() {
        return cluster;
    }

    public void setCluster(Cluster cluster) {
        this.cluster = cluster;
    }

    public Console getConsole() {
        return console;
    }

    public void setConsole(Console console) {
        this.console = console;
    }

    protected static class Jdbc {
        /** Whether to enable JDBC. */
        private boolean enabled = false;
        /** Fully qualified name of the JDBC driver. */
        private String driverClassName;
        /** JDBC URL of the database. */
        private String url;
        /** Login username of the database. */
        private String username;
        /** Login password of the database. */
        private String password;
        /** Table name. */
        private String table = TABLE_NAME;

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
        /** Whether to enable MongoDB. */
        private boolean enabled = false;
        /** Mongo database URI. */
        private String uri = MONGODB_URI;
        /** Login user of the mongo server. */
        private String username;
        /** Login password of the mongo server. */
        private String password;
        /** Database name. */
        private String database = DATABASE_NAME;
        /** Collection name. */
        private String collection = COLLECTION_NAME;

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

    protected static class Cluster {
        /** Whether to enable cluster. */
        private boolean enabled = false;
        /** Retries */
        private int retries = 10;
        /** Whether to allow obtaining single sequence. */
        private boolean allowSingle = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public int getRetries() {
            return retries;
        }

        public void setRetries(int retries) {
            this.retries = retries;
        }

        public boolean isAllowSingle() {
            return allowSingle;
        }

        public void setAllowSingle(boolean allowSingle) {
            this.allowSingle = allowSingle;
        }
    }

    protected static class Console {
        /** Whether to enable console. */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

}
