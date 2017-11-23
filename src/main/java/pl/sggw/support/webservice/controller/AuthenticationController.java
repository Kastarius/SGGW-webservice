package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.RestResponseEntity;
import pl.sggw.support.webservice.security.AuthenticationService;
import pl.sggw.support.webservice.security.exception.LoginFailureException;
import pl.sggw.support.webservice.security.util.UserValidationResult;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import static pl.sggw.support.webservice.security.util.SecurityConstants.AUTH_HEADER_NAME;


/**
 * Created by Kamil on 2017-10-21.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);
    private static final String AUTH_ERROR_CODE = "403";
    private static final String AUTH_ERROR_DESC = "User authentication failed";

    @Autowired
    private AuthenticationService authenticationService;

    @RequestMapping(method = RequestMethod.GET)
    public RestResponseEntity<UserValidationResult> authenticate(@RequestParam(value="login", defaultValue="") String login, @RequestParam(value="password", defaultValue="") String password, ServletResponse response) {
        try {
            this.authenticationService.tryAuthenticateUser(login,password, (HttpServletResponse) response);
            return new RestResponseEntity<>(UserValidationResult.VALID,HttpStatus.OK);
        } catch (LoginFailureException e) {
            LOG.error(e.getMessage(),e);
            return new RestResponseEntity<>(AUTH_ERROR_CODE, AUTH_ERROR_DESC,e.getValidationResult(),HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public RestResponseEntity invalidateToken(@RequestHeader(AUTH_HEADER_NAME) String token){
        this.authenticationService.invalidateTokenAndSetForceLoginForCurrentUser(token);
        return new RestResponseEntity(HttpStatus.OK);
    }
}
