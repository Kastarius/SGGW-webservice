package pl.sggw.support.webservice.security.exception;

import pl.sggw.support.webservice.security.util.UserValidationResult;

/**
 * Created by Kamil on 2017-10-21.
 */
public class LoginFailureException extends Exception {

    private final UserValidationResult validationResult;

    public LoginFailureException(UserValidationResult validationResult) {
        this.validationResult = validationResult;
    }

    public UserValidationResult getValidationResult() {
        return validationResult;
    }
}
