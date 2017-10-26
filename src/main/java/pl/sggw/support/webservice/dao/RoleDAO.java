package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.model.RoleModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

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
        QueryBuilder qb = createQuery();
        CriteriaBuilder builder = qb.getBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),roleId)).executeWithSingleResult();
    }

}
