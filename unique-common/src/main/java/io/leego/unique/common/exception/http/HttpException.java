package io.leego.unique.common.exception.http;

import io.leego.unique.common.enums.HttpStatus;

/**
 * HttpException for wrapping runtime {@code Exceptions} with an root cause.
 * <p>This class is {@code abstract} to force the programmer to extend the class. </p>
 * @author Yihleego
 */
public class HttpException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    protected final HttpStatus httpStatus;

    /**
     * Constructs an <code>HttpException</code> with no detail message.
     * @param httpStatus HttpStatus
     */
    public HttpException(HttpStatus httpStatus) {
        super();
        this.httpStatus = httpStatus;
    }

    /**
     * Constructs an <code>HttpException</code> with the specified detail message.
     * @param httpStatus HttpStatus
     * @param message    detail message
     */
    public HttpException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * Constructs an <code>HttpException</code> with the specified detail message and cause.
     * @param httpStatus HttpStatus
     * @param message    detail message
     * @param cause      the cause
     */
    public HttpException(HttpStatus httpStatus, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
    }

    /**
     * Constructs an <code>HttpException</code> with the cause.
     * @param httpStatus HttpStatus
     * @param cause      the cause
     */
    public HttpException(HttpStatus httpStatus, Throwable cause) {
        super(cause);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
