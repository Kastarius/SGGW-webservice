package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.*;
import pl.sggw.support.webservice.service.TaskService;

import pl.sggw.support.webservice.service.StatusService;
import pl.sggw.support.webservice.service.UserService;


import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    TaskService taskService;

    @Autowired
    StatusService statusService;

    @Autowired
    UserService userService;

    @ResponseBody
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAll();
    }

    @ResponseBody
    @PostMapping
    public ResponseEntity<Task> addComment(@RequestBody Task task) {
        return new ResponseEntity<>(taskService.save(task), HttpStatus.CREATED);
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ResponseEntity<Task> get(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        if (Objects.isNull(task)) return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    @ResponseBody
    public ResponseEntity delete(@PathVariable String id) {
        Task task = taskService.getTaskById(id);
        if (Objects.isNull(task)) return new ResponseEntity<Task>(HttpStatus.BAD_REQUEST);
        taskService.remove(Long.valueOf(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping
    @ResponseBody
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        Task task1 = taskService.saveOrUpdate(task);
        return new ResponseEntity<>(task1, HttpStatus.CREATED);
    }


    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.GET, path = "{id}/comment")
    @ResponseBody
    public List<Comment> getAllComments(@PathVariable String id) {
        return taskService.getTaskComments(id);
    }

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.POST, path = "{id}/comment")
    @ResponseBody
    public Comment addComment(@PathVariable(name = "id") String taskId, @RequestBody Comment comment) {
        return taskService.addComment(comment, taskId);
    }

    @PutMapping("/{id}/status")
    @ResponseBody
    public ResponseEntity changeStatus(@PathVariable String id, @RequestBody String statusId) {
        Task task = taskService.getTaskById(id);
        Status status = statusService.getStatus(statusId);
        if (Objects.isNull(status)) return new ResponseEntity<>(String.format("Cannot find status with id %s", id), HttpStatus.BAD_REQUEST);
        task.setStatus(status);
        taskService.saveOrUpdate(task);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }

    @Secured("ROLE_USER")
    @PutMapping("/{id}/user")
    @ResponseBody
    public ResponseEntity assignUser(@PathVariable String taskId, @RequestBody String userId) {
        User user = userService.getUserById(userId);
        if (Objects.isNull(user)) return new ResponseEntity<>(String.format("Cannot find user with id %s", userId), HttpStatus.BAD_REQUEST);
        Task task = taskService.assignUser(taskId, userId);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
}
