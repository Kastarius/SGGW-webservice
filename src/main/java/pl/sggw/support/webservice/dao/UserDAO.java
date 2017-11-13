package pl.sggw.support.webservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.exception.DatabaseOperationException;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.*;
import pl.sggw.support.webservice.model.RoleModel_;
import pl.sggw.support.webservice.model.UserModel_;
import pl.sggw.support.webservice.security.SecurityHelper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.*;
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
        QueryBuilder<UserModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.and(builder.equal(qb.getColumn(UserModel_.login),login),
                builder.equal(qb.getColumn(UserModel_.password),securityHelper.encodePassword(password))))
                .executeWithSingleResult();
    }

    public UserModel getUserByLogin(String login){
        QueryBuilder<UserModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn(UserModel_.login),login)).executeWithSingleResult();
    }

    public UserModel getUserByID(long userId){
        QueryBuilder<UserModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn(UserModel_.id),userId)).executeWithSingleResult();
    }

    @Override
    public void save(UserModel entity) {
        Set<RoleModel> models = null;
        if(Objects.nonNull(entity.getPermissions()))models = entity.getPermissions().stream().map(role -> {
            RoleModel roleModel = roleDAO.getRoleById(role.getId());
            if(Objects.nonNull(roleModel)){
                role = roleModel;
            } else {
                throw new DatabaseOperationException(String.format("Cannot find role with id %s",role.getId()));
            }
            return role;
        }).filter(Objects::nonNull).collect(Collectors.toSet());
        entity.setPermissions(models);
        UserModel userByID = getUserByID(entity.getId());
        if(Objects.isNull(userByID)){
            entity.setId(0);
            encodeUserPassword(entity);
        } else {
            encodeUserPasswordIfNeeded(entity, userByID);
        }
        super.save(entity);
    }

    private void encodeUserPasswordIfNeeded(UserModel entity, UserModel userByID) {
        if(!userByID.getPassword().equals(entity.getPassword())){
            encodeUserPassword(entity);
        }
    }

    private void encodeUserPassword(UserModel entity) {
        if(Objects.nonNull(entity.getPassword()))
        entity.setPassword(securityHelper.encodePassword(entity.getPassword()));
    }

    @Override
    public void remove(UserModel entity) {
        entity.setPermissions(null); // removing relationship many to many
        super.remove(entity);
    }


    /**
     * Example query with join and orderBy
     */
    public List<UserModel> getAllUserWhereRole(String roleKod){
        QueryBuilder<UserModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.distinct().join(UserModel_.permissions)
                .where(builder.equal(qb.getJoinColumn(UserModel_.permissions,RoleModel_.code),roleKod))
                .orderBy(builder.desc(qb.getColumn(UserModel_.id)))
                .executeWithResultList();
    }

//    /**
//     * Truncate example
//     */
//    public void truncate(){
//        getEntityManager().createQuery("DELETE FROM User").executeUpdate();
////            createQuery("DELETE FROM Country c WHERE c.population < :p");
////            int deletedCount = query.setParameter(p, 100000).executeUpdate();
//    }
}
