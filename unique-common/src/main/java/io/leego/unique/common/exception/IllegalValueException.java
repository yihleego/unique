package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class IllegalValueException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>IllegalValueException</code> with no detail message.
     */
    public IllegalValueException() {
        super();
    }

    /**
     * Constructs an <code>IllegalValueException</code> with the specified detail message.
     * @param message detail message
     */
    public IllegalValueException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>IllegalValueException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public IllegalValueException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an <code>IllegalValueException</code> with the cause.
     * @param cause the cause
     */
    public IllegalValueException(Throwable cause) {
        super(cause);
    }

}