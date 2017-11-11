package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.CategoryModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class CategoryDAO extends GenericDAO<CategoryModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<CategoryModel> getAll() {
        return createQuery().executeWithResultList();
    }

    @Override
    public void save(CategoryModel entity) {
        if(Objects.isNull(getCategoryByID(entity.getId())))
            entity.setId(0);
        super.save(entity);
    }

    public CategoryModel getCategoryByID(long id) {
        QueryBuilder<CategoryModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),id)).executeWithSingleResult();
    }

    public void remove(CategoryModel categoryModel) {
        super.remove(categoryModel);
    }

}
