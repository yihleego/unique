package io.leego.unique.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yihleego
 */
public enum SequenceColumn {
    KEY("key", "seq_key", "_id"),
    VALUE("value", "seq_value", "value"),
    INCREMENT("increment", "seq_increment", "increment"),
    CACHE("cache", "seq_cache", "cache"),
    VERSION("version", "seq_version", "version"),
    CREATE_TIME("createTime", "seq_create_time", "create_time"),
    UPDATE_TIME("updateTime", "seq_update_time", "update_time");

    private final String property;
    private final String dbColumn;
    private final String mongoColumn;

    SequenceColumn(String property, String dbColumn, String mongoColumn) {
        this.property = property;
        this.dbColumn = dbColumn;
        this.mongoColumn = mongoColumn;
    }

    public String getProperty() {
        return property;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public String getMongoColumn() {
        return mongoColumn;
    }

    private static final Map<String, SequenceColumn> map = new HashMap<>();

    static {
        for (SequenceColumn e : values()) {
            map.put(e.getProperty(), e);
        }
    }

    public static SequenceColumn get(String property) {
        return property != null ? map.get(property) : null;
    }

}
