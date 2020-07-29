package io.leego.unique.common.exception.http;

import io.leego.unique.common.enums.HttpStatus;

/**
 * Locked 423
 * @author Yihleego
 */
public class ServiceUnavailableException extends HttpException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.SERVICE_UNAVAILABLE;
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>ServiceUnavailableException</code> with no detail message.
     */
    public ServiceUnavailableException() {
        super(HTTP_STATUS);
    }

    /**
     * Constructs a <code>ServiceUnavailableException</code> with the specified detail message.
     * @param message detail message
     */
    public ServiceUnavailableException(String message) {
        super(HTTP_STATUS, message);
    }

    /**
     * Constructs a <code>ServiceUnavailableException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public ServiceUnavailableException(String message, Throwable cause) {
        super(HTTP_STATUS, message, cause);
    }

    /**
     * Constructs a <code>ServiceUnavailableException</code> with the cause.
     * @param cause the cause
     */
    public ServiceUnavailableException(Throwable cause) {
        super(HTTP_STATUS, cause);
    }

}
