package io.leego.unique.server.autoconfigure;

/**
 * @author Yihleego
 */
public final class UniqueServerConstants {
    /** For JDBC */
    public static final String TABLE_NAME = "sequence";
    /** For MongoDB */
    public static final String MONGODB_URI = "mongodb://localhost:27017";
    /** For MongoDB */
    public static final String DATABASE_NAME = "sequence";
    /** For MongoDB */
    public static final String COLLECTION_NAME = "sequence";

    private UniqueServerConstants() {
    }
}
