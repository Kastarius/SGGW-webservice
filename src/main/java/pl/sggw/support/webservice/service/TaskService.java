package pl.sggw.support.webservice.service;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.TaskDAO;
import pl.sggw.support.webservice.dto.BasicUserData;
import pl.sggw.support.webservice.dto.Comment;
import pl.sggw.support.webservice.dto.Task;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.TaskModel;
import pl.sggw.support.webservice.populator.TaskPopulator;

import javax.xml.ws.ServiceMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("taskService")
public class TaskService {

    @Autowired
    TaskPopulator taskPopulator;

    @Autowired
    UserService userService;

    @Autowired
    TaskDAO taskDAO;

    public List<Task> getAll() {
        return taskDAO.getAllTasks().stream().map(this::convert).collect(Collectors.toList());
    }

    private Task convert(TaskModel taskModel) {
        Task task = new Task();
        if(Objects.nonNull(taskModel))taskPopulator.populate(taskModel, task);
        return task;
    }

    public Task save(Task task) {
        TaskModel taskModel = new TaskModel();
        taskPopulator.reversePopulate(task, taskModel);
        taskDAO.save(taskModel);
        return task;
    }

    public Task saveOrUpdate(Task task) {
        TaskModel taskModel = new TaskModel();
        taskPopulator.reversePopulate(task, taskModel);
        save(task);
        taskPopulator.populate(taskModel, task);
        return task;
    }

    public Task getTaskById(String taskId) {
        return convert(taskDAO.getTaskById(Long.valueOf(taskId)));
    }

    public void remove(long id) {
        taskDAO.remove(taskDAO.getTaskById(id));
    }

    public Comment addComment(Comment comment, String taskId) {
        Task taskById = getTaskById(taskId);
        List<Comment> comments = taskById.getComments();
        comments.add(comment);
        save(taskById);
        return comment;
    }

    public Task assignUser(String taskId, String userId) {
        Task task = getTaskById(taskId);
        User user = userService.getUserById(userId);
        BasicUserData basicUserData = new BasicUserData();
        basicUserData.setFirstName(user.getFirstName());
        basicUserData.setLastName(user.getLastName());
        basicUserData.setId(user.getId());
        task.setUserData(basicUserData);
        saveOrUpdate(task);
        return task;
    }

    public List<Comment> getTaskComments(String taskId) {
        return getTaskById(taskId).getComments();
    }
}
