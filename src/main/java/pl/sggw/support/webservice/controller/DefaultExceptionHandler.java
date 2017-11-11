package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sggw.support.webservice.controller.exception.InvalidDataException;
import pl.sggw.support.webservice.dao.exception.ConstraintViolationException;
import pl.sggw.support.webservice.dao.exception.DatabaseOperationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Kamil on 2017-10-23.
 */
@RestControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<String> handleDatabaseOperationException(HttpServletRequest request, DatabaseOperationException ex){
        LOG.error("Exception Occured:: URL="+request.getRequestURL());
        LOG.error(String.valueOf(ex.hashCode()));
        LOG.error(ex.getMessage(),ex);
        return new ResponseEntity<>(String.format("Bad Request {%s} [%s]",ex.getMessage(),String.valueOf(ex.hashCode())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex){
        LOG.error("Exception Occured:: URL="+request.getRequestURL());
        LOG.error(String.valueOf(ex.hashCode()));
        LOG.error(ex.getMessage(),ex);
        return new ResponseEntity<>(String.format("Bad Request {%s} [%s]",ex.getMessage(),String.valueOf(ex.hashCode())), HttpStatus.BAD_REQUEST); //TODO parse info about violations
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<String> handleInvalidDataException(HttpServletRequest request, InvalidDataException ex){
        LOG.error("Exception Occured:: URL="+request.getRequestURL());
        LOG.error(String.valueOf(ex.hashCode()));
        Optional<String> reduce = ex.getErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).reduce(((s, s2) -> s2 + "," + s));
        LOG.error(ex.getMessage(),ex);
        return new ResponseEntity<>(String.format("Bad Request {%s} [%s]",reduce.orElse(""),String.valueOf(ex.hashCode())), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAnyException(HttpServletRequest request, Exception ex){
        LOG.error("Exception Occured:: URL="+request.getRequestURL());
        LOG.error(String.valueOf(ex.hashCode()));
        LOG.error(ex.getMessage(),ex);
        return new ResponseEntity<>(String.format("Internal Server Error [%s]",String.valueOf(ex.hashCode())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
