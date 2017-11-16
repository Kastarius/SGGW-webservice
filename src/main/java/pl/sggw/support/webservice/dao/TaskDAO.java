package pl.sggw.support.webservice.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.sggw.support.webservice.dao.query.QueryBuilder;
import pl.sggw.support.webservice.model.TaskModel;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Repository
@Transactional
public class TaskDAO extends GenericDAO<TaskModel> {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private PriorityDAO priorityDAO;

    @Autowired
    private UserDAO userDAO;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public TaskModel getTaskById(long taskId) {
        QueryBuilder<TaskModel> qb = createQuery();
        CriteriaBuilder builder = qb.getCriteriaBuilder();
        return qb.where(builder.equal(qb.getColumn("id"), taskId)).executeWithSingleResult();
    }

    public List<TaskModel> getAllTasks() {
        return createQuery().executeWithResultList();
    }

    @Override
    public void save(TaskModel entity) {
        if (Objects.isNull(getTaskById(entity.getId())))
            entity.setId(0);
        super.save(entity);
    }

    @Override
    public void remove(TaskModel taskModel) {
        super.remove(taskModel);
    }

}
