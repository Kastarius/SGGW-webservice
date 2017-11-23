package pl.sggw.support.webservice.security.util;

/**
 * Created by Kamil on 2017-11-19.
 */
public enum TokenValidationResult {

    VALID("0","Success"),
    EXPIRED("111","Token expired"),
    MALFORMED("112","Token is broken"),
    INCORRECT("113","Token is incorrect"),
    NOT_FOUND("114","Token not found");

    private String code;
    private String message;

    TokenValidationResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
