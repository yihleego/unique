package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class ConversionException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>ConversionException</code> with no detail message.
     */
    public ConversionException() {
        super();
    }

    /**
     * Constructs a <code>ConversionException</code> with the specified detail message.
     * @param message detail message
     */
    public ConversionException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>ConversionException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public ConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>ConversionException</code> with the cause.
     * @param cause the cause
     */
    public ConversionException(Throwable cause) {
        super(cause);
    }

}