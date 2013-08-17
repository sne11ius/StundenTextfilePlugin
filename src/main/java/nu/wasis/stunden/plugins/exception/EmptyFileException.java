package nu.wasis.stunden.plugins.exception;

public class EmptyFileException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyFileException() {
    }

    public EmptyFileException(final String message) {
        super(message);
    }

    public EmptyFileException(final Throwable cause) {
        super(cause);
    }

    public EmptyFileException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public EmptyFileException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
