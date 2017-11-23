package pl.sggw.support.webservice.security.exception;

import pl.sggw.support.webservice.security.util.TokenValidationResult;

/**
 * Created by Kamil on 2017-10-23.
 */
public class TokenValidationFailureException extends Exception{

    private final TokenValidationResult tokenValidationResult;

    public TokenValidationFailureException(Throwable cause, TokenValidationResult tokenValidationResult) {
        super(cause);
        this.tokenValidationResult = tokenValidationResult;
    }

    public TokenValidationFailureException(TokenValidationResult tokenValidationResult) {
        this.tokenValidationResult = tokenValidationResult;
    }

    public TokenValidationResult getTokenValidationResult() {
        return tokenValidationResult;
    }
}
