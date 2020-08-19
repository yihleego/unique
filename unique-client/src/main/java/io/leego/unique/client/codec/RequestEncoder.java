package io.leego.unique.client.codec;

import com.fasterxml.jackson.core.JsonProcessingException;
import feign.RequestTemplate;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;

/**
 * @author Yihleego
 */
public class RequestEncoder extends FeignCodec implements Encoder {

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        try {
            template.body(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new EncodeException("Failed to encode", e);
        }
    }

}
