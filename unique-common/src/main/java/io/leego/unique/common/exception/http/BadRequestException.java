package io.leego.unique.common.exception.http;

import io.leego.unique.common.enums.HttpStatus;

/**
 * Bad Request 400
 * @author Yihleego
 */
public class BadRequestException extends HttpException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.BAD_REQUEST;
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>BadRequestException</code> with no detail message.
     */
    public BadRequestException() {
        super(HTTP_STATUS);
    }

    /**
     * Constructs a <code>BadRequestException</code> with the specified detail message.
     * @param message detail message
     */
    public BadRequestException(String message) {
        super(HTTP_STATUS, message);
    }

    /**
     * Constructs a <code>BadRequestException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public BadRequestException(String message, Throwable cause) {
        super(HTTP_STATUS, message, cause);
    }

    /**
     * Constructs a <code>BadRequestException</code> with the cause.
     * @param cause the cause
     */
    public BadRequestException(Throwable cause) {
        super(HTTP_STATUS, cause);
    }

}
