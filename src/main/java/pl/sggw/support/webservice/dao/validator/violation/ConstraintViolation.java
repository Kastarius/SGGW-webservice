package pl.sggw.support.webservice.dao.validator.violation;

import pl.sggw.support.webservice.dao.validator.Validator;
import pl.sggw.support.webservice.model.ItemModel;

import java.lang.reflect.Field;

/**
 * Created by Kamil on 2017-11-11.
 */
public class ConstraintViolation<T extends ItemModel> implements Violation<T> {

    private final String message;
    private final Object invalidValue;
    private final Field field;
    private final T root;
    private final Validator<T> validator;

    public ConstraintViolation(String message, Object invalidValue, Field field, T root, Validator<T> validator) {
        this.message = message;
        this.invalidValue = invalidValue;
        this.field = field;
        this.root = root;
        this.validator = validator;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Object getInvalidValue() {
        return invalidValue;
    }

    @Override
    public String getFieldName() {
        return field.getName();
    }

    @Override
    public Field getField() {
        return field;
    }

    @Override
    public T getRootClass() {
        return root;
    }

    @Override
    public Validator<T> getValidator() {
        return validator;
    }
}
