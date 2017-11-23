package pl.sggw.support.webservice.security.util;

/**
 * Created by Kamil on 2017-11-19.
 */
public enum UserValidationResult {
    VALID("0","Success"),
    LOGIN_REQUIRED("101","Re-authentication is required"),
    DISABLED_ACCOUNT("102","Account is disabled"),
    BAD_CREDENTIALS("103","Incorrect credentials");

    private String code;
    private String message;

    UserValidationResult(String code,String message) {
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
