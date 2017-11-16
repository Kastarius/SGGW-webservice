package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.Task;
import pl.sggw.support.webservice.model.TaskModel;
import pl.sggw.support.webservice.service.TaskService;

import javax.ws.rs.DELETE;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @ResponseBody
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Task> add(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.save(task), HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        if (Objects.isNull(task)) return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        if (Objects.isNull(task)) return new ResponseEntity<Task>(HttpStatus.NOT_FOUND);
        taskService.remove(Long.valueOf(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        Task task1 = taskService.saveOrUpdate(task);
        return new ResponseEntity<>(task1, HttpStatus.CREATED);
    }

}