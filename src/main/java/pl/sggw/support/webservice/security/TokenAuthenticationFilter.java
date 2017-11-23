package pl.sggw.support.webservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import pl.sggw.support.webservice.security.exception.TokenValidationFailureException;
import pl.sggw.support.webservice.security.exception.UserValidationFailureException;
import pl.sggw.support.webservice.security.util.TokenValidationResult;
import pl.sggw.support.webservice.security.util.UserValidationResult;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static pl.sggw.support.webservice.controller.util.ResponseHelper.createResponse;

/**
 * Created by Kamil on 2017-10-21.
 */
public class TokenAuthenticationFilter extends GenericFilterBean {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationFilter.class);

    private final TokenAuthenticationService authenticationService;

    public TokenAuthenticationFilter(TokenAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        try {
            Authentication authentication = authenticationService.getAuthentication(httpRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (TokenValidationFailureException e) {
            TokenValidationResult validationResult = e.getTokenValidationResult();
            resolveResponse(httpResponse,validationResult.getCode(),validationResult.getMessage());
        } catch (UserValidationFailureException e) {
            UserValidationResult validationResult = e.getValidationResult();
            resolveResponse(httpResponse,validationResult.getCode(),validationResult.getMessage());
        } finally {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    private void resolveResponse(HttpServletResponse httpResponse,String errorCode,String errorDesc) {
        try {
            createResponse(httpResponse,HttpServletResponse.SC_FORBIDDEN,errorCode,"Access denied: "+errorDesc,null);
        } catch (IOException e) {
            LOG.error(e.getMessage(),e);
        }
    }
}
