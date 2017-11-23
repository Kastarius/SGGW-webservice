package pl.sggw.support.webservice.dao;

import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.CommentModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;

@Repository
@Transactional
public class CommentDAO extends GenericDAO<CommentModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public CommentModel getCommentById(long id) {
        QueryBuilder<CommentModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn("id"),id)).executeWithSingleResult();
    }

    public void remove(CommentModel commentModel) {
        super.remove(commentModel);
    }
}
