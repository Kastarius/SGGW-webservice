package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.RoleModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.Objects;

/**
 * Created by Kamil on 2017-10-24.
 */
@Repository
@Transactional
public class RoleDAO extends GenericDAO<RoleModel>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }


    public RoleModel getRoleById(long roleId){
        QueryBuilder<RoleModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),roleId)).executeWithSingleResult();
    }

    public RoleModel getRoleByCode(String code){
        QueryBuilder<RoleModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn("code"),code)).executeWithSingleResult();
    }

    @Override
    public void save(RoleModel entity) {
        if(Objects.isNull(getRoleById(entity.getId())))entity.setId(0);
        super.save(entity);
    }
}
