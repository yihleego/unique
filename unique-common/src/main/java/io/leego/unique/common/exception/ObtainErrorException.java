package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class ObtainErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs an <code>ObtainErrorException</code> with no detail message.
     */
    public ObtainErrorException() {
        super();
    }

    /**
     * Constructs an <code>ObtainErrorException</code> with the specified detail message.
     * @param message detail message
     */
    public ObtainErrorException(String message) {
        super(message);
    }

    /**
     * Constructs an <code>ObtainErrorException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public ObtainErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs an <code>ObtainErrorException</code> with the cause.
     * @param cause the cause
     */
    public ObtainErrorException(Throwable cause) {
        super(cause);
    }

}
