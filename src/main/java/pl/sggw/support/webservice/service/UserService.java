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

import java.util.List;
import java.util.Objects;
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

    public UserModel loadUserByCredentials(String login, String password){
        return userDAO.getUserByCredentials(login,password);
    }

    public UserModel loadUserByCredentials(String userId){
        return userDAO.getUserByID(Long.valueOf(userId));
    }

    public User fetchUserById(String userId) throws UsernameNotFoundException {
        return convert(userDAO.getUserByID(Long.valueOf(userId)));
    }

    public User saveOrUpdateUser(User user){
        UserModel userModel = new UserModel();
        userPopulator.reversePopulate(user,userModel);
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
        return convert(authenticationService.getCurrentUser());
    }

    private User convert(UserModel model){
        User user = new User();
        if(Objects.nonNull(model))userPopulator.populate(model,user);
        return user;
    }

}
