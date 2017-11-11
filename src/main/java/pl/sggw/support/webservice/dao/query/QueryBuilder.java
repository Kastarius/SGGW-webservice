package pl.sggw.support.webservice.dao.query;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.util.CollectionUtils;
import pl.sggw.support.webservice.model.ItemModel;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.*;
import java.util.*;

/**
 * Created by Kamil on 2017-11-06.
 *
 *
 * Builder for query
 * Based on CriteriaAPI
 */
public class QueryBuilder<T extends ItemModel> {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;
    private final CriteriaQuery<T> criteriaQuery;
    private final Class<T> modelClass;
    private final ManagedType<T> managedType;
    private Root<T> rootObject;
    private Predicate[] predicates;
    private Map<String, Join<? extends ItemModel, ? extends ItemModel>> joins;
    private List<Order> orders;
    private boolean distinct = false;


    private QueryBuilder(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.modelClass = entityClass;
        this.criteriaBuilder = this.entityManager.getCriteriaBuilder();
        this.managedType = this.resolveManagedType();
        this.criteriaQuery = this.criteriaBuilder.createQuery(this.modelClass);
        this.rootObject = criteriaQuery.from(this.modelClass);
    }

    /**
     * Builder for query
     * Based on CriteriaAPI
     *
     * new QueryBuilder(entityClass,entityManager) is equal to SELECT * FROM entityClass_table
     * @param entityClass entity class, determines root object (table of entity) and return type
     * @param entityManager entityManager
     */
    public static <T extends ItemModel> QueryBuilder<T> createQuery(EntityManager entityManager, Class<T> entityClass) {
        return new QueryBuilder<>(entityManager,entityClass);
    }

    private ManagedType<T> resolveManagedType(){
        Metamodel metamodel = this.entityManager.getMetamodel();
        if(Objects.nonNull(metamodel)){
            return metamodel.managedType(this.modelClass);
        }
        return null;
    }

    public SingularAttribute<T, ?> getMetamodelByFiledName(String filedName){
        if(Objects.isNull(this.managedType)) throw new IllegalArgumentException(String.format("Cannot resolve metamodel for class [%s]",this.modelClass.getCanonicalName()));
        return this.managedType.getDeclaredSingularAttribute(filedName);
    }

    public QueryBuilder<T> distinct() {
        this.distinct = true;
        return this;
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(columnName, JoinType.INNER);
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.rootObject.join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SetAttribute<T,? extends ItemModel> columnName) {
        return join(columnName, JoinType.INNER);
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SetAttribute<T,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.rootObject.join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(ListAttribute<T,? extends ItemModel> columnName) {
        return join(columnName, JoinType.INNER);
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(ListAttribute<T,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.rootObject.join(columnName,joinType));
        return this;
    }


    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(CollectionAttribute<T,? extends ItemModel> columnName) {
        return join(columnName, JoinType.INNER);
    }

    /**
     * Representation of join from root object
     *
     * @param columnName join column, on root object
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(CollectionAttribute<T,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.rootObject.join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(CollectionAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(CollectionAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, SingularAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SingularAttribute<ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(CollectionAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(CollectionAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(SetAttribute<? extends ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, CollectionAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, SetAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @return QueryBuilder PluralAttribute
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName) {
        return join(joinName, columnName, JoinType.INNER);
    }

    /**
     * Representation of join from joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName join column, on joined entity
     * @param joinType JoinType
     * @return QueryBuilder
     */
    public QueryBuilder<T> join(ListAttribute<? extends ItemModel,? extends ItemModel> joinName, ListAttribute<ItemModel,? extends ItemModel> columnName, JoinType joinType) {
        if (Objects.isNull(this.joins)) {
            this.joins = new HashMap<>();
        }
        this.joins.put(columnName.getName(), this.joins.get(joinName.getName()).join(columnName,joinType));
        return this;
    }



    /**
     * Clause where
     *
     * @param predicates Predicate[] predicates
     * @return QueryBuilder
     */
    public QueryBuilder<T> where(Predicate... predicates) {
        if (Objects.isNull(this.predicates)) {
            this.predicates = predicates;
        } else {
            this.predicates = (Predicate[]) ArrayUtils.addAll(this.predicates, predicates);
        }
        return this;
    }

    /**
     * Clause orderBy
     *
     * @param orders Order[] orders
     * @return QueryBuilder
     */
    public QueryBuilder<T> orderBy(Order... orders) {
        if (Objects.isNull(this.orders)) {
            this.orders = Arrays.asList(orders);
        } else {
            this.orders.addAll(Arrays.asList(orders));
        }
        return this;
    }

    /**
     * Return CriteriaBuilder
     *
     * @return CriteriaBuilder
     */
    public CriteriaBuilder getCriteriaBuilder() {
        return this.criteriaBuilder;
    }

    /**
     * Return Path for column name of root object
     *
     * @param columnName column name of root object
     * @return Path
     */
    @Deprecated
    public Path<String> getColumn(String columnName) {
        return rootObject.get(columnName);
    }

    /**
     * Return Path for column name of root object
     *
     * @param columnName column name of root object
     * @return Path
     */
    public <X> Path<X> getColumn(SingularAttribute<T,X> columnName) {
        return rootObject.get(columnName);
    }

    /**
     * Return Path for column name of root object
     *
     * @param columnName column name of root object
     * @return Expression
     */
    public <X extends ItemModel, Y extends Collection<X>> Expression<Y> getColumn(PluralAttribute<T, Y, X> columnName) {
        return rootObject.get(columnName);
    }

    /**
     * Return Path for column name of joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName column name of joined entity
     * @return Path
     */
    public <X> Path<X> getJoinColumn(SingularAttribute<?,?>  joinName, SingularAttribute<ItemModel,X> columnName) {
        return this.joins.get(joinName.getName()).get(columnName);
    }

    /**
     * Return Path for column name of joined entity
     *
     * @param joinName name of join entity, named like join column
     * @param columnName column name of joined entity
     * @return Path
     */
    @SuppressWarnings("unchecked")
    public <X,Y extends ItemModel> Path<X> getJoinColumn(PluralAttribute<?,?,Y>  joinName, SingularAttribute<Y,X> columnName) {
        return this.joins.get(joinName.getName()).get((SingularAttribute<ItemModel, X>)columnName);
    }

    /**
     * Query build method
     *
     * @return TypedQuery
     */
    private TypedQuery<T> build() {
        CriteriaQuery<T> query = this.criteriaQuery.select(this.rootObject).distinct(this.distinct);
        query = Objects.nonNull(this.predicates) ? query.where(this.predicates) : query;
        query = CollectionUtils.isEmpty(this.orders) ? query : query.orderBy(this.orders);
        return this.entityManager.createQuery(query);
    }

    /**
     * Performs a query
     *
     * @return List of result
     */
    public List<T> executeWithResultList() {
        try {
            return build().getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Performs a query, expected a single result of query
     *
     * @return Single result
     */
    public T executeWithSingleResult() {
        try {
            return build().getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

}
