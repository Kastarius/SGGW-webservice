package pl.sggw.support.webservice.security.exception;

import pl.sggw.support.webservice.security.util.UserValidationResult;

/**
 * Created by Kamil on 2017-11-19.
 */
public class UserValidationFailureException extends LoginFailureException {

    public UserValidationFailureException(UserValidationResult validationResult) {
        super(validationResult);
    }
}
