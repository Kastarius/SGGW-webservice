package pl.sggw.support.webservice.security;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.exception.TokenValidationFailureException;
import pl.sggw.support.webservice.security.exception.UserValidationFailureException;
import pl.sggw.support.webservice.security.util.TokenValidationResult;
import pl.sggw.support.webservice.security.util.UserAuthentication;
import pl.sggw.support.webservice.security.util.UserValidationResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

import static pl.sggw.support.webservice.security.util.SecurityConstants.AUTH_HEADER_NAME;

/**
 * Created by Kamil on 2017-10-21.
 */
@Service
public class TokenAuthenticationService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenAuthenticationService.class);

    private List<String> tokenBlacklist = new ArrayList<>();

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationService authenticationService;

    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final UserModel user = authentication.getDetails();
        response.addHeader(AUTH_HEADER_NAME, tokenService.createTokenForUser(user));
    }

    public UserAuthentication getAuthentication(HttpServletRequest request) throws TokenValidationFailureException, UserValidationFailureException {
        final String token = request.getHeader(AUTH_HEADER_NAME);
        if (StringUtils.isNotBlank(token)) {
            UserModel user = null;
            try {
                user = tokenService.parseUserFromToken(token);
            } catch (TokenValidationFailureException e) {
                LOG.error(String.format("Exception occured: [%s] IP: [%s}",e.getMessage(),request.getRemoteAddr()));
                throw e;
            }

            UserValidationResult validationResult = authenticationService.validate(user);
            if (UserValidationResult.VALID.equals(validationResult)) {
                return new UserAuthentication(user);
            } else {
                throw new UserValidationFailureException(validationResult);
            }
        }
        return null;
    }

    public TokenValidationResult validate(String token) {
        if(this.tokenBlacklist.contains(token)) return TokenValidationResult.EXPIRED;
        return this.tokenService.validate(token);
    }

    public void invalidateToken(String token){
        this.tokenBlacklist.add(token);
    }
}
