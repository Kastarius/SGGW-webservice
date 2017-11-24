package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.CommentDAO;
import pl.sggw.support.webservice.dao.TaskDAO;
import pl.sggw.support.webservice.model.CommentModel;
import pl.sggw.support.webservice.model.TaskModel;
import pl.sggw.support.webservice.populator.CommentPopulator;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentPopulator populator;

    @Autowired
    private TaskDAO taskDAO;

    @Autowired
    private CommentDAO commentDAO;

    @Transactional
    public void remove(long id) {
        TaskModel task = commentDAO.getCommentById(id).getTask();
        List<CommentModel> comments = task.getComments();
        CommentModel commentToRemove = getCommentById(comments, id);
        task.removeComment(commentToRemove);
    }

    private CommentModel getCommentById(List<CommentModel> models, long id) {
        for (CommentModel model : models) {
            if (model.getId() == id) {
                return model;
            }
        }
        return null;
    }

    @Transactional
    public void edit(long id, String content) {
        TaskModel task = commentDAO.getCommentById(id).getTask();
        List<CommentModel> comments = task.getComments();
        CommentModel commentToEdit = getCommentById(comments, id);
        commentToEdit.setContent(content);
    }
}
