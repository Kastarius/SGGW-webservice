package pl.sggw.support.webservice.controller.exception;

import org.springframework.validation.FieldError;

import java.util.List;

/**
 * Created by Kamil on 2017-11-11.
 */
public class InvalidDataException extends RuntimeException {
    private List<FieldError> errors;

    public InvalidDataException(List<FieldError> errors) {
        this.errors = errors;
    }

    public InvalidDataException(String message, List<FieldError> errors) {
        super(message);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }
}
