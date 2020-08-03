package io.leego.unique.common.exception;

/**
 * @author Yihleego
 */
public class SequenceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a <code>SequenceNotFoundException</code> with no detail message.
     */
    public SequenceNotFoundException() {
        super();
    }

    /**
     * Constructs a <code>SequenceNotFoundException</code> with the specified detail message.
     * @param message detail message
     */
    public SequenceNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs a <code>SequenceNotFoundException</code> with the specified detail message and cause.
     * @param message detail message
     * @param cause   the cause
     */
    public SequenceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a <code>SequenceNotFoundException</code> with the cause.
     * @param cause the cause
     */
    public SequenceNotFoundException(Throwable cause) {
        super(cause);
    }

}
