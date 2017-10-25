package pl.sggw.support.webservice.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.exception.TokenValidationFailureException;
import pl.sggw.support.webservice.service.UserService;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kamil on 2017-10-21.
 */
@Component
public class TokenService {

    private static final Logger LOG = LoggerFactory.getLogger(TokenService.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.expiration.time}")
    private String tokenExpirationTime;

    @Autowired
    private UserService userService;


    public UserModel parseUserFromToken(String token) throws TokenValidationFailureException {
        try {
            String username = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            return userService.loadUserByUsername(username);
        } catch(ExpiredJwtException | MalformedJwtException | SignatureException e){
            LOG.error(e.getMessage());
            throw new TokenValidationFailureException(e);
        }
    }

    public String createTokenForUser(UserModel user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setExpiration(calculateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, Integer.valueOf(tokenExpirationTime));
        return calendar.getTime();
    }
}
