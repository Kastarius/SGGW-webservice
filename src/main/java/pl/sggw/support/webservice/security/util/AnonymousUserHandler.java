package pl.sggw.support.webservice.security.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.sggw.support.webservice.controller.util.ResponseHelper.createResponse;
import static pl.sggw.support.webservice.controller.util.RestErrorHelper.calculateErrorStamp;

/**
 * Created by Kamil on 2017-11-19.
 */
public class AnonymousUserHandler implements AuthenticationEntryPoint {

    private static final Logger LOG = LoggerFactory.getLogger(AnonymousUserHandler.class);

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        LOG.error(String.format("Exception Occurred: URL[%s] User[%s] ErrorStamp[%s]",httpServletRequest.getRequestURL(),"Anonymous", calculateErrorStamp(httpServletRequest, e)));
        LOG.error(e.getMessage(),e);
        createResponse(httpServletResponse,HttpServletResponse.SC_FORBIDDEN
                ,String.valueOf(HttpServletResponse.SC_FORBIDDEN)
                ,String.format("Access denied: No permissions to the resource [%s]",httpServletRequest.getRequestURL()),null);
    }
}
