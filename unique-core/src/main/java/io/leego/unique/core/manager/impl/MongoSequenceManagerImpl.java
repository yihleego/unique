package io.leego.unique.core.manager.impl;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import io.leego.unique.common.exception.ConversionException;
import io.leego.unique.core.entity.Sequence;
import io.leego.unique.core.enums.SequenceColumn;
import io.leego.unique.core.manager.SequenceManager;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
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
    protected final MongoCollection<Document> collection;
    protected final String databaseName;
    protected final String collectionName;

    public MongoSequenceManagerImpl(String url, String username, String password, String databaseName, String collectionName) {
        Objects.requireNonNull(url);
        Objects.requireNonNull(databaseName);
        Objects.requireNonNull(collectionName);
        MongoClientSettings.Builder builder = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(url));
        if (username != null && username.length() > 0) {
            MongoCredential credential = MongoCredential.createCredential(
                    username,
                    databaseName,
                    password != null ? password.toCharArray() : null);
            builder.credential(credential);
        }
        this.mongoClient = MongoClients.create(builder.build());
        this.database = this.mongoClient.getDatabase(databaseName);
        this.collection = this.database.getCollection(collectionName);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    public MongoSequenceManagerImpl(MongoClient mongoClient, String databaseName, String collectionName) {
        Objects.requireNonNull(mongoClient);
        Objects.requireNonNull(databaseName);
        Objects.requireNonNull(collectionName);
        this.mongoClient = mongoClient;
        this.database = this.mongoClient.getDatabase(databaseName);
        this.collection = this.database.getCollection(collectionName);
        this.databaseName = databaseName;
        this.collectionName = collectionName;
    }

    @Override
    public List<Sequence> query() {
        return toList(collection.find());
    }

    @Override
    public int updateValue(String key, long value) {
        Bson filter = Filters.and(
                Filters.eq(SequenceColumn.KEY.getMongoColumn(), key),
                Filters.lt(SequenceColumn.VALUE.getMongoColumn(), value));
        Bson update = Updates.set(SequenceColumn.VALUE.getMongoColumn(), value);
        return (int) collection.updateOne(filter, update).getModifiedCount();
    }

    @Override
    public int save(Sequence sequence) {
        Document save = new Document()
                .append(SequenceColumn.KEY.getMongoColumn(), sequence.getKey())
                .append(SequenceColumn.VALUE.getMongoColumn(), sequence.getValue())
                .append(SequenceColumn.INCREMENT.getMongoColumn(), sequence.getIncrement())
                .append(SequenceColumn.CACHE.getMongoColumn(), sequence.getCache())
                .append(SequenceColumn.VERSION.getMongoColumn(), sequence.getVersion());
        return collection.insertOne(save).wasAcknowledged() ? 1 : 0;
    }

    @Override
    public int update(Sequence sequence) {
        Bson filter = Filters.eq(SequenceColumn.KEY.getMongoColumn(), sequence.getKey());
        Bson update = Updates.combine(
                Updates.set(SequenceColumn.INCREMENT.getMongoColumn(), sequence.getIncrement()),
                Updates.set(SequenceColumn.CACHE.getMongoColumn(), sequence.getCache()),
                Updates.inc(SequenceColumn.VERSION.getMongoColumn(), 1));
        return (int) collection.updateOne(filter, update).getModifiedCount();
    }

    @Override
    public int delete(String key) {
        Bson filter = Filters.eq(SequenceColumn.KEY.getMongoColumn(), key);
        return (int) collection.deleteOne(filter).getDeletedCount();
    }

    protected List<Sequence> toList(FindIterable<Document> documents) {
        if (documents == null) {
            return Collections.emptyList();
        }
        try (MongoCursor<Document> cursor = documents.cursor()) {
            if (!cursor.hasNext()) {
                return Collections.emptyList();
            }
            PropertyDescriptor[] descriptors = Introspector.getBeanInfo(Sequence.class).getPropertyDescriptors();
            List<Sequence> list = new ArrayList<>();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                Sequence sequence = new Sequence();
                list.add(sequence);
                for (PropertyDescriptor descriptor : descriptors) {
                    SequenceColumn column = SequenceColumn.get(descriptor.getName());
                    if (column != null) {
                        Object value = document.get(column.getMongoColumn(), column.getType());
                        descriptor.getWriteMethod().invoke(sequence, value);
                    }
                }
            }
            return list;
        } catch (IntrospectionException | InvocationTargetException | IllegalAccessException e) {
            throw new ConversionException(e);
        }
    }

}
