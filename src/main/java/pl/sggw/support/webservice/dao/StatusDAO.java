package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.StatusModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class StatusDAO extends GenericDAO<StatusModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<StatusModel> getAllStatuses()  {
        return createQuery().executeWithResultList();
    }

    public void remove(StatusModel statusModel) {
        super.remove(statusModel);
    }

    public void save(StatusModel statusModel) {
        if(Objects.isNull(getStatusById(statusModel.getId())))
            statusModel.setId(0);
        super.save(statusModel);
    }

    public StatusModel getStatusById(long id) {
        QueryBuilder<StatusModel> queryBuilder = createQuery();
        CriteriaBuilder criteriaBuilder = queryBuilder.getCriteriaBuilder();
        return queryBuilder.where(criteriaBuilder.equal(queryBuilder.getColumn("id"), id)).executeWithSingleResult();
    }
}
