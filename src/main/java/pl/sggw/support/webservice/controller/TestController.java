package pl.sggw.support.webservice.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dao.UserDAO;
import pl.sggw.support.webservice.security.AuthenticationService;
import pl.sggw.support.webservice.service.EmailService;


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

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public ResponseEntity<String> greeting(@RequestParam(value="name", defaultValue="World") String name,@RequestParam(value="recipient", defaultValue="sggw.support@o2.pl") String recipient,@RequestParam(value="title", defaultValue="Hello") String title,@RequestParam(value="text", defaultValue="Test mail text") String text) {
        userDAO.getAllUserWhereRole("ADMIN");
        if("template".equals(name)) {
            emailService.sendTestEmail(recipient, title);
        } else {
            emailService.sendEmail(recipient,title,text,false);
        }
        return new ResponseEntity<>(String.format("Hello %s received name is %s",authenticationService.getCurrentUser().getLogin(),name), HttpStatus.I_AM_A_TEAPOT);
    }
}
