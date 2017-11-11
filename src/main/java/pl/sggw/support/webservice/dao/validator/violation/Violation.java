package pl.sggw.support.webservice.dao.validator.violation;

import pl.sggw.support.webservice.dao.validator.Validator;
import pl.sggw.support.webservice.model.ItemModel;

import java.lang.reflect.Field;

/**
 * Created by Kamil on 2017-11-11.
 */
public interface Violation<T extends ItemModel> {

    String getMessage();

    Object getInvalidValue();

    String getFieldName();

    Field getField();

    T getRootClass();

    Validator<T> getValidator();
}
