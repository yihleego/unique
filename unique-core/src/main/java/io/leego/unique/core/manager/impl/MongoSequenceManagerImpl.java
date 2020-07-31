package io.leego.unique.core.manager.impl;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.leego.unique.core.codec.SequenceCodec;
import io.leego.unique.core.constant.Constants;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.manager.SequenceManager;
import org.bson.codecs.IntegerCodec;
import org.bson.codecs.LongCodec;
import org.bson.codecs.StringCodec;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @author Yihleego
 */
public class MongoSequenceManagerImpl implements SequenceManager {
    protected final MongoClient mongoClient;
    protected final MongoDatabase database;
    protected final MongoCollection<Sequence> collection;
    protected final String databaseName;
    protected final String collectionName;

    public MongoSequenceManagerImpl(MongoClient mongoClient, String databaseName, String collectionName) {
        Objects.requireNonNull(mongoClient);
        Objects.requireNonNull(databaseName);
        Objects.requireNonNull(collectionName);
        CodecRegistry codecRegistry = CodecRegistries.fromCodecs(
                new SequenceCodec(),
                new StringCodec(),
                new IntegerCodec(),
                new LongCodec(),
                new LocalDateTimeCodec());
        this.mongoClient = mongoClient;
        this.database = this.mongoClient.getDatabase(databaseName).withCodecRegistry(codecRegistry);
        this.collection = this.database.getCollection(collectionName, Sequence.class);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public Sequence findByKey(String key) {
        Bson filter = Filters.eq(Constants.Mongo.KEY, key);
        return getOne(collection.find(filter, Sequence.class));
    }

    @Override
    public List<Sequence> findAll() {
        return toList(collection.find(Sequence.class));
    }

    @Override
    public int updateValue(String key, long value) {
        Bson filter = Filters.and(
                Filters.eq(Constants.Mongo.KEY, key),
                Filters.lt(Constants.Mongo.VALUE, value));
        Bson update = Updates.set(Constants.Mongo.VALUE, value);
        return (int) collection.updateOne(filter, update).getModifiedCount();
    }

    @Override
    public int save(Sequence sequence) {
        return collection.insertOne(sequence).wasAcknowledged() ? 1 : 0;
    }

    @Override
    public int update(Sequence sequence) {
        Bson filter = Filters.eq(Constants.Mongo.KEY, sequence.getKey());
        Bson update = Updates.combine(
                Updates.set(Constants.Mongo.INCREMENT, sequence.getIncrement()),
                Updates.set(Constants.Mongo.CACHE, sequence.getCache()),
                Updates.inc(Constants.Mongo.VERSION, 1),
                Updates.set(Constants.Mongo.UPDATE_TIME, sequence.getUpdateTime()));
        return (int) collection.updateOne(filter, update).getModifiedCount();
    }

    @Override
    public int delete(String key) {
        Bson filter = Filters.eq(Constants.Mongo.KEY, key);
        return (int) collection.deleteOne(filter).getDeletedCount();
    }

    protected Sequence getOne(FindIterable<Sequence> documents) {
        if (documents == null) {
            return null;
        }
        for (Sequence document : documents) {
            return document;
        }
        return null;
    }

    protected List<Sequence> toList(FindIterable<Sequence> documents) {
        if (documents == null) {
            return Collections.emptyList();
        }
        List<Sequence> list = new ArrayList<>();
        for (Sequence document : documents) {
            list.add(document);
        }
        return list;
    }

}
