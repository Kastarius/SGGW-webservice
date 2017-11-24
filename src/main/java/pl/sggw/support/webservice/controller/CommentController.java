package pl.sggw.support.webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.Comment;
import pl.sggw.support.webservice.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService service;

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.DELETE, path = "{id}")
    @ResponseBody
    public void deleteComment(@PathVariable String id) {
        service.remove(Long.valueOf(id));
    }

    @Secured("ROLE_USER")
    @RequestMapping(method = RequestMethod.PUT, path = "{id}")
    @ResponseBody
    public void editComment(@PathVariable String id, @RequestBody Comment comment) {
        service.edit(Long.valueOf(id), comment.getContent());
    }
}
