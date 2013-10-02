package nu.wasis.stunden.plugins.textfile.exception;

public class InvalidEntryException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidEntryException() {
    }

    public InvalidEntryException(final String message) {
        super(message);
    }

    public InvalidEntryException(final Throwable cause) {
        super(cause);
    }

    public InvalidEntryException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidEntryException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
