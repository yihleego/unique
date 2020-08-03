package io.leego.unique.client.codec;

import feign.Response;
import feign.codec.ErrorDecoder;
import io.leego.unique.common.enums.HttpStatus;
import io.leego.unique.common.exception.HttpException;

/**
 * @author Yihleego
 */
public class ResponseErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new HttpException(HttpStatus.get(response.status()), response.reason());
    }

}