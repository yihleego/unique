package io.leego.unique.client.codec;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import io.leego.unique.common.enums.HttpStatus;
import io.leego.unique.common.exception.http.HttpException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Yihleego
 */
public class ResponseDecoder implements Decoder {
    private final ObjectMapper objectMapper;
    private final ConcurrentMap<String, JavaType> typeMap = new ConcurrentHashMap<>();

    public ResponseDecoder() {
        this.objectMapper = new ObjectMapper()
                .registerModules(new ParameterNamesModule(), new Jdk8Module())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
                .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    @Override
    public Object decode(Response response, Type type) throws IOException {
        if (response.status() != HttpStatus.OK.getCode()) {
            throw new HttpException(HttpStatus.get(response.status()), response.reason());
        }
        try (InputStream inputStream = response.body().asInputStream()) {
            if (type instanceof Class) {
                return objectMapper.readValue(inputStream, (Class<?>) type);
            } else if (type instanceof ParameterizedType) {
                return objectMapper.readValue(inputStream, getJavaType(type));
            } else {
                throw new DecodeException(response.status(), "Type is not an instance of Class or ParameterizedType: " + type, response.request());
            }
        }
    }

    protected JavaType getJavaType(Type type) {
        return typeMap.computeIfAbsent(type.getTypeName(), key -> createJavaType(type));
    }

    protected JavaType createJavaType(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Class<?> rowClass = (Class<?>) ((ParameterizedType) type).getRawType();
            JavaType[] javaTypes = new JavaType[actualTypeArguments.length];
            for (int i = 0; i < actualTypeArguments.length; i++) {
                javaTypes[i] = createJavaType(actualTypeArguments[i]);
            }
            return TypeFactory.defaultInstance().constructParametricType(rowClass, javaTypes);
        } else {
            Class<?> clazz = (Class<?>) type;
            return TypeFactory.defaultInstance().constructParametricType(clazz, new JavaType[0]);
        }
    }

}
