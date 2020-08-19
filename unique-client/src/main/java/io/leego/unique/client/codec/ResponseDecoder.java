package io.leego.unique.client.codec;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import io.leego.unique.common.enums.HttpStatus;
import io.leego.unique.common.exception.HttpException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Yihleego
 */
public class ResponseDecoder extends FeignCodec implements Decoder {
    private final ConcurrentMap<String, JavaType> typeMap = new ConcurrentHashMap<>();

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
