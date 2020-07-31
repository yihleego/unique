package io.leego.unique.core.constant;

/**
 * @author Yihleego
 */
public final class Constants {
    private Constants() {
    }

    public static final class Property {
        public static final String KEY = "key";
        public static final String VALUE = "value";
        public static final String INCREMENT = "increment";
        public static final String CACHE = "cache";
        public static final String VERSION = "version";
        public static final String CREATE_TIME = "createTime";
        public static final String UPDATE_TIME = "updateTime";
    }

    public static final class Jdbc {
        public static final String KEY = "seq_key";
        public static final String VALUE = "seq_value";
        public static final String INCREMENT = "seq_increment";
        public static final String CACHE = "seq_cache";
        public static final String VERSION = "seq_version";
        public static final String CREATE_TIME = "seq_create_time";
        public static final String UPDATE_TIME = "seq_update_time";
    }

    public static final class Mongo {
        public static final String KEY = "_id";
        public static final String VALUE = "value";
        public static final String INCREMENT = "increment";
        public static final String CACHE = "cache";
        public static final String VERSION = "version";
        public static final String CREATE_TIME = "create_time";
        public static final String UPDATE_TIME = "update_time";
    }

}
