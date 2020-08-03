package io.leego.unique.core.codec;

import io.leego.unique.core.constant.Constants;
import io.leego.unique.core.entity.Sequence;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

/**
 * @author Yihleego
 */
public class SequenceCodec implements Codec<Sequence> {

    /**
     * Decodes a BSON value from the given reader into an instance of the type parameter {@link Sequence}.
     * @param reader         the BSON reader
     * @param decoderContext the decoder context
     * @return an instance of the type parameter {@link Sequence}.
     */
    @Override
    public Sequence decode(BsonReader reader, DecoderContext decoderContext) {
        Sequence user = new Sequence();
        reader.readStartDocument();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if (name.equals(Constants.Mongo.KEY)) {
                user.setKey(reader.readString());
            } else if (name.equals(Constants.Mongo.VALUE)) {
                user.setValue(reader.readInt64());
            } else if (name.equals(Constants.Mongo.INCREMENT)) {
                user.setIncrement(reader.readInt32());
            } else if (name.equals(Constants.Mongo.CACHE)) {
                user.setCache(reader.readInt32());
            } else if (name.equals(Constants.Mongo.VERSION)) {
                user.setVersion(reader.readInt32());
            } else if (name.equals(Constants.Mongo.CREATE_TIME)) {
                user.setCreateTime(toLocalDateTime(reader.readDateTime()));
            } else if (name.equals(Constants.Mongo.UPDATE_TIME)) {
                user.setUpdateTime(toLocalDateTime(reader.readDateTime()));
            } else {
                reader.skipValue();
            }
        }
        reader.readEndDocument();
        return user;
    }

    /**
     * Encode an instance of the type parameter {@link Sequence} into a BSON value.
     * @param writer         the BSON writer to encode into
     * @param value          the value to encode
     * @param encoderContext the encoder context
     */
    @Override
    public void encode(BsonWriter writer, Sequence value, EncoderContext encoderContext) {
        writer.writeStartDocument();
        if (value.getKey() != null) {
            writer.writeString(Constants.Mongo.KEY, value.getKey());
        }
        if (value.getValue() != null) {
            writer.writeInt64(Constants.Mongo.VALUE, value.getValue());
        }
        if (value.getIncrement() != null) {
            writer.writeInt32(Constants.Mongo.INCREMENT, value.getIncrement());
        }
        if (value.getCache() != null) {
            writer.writeInt32(Constants.Mongo.CACHE, value.getCache());
        }
        if (value.getVersion() != null) {
            writer.writeInt32(Constants.Mongo.VERSION, value.getVersion());
        }
        if (value.getCreateTime() != null) {
            writer.writeDateTime(Constants.Mongo.CREATE_TIME, toTimestamp(value.getCreateTime()));
        }
        if (value.getUpdateTime() != null) {
            writer.writeDateTime(Constants.Mongo.UPDATE_TIME, toTimestamp(value.getUpdateTime()));
        }
        writer.writeEndDocument();
    }

    @Override
    public Class<Sequence> getEncoderClass() {
        return Sequence.class;
    }

    private LocalDateTime toLocalDateTime(long timestamp) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private long toTimestamp(LocalDateTime dateTime) {
        return dateTime.toInstant(OffsetDateTime.now().getOffset()).toEpochMilli();
    }

}

