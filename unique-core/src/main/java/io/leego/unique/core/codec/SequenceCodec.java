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

    @Override
    public void encode(BsonWriter writer, Sequence sequence, EncoderContext encoderContext) {
        writer.writeStartDocument();
        if (sequence.getKey() != null) {
            writer.writeString(Constants.Mongo.KEY, sequence.getKey());
        }
        if (sequence.getValue() != null) {
            writer.writeInt64(Constants.Mongo.VALUE, sequence.getValue());
        }
        if (sequence.getIncrement() != null) {
            writer.writeInt32(Constants.Mongo.INCREMENT, sequence.getIncrement());
        }
        if (sequence.getCache() != null) {
            writer.writeInt32(Constants.Mongo.CACHE, sequence.getCache());
        }
        if (sequence.getVersion() != null) {
            writer.writeInt32(Constants.Mongo.VERSION, sequence.getVersion());
        }
        if (sequence.getCreateTime() != null) {
            writer.writeDateTime(Constants.Mongo.CREATE_TIME, toTimestamp(sequence.getCreateTime()));
        }
        if (sequence.getUpdateTime() != null) {
            writer.writeDateTime(Constants.Mongo.UPDATE_TIME, toTimestamp(sequence.getUpdateTime()));
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

