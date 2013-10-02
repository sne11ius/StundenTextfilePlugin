package nu.wasis.stunden.plugins.textfile.exception;

public class InvalidFilenameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidFilenameException() {
    }

    public InvalidFilenameException(final String message) {
        super(message);
    }

    public InvalidFilenameException(final Throwable cause) {
        super(cause);
    }

    public InvalidFilenameException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidFilenameException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
