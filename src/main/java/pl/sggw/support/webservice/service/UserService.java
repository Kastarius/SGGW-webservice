package pl.sggw.support.webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sggw.support.webservice.dao.UserDAO;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.populator.UserPopulator;
import pl.sggw.support.webservice.security.AuthenticationService;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Kamil on 2017-10-21.
 */
@Service("userDetailsService")
public class UserService implements UserDetailsService {

    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private UserPopulator userPopulator;

    @Override
    public UserModel loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDAO.getUserByLogin(username);
    }

    public UserModel getUserByCredentials(String login, String password){
        return userDAO.getUserByCredentials(login,password);
    }

    public UserModel getUserModelById(String userId){
        return userDAO.getUserByID(Long.valueOf(userId));
    }

    public User getUserById(String userId) throws UsernameNotFoundException {
        return convert(getUserModelById(userId));
    }

    public User saveOrUpdateUser(User user){
        UserModel userModel = new UserModel();
        userPopulator.reversePopulate(user,userModel);
        save(userModel);
        userPopulator.populate(userModel,user);
        return user;
    }

    public User saveOrUpdateUser(User user,boolean enableAccount){
        UserModel userModel = new UserModel();
        userPopulator.reversePopulate(user,userModel);
        userModel.setEnabled(enableAccount);
        save(userModel);
        userPopulator.populate(userModel,user);
        return user;
    }

    public void save(UserModel userModel) {
        userDAO.save(userModel);
    }

    public void removeUserById(long id){
        userDAO.remove(userDAO.getUserByID(id));
    }

    public List<User> getAllUsers(){
        return userDAO.getAllUsers().stream().map(this::convert).collect(Collectors.toList());
    }

    public User getCurrentUser(){
        return convert(getCurrentUserModel());
    }

    public UserModel getCurrentUserModel(){
        return authenticationService.getCurrentUser();
    }

    public void setForceLogin(UserModel userModel){
        userModel.setForceLogin(true);
        save(userModel);
    }

    public void setForceLoginForCurrentUser(){
        UserModel userModel = getCurrentUserModel();
        userModel.setForceLogin(true);
        save(userModel);
    }

    public void removeForceLogin(UserModel userModel){
        userModel.setForceLogin(false);
        save(userModel);
    }

    public void disableAccount(UserModel userModel){
        userModel.setEnabled(false);
        save(userModel);
    }

    public void enableAccount(UserModel userModel){
        userModel.setEnabled(true);
        save(userModel);
    }

    public void updateLastLogin(UserModel userModel){
        userModel.setLastLogin(new Date());
        userModel.setForceLogin(false);
        save(userModel);
    }

    private User convert(UserModel model){
        User user = new User();
        if(Objects.nonNull(model))userPopulator.populate(model,user);
        return user;
    }
}
