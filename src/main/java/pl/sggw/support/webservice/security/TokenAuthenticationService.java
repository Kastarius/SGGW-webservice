package pl.sggw.support.webservice.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.exception.TokenValidationFailureException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Kamil on 2017-10-21.
 */
@Service
public class TokenAuthenticationService {

    private static final String AUTH_HEADER_NAME = "X-AUTH-TOKEN";

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);


    @Autowired
    private TokenService tokenService;

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final UserModel user = authentication.getDetails();
        response.addHeader(AUTH_HEADER_NAME, tokenService.createTokenForUser(user));
    }

    public UserAuthentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (token != null) {
            UserModel user = null;
            try {
                user = tokenService.parseUserFromToken(token);
            } catch (TokenValidationFailureException e) {
                LOG.error(String.format("Exception occured: [%s] IP: [%s}",e.getMessage(),request.getRemoteAddr()));
            }
            if (user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
}
