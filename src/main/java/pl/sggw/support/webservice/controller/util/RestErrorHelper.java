package pl.sggw.support.webservice.controller.util;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import pl.sggw.support.webservice.dao.exception.ConstraintViolationException;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Kamil on 2017-11-22.
 */
public class RestErrorHelper {

    private RestErrorHelper() {
    }

    public static String calculateErrorStamp(HttpServletRequest request, Exception ex) {
        return String.valueOf(ex.hashCode())+String.valueOf(request.hashCode());
    }


    public static String resolveErrorMessage(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage).reduce(((s, s2) -> s2 + "," + s)).orElse("");
    }

    public static String resolveErrorMessage(ConstraintViolationException ex) {
        return ex.getViolations().stream()
                .map(violation -> String.format("Violation:{(Class.Field:Value)%s.%s:%s (Message)%s}"
                                , violation.getRootClass().getClass().getSimpleName()
                                , violation.getFieldName()
                                , String.valueOf(violation.getInvalidValue())
                                , violation.getMessage()))
                .reduce(((s, s2) -> s2 + "," + s))
                .orElse("");
    }
}
