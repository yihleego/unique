package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class KeyNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>KeyNotFoundException</code> with no detail message.
     */
    public KeyNotFoundException() {
        super();
    }

    /**
     * Constructs a <code>KeyNotFoundException</code> with the specified detail message.
     * @param message detail message
     */
    public KeyNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>KeyNotFoundException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public KeyNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>KeyNotFoundException</code> with the cause.
     * @param cause the cause
     */
    public KeyNotFoundException(Throwable cause) {
        super(cause);
    }

}
