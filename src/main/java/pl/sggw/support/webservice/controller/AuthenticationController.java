package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.sggw.support.webservice.security.AuthenticationService;
import pl.sggw.support.webservice.security.exception.LoginFailureException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Kamil on 2017-10-21.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity authenticate(@RequestParam(value="login", defaultValue="") String login,@RequestParam(value="password", defaultValue="") String password, ServletResponse response) {
        try {
            authenticationService.tryAuthenticateUser(login,password, (HttpServletResponse) response);
            return new ResponseEntity(HttpStatus.OK);
        } catch (LoginFailureException e) {
            LOG.error(e.getMessage(),e);
            return new ResponseEntity(HttpStatus.FORBIDDEN);
        }
    }
}
