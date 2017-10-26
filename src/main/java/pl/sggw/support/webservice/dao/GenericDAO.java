package pl.sggw.support.webservice.dao;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sggw.support.webservice.model.ItemModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Kamil on 2017-06-15.
 */
public abstract class GenericDAO<T extends ItemModel> {

    private static final Logger LOG = LoggerFactory.getLogger(GenericDAO.class);
    private final Class<T> modelClass;


    public GenericDAO() {
        this.modelClass = resolveModelClass();
        info("Initialization DAO for "+ modelClass.getSimpleName());
    }

    protected abstract EntityManager getEntityManager();

    @SuppressWarnings("unchecked")
    private Class<T> resolveModelClass() {
        Type t = this.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    public void save(T entity){
        T tempobject;
        try{
            if(entity.getId() == 0) {
                info("Saving entity : " + entity);
                getEntityManager().persist(entity);
            } else {
                //TODO
                info("Updating entity : " + entity);
                getEntityManager().merge(entity);
                getEntityManager().flush();
            }
        }catch(Exception e){
            error(e.getMessage());
        }
    }

    public void saveAll(List<T> entities){
        try{
            for(T entity : entities){
                this.save(entity);
            }
        }catch(Exception e){
            error(e.getMessage());
        }
    }


    public void remove(T entity){
        T tempobject;
        try{
            info("Removing entity : " + entity);
            tempobject = getEntityManager().find(modelClass, entity.getId());
            getEntityManager().remove(tempobject);
        }catch(Exception e){
            error(e.getMessage());
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
            error(e.getMessage());
        }
    }

    public QueryBuilder createQuery(){
        return new QueryBuilder(getEntityManager());
    }

    public List<T> findList(TypedQuery<T> query){
        try{
            return query.getResultList();
        }catch(Exception e){
            error(e.getMessage());
            return null;
        }
    }

    public T findSingle(TypedQuery<T> query){
        try{
            return query.getSingleResult();
        }catch(Exception e){
            error(e.getMessage());
            return null;
        }
    }

    void info(String message){
        LOG.info(message);
    }

    void error(String message){
        LOG.error(message);
    }



    class QueryBuilder {
        private EntityManager entityManager;
        private CriteriaBuilder builder;
        private CriteriaQuery<T> criteriaQuery;
        private Root<T> tableObject;
        private Path<String> column;
        private Predicate[] predicates;

        QueryBuilder(EntityManager entityManager) {
            this.entityManager = entityManager;
            this.builder = this.entityManager.getCriteriaBuilder();
            this.criteriaQuery = this.builder.createQuery(modelClass);
            this.tableObject = criteriaQuery.from(modelClass);
        }

        QueryBuilder where(Predicate... predicates){
            if(Objects.isNull(this.predicates)) {
                this.predicates = predicates;
            } else {
                this.predicates = (Predicate[]) ArrayUtils.addAll(this.predicates, predicates);
            }
            return this;
        }

        CriteriaBuilder getBuilder() {
            return this.builder;
        }

        Path<String> getColumn(String columnName) {
            this.column = tableObject.get(columnName);
            return this.column;
        }

        private TypedQuery<T> execute() {
            CriteriaQuery<T> select = this.criteriaQuery.select(this.tableObject);
            CriteriaQuery<T> query = Objects.nonNull(this.predicates)?select.where(this.predicates):select;
            return getEntityManager().createQuery(query);
        }
        List<T> executeWithResultList(){
            try {
                return execute().getResultList();
            } catch (NoResultException e){
                return new ArrayList<>();
            }
        }


        T executeWithSingleResult() {
            try {
            return execute().getSingleResult();
            } catch (NoResultException e){
                return null;
            }
        }


    }

}
