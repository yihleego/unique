package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class InvalidTableException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>InvalidTableException</code> with no detail message.
     */
    public InvalidTableException() {
        super();
    }

    /**
     * Constructs an <code>InvalidTableException</code> with the specified detail message.
     * @param message detail message
     */
    public InvalidTableException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>InvalidTableException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public InvalidTableException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an <code>InvalidTableException</code> with the cause.
     * @param cause the cause
     */
    public InvalidTableException(Throwable cause) {
        super(cause);
    }

}