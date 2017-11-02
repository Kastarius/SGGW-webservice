package pl.sggw.support.webservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.model.RoleModel;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.SecurityHelper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Kamil on 2017-10-21.
 */
@Repository
@Transactional
public class UserDAO extends GenericDAO<UserModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private RoleDAO roleDAO;

    @Autowired
    private SecurityHelper securityHelper;

    @Override
    protected EntityManager getEntityManager() {
        return this.entityManager;
    }

    public List<UserModel> getAllUsers(){
        return createQuery().executeWithResultList();
    }

    public UserModel getUserByCredentials(String login, String password){
        QueryBuilder qb = createQuery();
        CriteriaBuilder builder = qb.getBuilder();
        return qb.where(builder.equal(qb.getColumn("login"),login),
                builder.and(builder.equal(qb.getColumn("password"),securityHelper.encodePassword(password))))
                .executeWithSingleResult();
    }

    public UserModel getUserByLogin(String login){
        QueryBuilder qb = createQuery();
        CriteriaBuilder builder = qb.getBuilder();
        return qb.where(builder.equal(qb.getColumn("login"),login)).executeWithSingleResult();
    }

    public UserModel getUserByID(long userId){
        QueryBuilder qb = createQuery();
        CriteriaBuilder builder = qb.getBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),userId)).executeWithSingleResult();
    }

    @Override
    public void save(UserModel entity) {
        Set<RoleModel> models = null;
        if(Objects.nonNull(entity.getPermissions()))models = entity.getPermissions().stream().map(role -> {
            RoleModel roleModel = roleDAO.getRoleById(role.getId());
            if(Objects.nonNull(roleModel)){
                role = roleModel;
            } else {
                roleDAO.save(role);
            }
            return role;
        }).collect(Collectors.toSet());
        entity.setPermissions(models);
        if(Objects.isNull(getUserByID(entity.getId()))){
            entity.setId(0);
            encodeUserPassword(entity);
        }
        super.save(entity);
    }

    private void encodeUserPassword(UserModel entity) {
        entity.setPassword(securityHelper.encodePassword(entity.getPassword()));
    }

    @Override
    public void remove(UserModel entity) {
        entity.setPermissions(null); // removing relationship many to many
        super.remove(entity);
    }
}
