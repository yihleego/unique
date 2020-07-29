package io.leego.unique.common.exception.http;

import io.leego.unique.common.enums.HttpStatus;

/**
 * Not Found 404
 * @author Yihleego
 */
public class NotFoundException extends HttpException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.NOT_FOUND;
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>NotFoundException</code> with no detail message.
     */
    public NotFoundException() {
        super(HTTP_STATUS);
    }

    /**
     * Constructs a <code>NotFoundException</code> with the specified detail message.
     * @param message detail message
     */
    public NotFoundException(String message) {
        super(HTTP_STATUS, message);
    }

    /**
     * Constructs a <code>NotFoundException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public NotFoundException(String message, Throwable cause) {
        super(HTTP_STATUS, message, cause);
    }

    /**
     * Constructs a <code>NotFoundException</code> with the cause.
     * @param cause the cause
     */
    public NotFoundException(Throwable cause) {
        super(HTTP_STATUS, cause);
    }

}
