package pl.sggw.support.webservice.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sggw.support.webservice.dao.exception.ConstraintViolationException;
import pl.sggw.support.webservice.dao.exception.DatabaseOperationException;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.dao.validator.ConstraintValidator;
import pl.sggw.support.webservice.dao.validator.Validator;
import pl.sggw.support.webservice.dao.validator.violation.Violation;
import pl.sggw.support.webservice.model.ItemModel;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by Kamil on 2017-06-15.
 */
public abstract class GenericDAO<T extends ItemModel> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDAO.class);
    private final Class<T> modelClass;
    private List<Validator<T>> validators = new ArrayList<>();


    public GenericDAO() {
        this.modelClass = resolveModelClass();
        this.validators.add(new ConstraintValidator<>(this));
        info("Initialization DAO for "+ modelClass.getSimpleName());
    }

    protected abstract EntityManager getEntityManager();

    public List<Validator<T>> getValidators() {return this.validators;}

    @SuppressWarnings("unchecked")
    private Class<T> resolveModelClass() {
        Type t = this.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    public Class<T> getModelClass(){
        return this.modelClass;
    }

    public void save(T entity){
        try {
            validate(entity);
            if(Objects.isNull(getEntityManager().find(modelClass, entity.getId()))) {
                info("Saving entity : " + entity);
                getEntityManager().persist(entity);
            } else {
                info("Updating entity : " + entity);
                getEntityManager().merge(entity);
            }
            getEntityManager().flush();
        } catch(ConstraintViolationException cve) {
            error(cve.getMessage(),cve);
            throw cve;
        } catch(Exception e){
            error(e.getMessage(),e);
            throw new DatabaseOperationException(e);
        }
    }

    public void saveAll(List<T> entities){
        for(T entity : entities){
            this.save(entity);
        }
    }


    public void remove(T entity){
        T tempobject;
        try{
            info("Removing entity : " + entity);
            tempobject = getEntityManager().find(modelClass, entity.getId());
            getEntityManager().remove(tempobject);
        }catch(Exception e){
            error(e.getMessage(),e);
            throw new DatabaseOperationException(e);
        }
    }

    public void removeAll(List<T> entities){
        T tempobject;
        try{
            for(T entity : entities) {
                info("Removing entity : " + entity);
                tempobject = getEntityManager().find(modelClass, entity.getId());
                getEntityManager().remove(tempobject);
            }
        }catch(Exception e){
            error(e.getMessage(),e);
            throw new DatabaseOperationException(e);
        }
    }


    public QueryBuilder<T> createQuery(){
        return QueryBuilder.createQuery(getEntityManager(),this.modelClass);
    }

    protected void info(String message){
        LOG.info(message);
    }

    protected void error(String message, Exception ex){
        LOG.error(message,ex);
    }

    private void validate(T entity) {
        Set<Violation<T>> validationResult = new HashSet<>();
        for (Validator<T> validator: validators) {
            validationResult.addAll(validator.validate(entity));
        }
        if(!validationResult.isEmpty())
            throw new ConstraintViolationException(String.format("Entity [%s] violates constraint",entity.toString()),validationResult);
    }
}
