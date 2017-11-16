package pl.sggw.support.webservice.service;

import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.TaskDAO;
import pl.sggw.support.webservice.dto.Task;
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
}
