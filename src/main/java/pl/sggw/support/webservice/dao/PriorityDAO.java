package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.model.PriorityModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class PriorityDAO extends GenericDAO<PriorityModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public List<PriorityModel> getAllPriorities() {
        return createQuery().executeWithResultList();
    }

    @Override
    public void save(PriorityModel entity) {
        if(Objects.isNull(getPriorityByID(entity.getId())))
            entity.setId(0);
        super.save(entity);
    }

    public PriorityModel getPriorityByID(long id) {
        QueryBuilder qb = createQuery();
        CriteriaBuilder builder = qb.getBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),id)).executeWithSingleResult();
    }

    public void remove(PriorityModel priorityModel) {
        super.remove(priorityModel);
    }
}
