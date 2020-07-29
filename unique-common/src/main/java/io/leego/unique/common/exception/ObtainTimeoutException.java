package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class ObtainTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>ObtainTimeoutException</code> with no detail message.
     */
    public ObtainTimeoutException() {
        super();
    }

    /**
     * Constructs an <code>ObtainTimeoutException</code> with the specified detail message.
     * @param message detail message
     */
    public ObtainTimeoutException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>ObtainTimeoutException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public ObtainTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an <code>ObtainTimeoutException</code> with the cause.
     * @param cause the cause
     */
    public ObtainTimeoutException(Throwable cause) {
        super(cause);
    }

}
