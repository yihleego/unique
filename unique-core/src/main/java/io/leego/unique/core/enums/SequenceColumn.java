package io.leego.unique.core.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Yihleego
 */
public enum SequenceColumn {
    KEY("key", String.class, "key", "_id"),
    VALUE("value", Long.class, "value", "value"),
    INCREMENT("increment", Integer.class, "increment", "increment"),
    CACHE("cache", Integer.class, "cache", "cache"),
    VERSION("version", Integer.class, "version", "version");

    private static final Map<String, SequenceColumn> map = new HashMap<>();

    static {
        for (SequenceColumn e : values()) {
            map.put(e.getFieldName(), e);
        }
    }

    private final String fieldName;
    private final Class<?> type;
    private final String dbColumn;
    private final String mongoColumn;

    SequenceColumn(String fieldName, Class<?> type, String dbColumn, String mongoColumn) {
        this.fieldName = fieldName;
        this.type = type;
        this.dbColumn = dbColumn;
        this.mongoColumn = mongoColumn;
    }

    public static SequenceColumn get(String fieldName) {
        return fieldName != null ? map.get(fieldName) : null;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Class<?> getType() {
        return type;
    }

    public String getDbColumn() {
        return dbColumn;
    }

    public String getMongoColumn() {
        return mongoColumn;
    }

}
