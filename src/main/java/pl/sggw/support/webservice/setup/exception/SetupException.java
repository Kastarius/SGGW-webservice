package pl.sggw.support.webservice.setup.exception;

/**
 * Created by Kamil on 2017-11-02.
 */
public class SetupException extends Exception {

    public SetupException(String message) {
        super(message);
    }

    public SetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetupException(Throwable cause) {
        super(cause);
    }
}
