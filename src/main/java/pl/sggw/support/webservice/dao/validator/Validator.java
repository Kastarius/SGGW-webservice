package pl.sggw.support.webservice.dao.validator;

import pl.sggw.support.webservice.dao.validator.violation.Violation;
import pl.sggw.support.webservice.model.ItemModel;

import java.util.Set;

/**
 * Created by Kamil on 2017-11-11.
 */
public interface Validator<T extends ItemModel> {
    Set<Violation<T>> validate(T entity);
}
