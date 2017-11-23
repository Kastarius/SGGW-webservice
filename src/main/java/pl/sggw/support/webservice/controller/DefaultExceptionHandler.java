package pl.sggw.support.webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.sggw.support.webservice.dao.exception.ConstraintViolationException;
import pl.sggw.support.webservice.dto.RestResponseEntity;
import pl.sggw.support.webservice.security.AuthenticationService;

import javax.servlet.http.HttpServletRequest;

import static pl.sggw.support.webservice.controller.util.RestErrorHelper.calculateErrorStamp;
import static pl.sggw.support.webservice.controller.util.RestErrorHelper.resolveErrorMessage;

/**
 * Created by Kamil on 2017-10-23.
 */
@RestControllerAdvice
public class DefaultExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Autowired
    private AuthenticationService authenticationService;

    @ExceptionHandler(ConstraintViolationException.class)
    public RestResponseEntity<String> handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex){
        LOG.error(String.format("Exception Occurred: URL[%s] User[%s] ErrorStamp[%s]"
                ,request.getRequestURL()
                ,authenticationService.getCurrentUserId()
                ,calculateErrorStamp(request, ex)));
        LOG.error(ex.getMessage()+calculateErrorStamp(request, ex),ex);
        String message = resolveErrorMessage(ex);
        return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,String.format("Constraint violation occurred {%s} [%s]",message,calculateErrorStamp(request, ex))
                    ,null
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponseEntity<String> handleInvalidDataException(HttpServletRequest request, MethodArgumentNotValidException ex){
        LOG.error(String.format("Exception Occurred: URL[%s] User[%s] ErrorStamp[%s]"
                ,request.getRequestURL()
                ,authenticationService.getCurrentUserId()
                ,calculateErrorStamp(request, ex)));
        LOG.error(ex.getMessage()+calculateErrorStamp(request, ex),ex);
        String massage = resolveErrorMessage(ex);
        LOG.error(ex.getMessage(),ex);
        return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,String.format("Data validation failed {%s} [%s]",massage,calculateErrorStamp(request, ex))
                    ,null
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public RestResponseEntity<String> handleAccessDeniedException(HttpServletRequest request, AccessDeniedException ex){
        LOG.error(String.format("Exception Occurred: URL[%s] User[%s] ErrorStamp[%s]"
                ,request.getRequestURL()
                ,authenticationService.getCurrentUserId()
                ,calculateErrorStamp(request, ex)));
        LOG.error(ex.getMessage()+calculateErrorStamp(request, ex),ex);
        return new RestResponseEntity<>(String.valueOf(HttpStatus.FORBIDDEN.value())
                    ,String.format("Access denied: No permissions to the resource [%s] , [%s]",request.getRequestURL(),calculateErrorStamp(request, ex))
                    ,null
                , HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public RestResponseEntity<String> handleAnyException(HttpServletRequest request, Exception ex){
        LOG.error(String.format("Exception Occurred: URL[%s] User[%s] ErrorStamp[%s]"
                ,request.getRequestURL()
                ,authenticationService.getCurrentUserId()
                ,calculateErrorStamp(request, ex)));
        LOG.error(ex.getMessage()+calculateErrorStamp(request, ex),ex);
        return new RestResponseEntity<>(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    ,String.format("Internal Server Error [%s]",calculateErrorStamp(request, ex))
                    ,null
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
