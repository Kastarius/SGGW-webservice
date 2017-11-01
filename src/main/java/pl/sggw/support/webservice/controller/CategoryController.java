package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.Category;
import pl.sggw.support.webservice.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Category> getAll() {
        return service.getAll();
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Category add(@RequestBody Category category) {
        return service.save(category);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity update(@RequestBody Category category) {
        Category category1= service.saveOrUpdate(category);
        return new ResponseEntity(category1, HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/{priorityId}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseEntity delete(@PathVariable String category) {
        service.remove(Long.valueOf(category));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }
}
