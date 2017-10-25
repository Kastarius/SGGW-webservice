package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sggw.support.webservice.security.exception.TokenValidationFailureException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Kamil on 2017-10-23.
 */
@RestControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(HttpServletRequest request, Exception ex){
        LOG.error("Exception Occured:: URL="+request.getRequestURL());
        LOG.error(ex.getMessage(),ex);
        return new ResponseEntity<>("Something go wrong, we fix it soon \nHave a nice day", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
