package ac.uk.kn253.graphbrowser.exceptions;

public class PagerRunException extends RuntimeException {

    public PagerRunException(final Exception e) {
        super(e);
    }

    public PagerRunException(final String message) {
        super(message);
    }

    public PagerRunException(final String message, final Exception e) {
        super(message, e);
    }

}
