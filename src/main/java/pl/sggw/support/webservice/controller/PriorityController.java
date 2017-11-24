package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.Priority;
import pl.sggw.support.webservice.service.PriorityService;

import java.util.List;

@RestController
@RequestMapping("/priority")
public class PriorityController {

    @Autowired
    private PriorityService service;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Priority> getAll() {
        return service.getAll();
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Priority add(@RequestBody Priority priority) {
        return service.save(priority);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Priority priority) {
        Priority priority1 = service.saveOrUpdate(priority);
        return new ResponseEntity(priority1, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{priorityId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable String priorityId) {
        service.remove(Long.valueOf(priorityId));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
