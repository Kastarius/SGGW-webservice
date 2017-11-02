package pl.sggw.support.webservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Created by Kamil on 2017-11-02.
 */
@Component
public class SecurityHelper {

    /**
     * Salt
     */
    @Value("${password.encode.salt}")
    private String salt;

    @Autowired
    private ShaPasswordEncoder passwordEncoder;

    public String encodePassword(String password){
        return passwordEncoder.encodePassword(password,salt);
    }
}
