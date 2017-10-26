package pl.sggw.support.webservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.exception.LoginFailureException;
import pl.sggw.support.webservice.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

/**
 * Created by Kamil on 2017-10-21.
 */
@Service
public class AuthenticationService {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private UserService userService;

    public void tryAuthenticateUser(String login, String password, HttpServletResponse response) throws LoginFailureException{
        Optional<UserModel> userModel = Optional.ofNullable(userService.loadUserByCredentials(login, password));
        if(userModel.isPresent()){
            tokenAuthenticationService.addAuthentication(response,new UserAuthentication(userModel.get()));
        } else {
            throw new LoginFailureException();
        }
    }

    public UserModel getCurrentUser(){
        return (UserModel) Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).orElse(new UserAuthentication(null))
                .getDetails();
    }
}
