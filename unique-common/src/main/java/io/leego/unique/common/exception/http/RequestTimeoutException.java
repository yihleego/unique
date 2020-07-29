package io.leego.unique.common.exception.http;

import io.leego.unique.common.enums.HttpStatus;

/**
 * Request Timeout 408
 * @author Yihleego
 */
public class RequestTimeoutException extends HttpException {
    public static final HttpStatus HTTP_STATUS = HttpStatus.REQUEST_TIMEOUT;
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>RequestTimeoutException</code> with no detail message.
     */
    public RequestTimeoutException() {
        super(HTTP_STATUS);
    }

    /**
     * Constructs a <code>RequestTimeoutException</code> with the specified detail message.
     * @param message detail message
     */
    public RequestTimeoutException(String message) {
        super(HTTP_STATUS, message);
    }

    /**
     * Constructs a <code>RequestTimeoutException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public RequestTimeoutException(String message, Throwable cause) {
        super(HTTP_STATUS, message, cause);
    }

    /**
     * Constructs a <code>RequestTimeoutException</code> with the cause.
     * @param cause the cause
     */
    public RequestTimeoutException(Throwable cause) {
        super(HTTP_STATUS, cause);
    }

}
