package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dao.UserDAO;
import pl.sggw.support.webservice.security.AuthenticationService;


/**
 * Created by Kamil on 2017-10-21.
 */
@RestController
@RequestMapping("/test")
public class TestController {

    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserDAO userDAO;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> greeting(@RequestParam(value="name", defaultValue="World") String name) {
        userDAO.getAllUserWhereRole("ADMIN"); //TODO zwraca dwa
        return new ResponseEntity<>(String.format("Hello %s received name is %s",authenticationService.getCurrentUser().getLogin(),name), HttpStatus.I_AM_A_TEAPOT);
    }
}
