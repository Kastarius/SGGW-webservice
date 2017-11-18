package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.Status;
import pl.sggw.support.webservice.service.StatusService;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/statuses")
public class StatusController {

    @Autowired
    StatusService statusService;

    @ResponseBody
    @GetMapping
    public List<Status> getAll() {
        return statusService.getAll();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public Status getStatus(@PathVariable String id) {
        return statusService.getStatus(id);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @PostMapping
    public ResponseEntity<Status> add(@RequestBody Status status) {
        return new ResponseEntity<Status>(statusService.save(status), HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        Status status = statusService.getStatus(id);
        if(Objects.isNull(status)) return new ResponseEntity(HttpStatus.NOT_FOUND);
        statusService.remove(Long.valueOf(id));
        return new ResponseEntity(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @ResponseBody
    @PutMapping
    public ResponseEntity<Status> update(@RequestBody Status status) {
        Status status1 = statusService.saveOrUpdate(status);
        return new ResponseEntity<Status>(status1, HttpStatus.CREATED);
    }

}
