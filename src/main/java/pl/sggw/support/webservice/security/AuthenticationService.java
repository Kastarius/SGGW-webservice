package pl.sggw.support.webservice.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.security.exception.LoginFailureException;
import pl.sggw.support.webservice.security.util.UserAuthentication;
import pl.sggw.support.webservice.security.util.UserValidationResult;
import pl.sggw.support.webservice.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
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
        UserModel userModel = userService.getUserByCredentials(login, password);
        UserValidationResult validationResult = validate(userModel);
        if(isValid(validationResult)){
            userService.updateLastLogin(userModel);
            tokenAuthenticationService.addAuthentication(response, new UserAuthentication(userModel));
        } else {
            throw new LoginFailureException(validationResult);
        }
    }

    private boolean isValid(UserValidationResult validationResult) {
        return UserValidationResult.VALID.equals(validationResult) || UserValidationResult.LOGIN_REQUIRED.equals(validationResult);
    }

    public UserModel getCurrentUser(){
        return (UserModel) Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication()).orElse(new UserAuthentication(null))
                .getDetails();
    }

    public String getCurrentUserId(){
        return String.valueOf(getCurrentUser().getId());
    }

    public UserValidationResult validate(UserModel user){
        if(Objects.isNull(user)){
            return UserValidationResult.BAD_CREDENTIALS;
        } else if(user.isForceLogin()){
            return UserValidationResult.LOGIN_REQUIRED;
        } else if(!user.isEnabled()){
            return UserValidationResult.DISABLED_ACCOUNT;
        } else {
            return UserValidationResult.VALID;
        }
    }

    public void invalidateTokenAndSetForceLoginForCurrentUser(String token){
        this.tokenAuthenticationService.invalidateToken(token);
        this.userService.setForceLoginForCurrentUser();
    }
}
