package pl.sggw.support.webservice.dao.validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sggw.support.webservice.dao.GenericDAO;
import pl.sggw.support.webservice.dao.exception.ConstraintViolationException;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.dao.validator.violation.ConstraintViolation;
import pl.sggw.support.webservice.dao.validator.violation.Violation;
import pl.sggw.support.webservice.model.ItemModel;

import javax.persistence.Column;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Kamil on 2017-11-06.
 */
public class ConstraintValidator<T extends ItemModel> implements Validator<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ConstraintValidator.class);

    private final GenericDAO<T> dao;
    private final Class<T> modelClass;
    private final Map<Field, Column> modelFields;

    public ConstraintValidator(GenericDAO<T> dao) {
        this.dao = dao;
        this.modelClass = dao.getModelClass();
        this.modelFields = resolveModelFieldsWithConstraints();
    }

    private Map<Field, Column> resolveModelFieldsWithConstraints() {
        return Arrays.stream(this.modelClass.getDeclaredFields())
                .map(field -> new AbstractMap.SimpleEntry<>(field,field.getAnnotation(Column.class)))
                .filter(i -> Objects.nonNull(i.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public Set<Violation<T>> validate(T entity) throws ConstraintViolationException {
        return this.modelFields.entrySet().stream().filter(item -> Objects.nonNull(item.getValue())).map(item -> {
            Set<Violation<T>> fieldViolations = new HashSet<>();
            Field field = item.getKey();
            Column column = item.getValue();
            Object fieldValue = tryGetFieldValue(entity, field);

            validateUnique(entity, fieldViolations, field, column, fieldValue);

            validateNotNull(entity, fieldViolations, field, column, fieldValue);

            validateLength(entity, fieldViolations, field, column, fieldValue);

            return fieldViolations;
        }).filter(Objects::nonNull).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Object tryGetFieldValue(T entity, Field field) {
        field.setAccessible(true);
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            LOG.error(e.getMessage());
        }
        return null;
    }

    private void validateLength(T entity, Set<Violation<T>> fieldViolations, Field field, Column column, Object fieldValue) {
        if(column.length() < String.valueOf(fieldValue).length()){
            fieldViolations.add(createViolation(entity, field, fieldValue,String.format("The value exceeds the allowed length of %s characters",column.length())));
        }
    }

    private void validateNotNull(T entity, Set<Violation<T>> fieldViolations, Field field, Column column, Object fieldValue) {
        if (!column.nullable() && Objects.isNull(fieldValue)) {
            fieldViolations.add(createViolation(entity, field, null,"Value cannot be null"));
        }
    }

    private void validateUnique(T entity, Set<Violation<T>> fieldViolations, Field field, Column column, Object fieldValue) {
        if (column.unique()) {
            List<T> entitiesByAttribute = getEntitiesByAttribute(field.getName(), fieldValue);
            if (!(entitiesByAttribute.isEmpty() || entitiesByAttribute.stream().anyMatch(i -> i.getId().equals(entity.getId())))) {
                fieldViolations.add(createViolation(entity, field, fieldValue,String.format("Value %s is not unique",fieldValue)));
            }
        }
    }

    private ConstraintViolation<T> createViolation(T entity, Field field, Object fieldValue, String massage) {
        return new ConstraintViolation<>(massage,fieldValue,field,entity,this);
    }

    private List<T> getEntitiesByAttribute(String filedName, Object fieldValue){
        QueryBuilder<T> qb = this.dao.createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn(qb.getMetamodelByFiledName(filedName)),fieldValue)).executeWithResultList();
    }
}
