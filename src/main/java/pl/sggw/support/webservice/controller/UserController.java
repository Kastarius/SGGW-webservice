package pl.sggw.support.webservice.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.RestResponseEntity;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.service.UserService;

import javax.servlet.ServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * Created by Kamil on 2017-10-22.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public RestResponseEntity<List<User>> getAllUsers() {
        return new RestResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public RestResponseEntity<User> getCurrentUser() {
        return new RestResponseEntity<>(userService.getCurrentUser(),HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponseEntity<User> getUserById(@PathVariable String userId) {
        return new RestResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseEntity<User> addUser(@Valid @RequestBody User user) {
        return new RestResponseEntity<>(userService.saveOrUpdateUser(user,true),HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public RestResponseEntity updateUser(@Valid @RequestBody User user) {
        UserModel userModel = userService.getUserModelById(String.valueOf(user.getId()));
        if(Objects.isNull(userModel))
            return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,String.format("Cannot find User with id %s"
                    ,String.valueOf(user.getId())),HttpStatus.BAD_REQUEST);

        userService.saveOrUpdateUser(user);
        userService.setForceLogin(userModel);
        return new RestResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.PATCH)
    @ResponseBody
    public RestResponseEntity updateCurrentUserPassword(@RequestParam(value="password", defaultValue="") String password, ServletRequest request) {
        if(!StringUtils.isBlank(password)){
            UserModel userModel = userService.getCurrentUserModel();
            userModel.setPassword(password);
            userService.save(userModel);
            userService.setForceLogin(userModel);
        } else {
            return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,"Password cannot be empty"
                    ,HttpStatus.BAD_REQUEST);
        }
        return new RestResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}/password", method = RequestMethod.PATCH)
    @ResponseBody
    public RestResponseEntity updateUserPassword(@PathVariable String userId,@RequestParam(value="password", defaultValue="") String password) {
        if(!StringUtils.isBlank(password)){
            UserModel userModel = userService.getUserModelById(userId);
            if(Objects.isNull(userModel))
                return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                        ,String.format("Cannot find User with id %s",userId)
                        ,HttpStatus.BAD_REQUEST);

            userModel.setPassword(password);
            userService.save(userModel);
            userService.setForceLogin(userModel);
        } else {
            return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,"Password cannot be empty"
                    ,HttpStatus.BAD_REQUEST);
        }
        return new RestResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    public RestResponseEntity removeUserById(@PathVariable String userId) {
        UserModel userModel = userService.getUserModelById(userId);
        if(Objects.isNull(userModel))
            return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,String.format("Cannot find User with id %s",userId)
                    ,HttpStatus.BAD_REQUEST);

        userService.removeUserById(Long.valueOf(userId));
        return new RestResponseEntity(HttpStatus.ACCEPTED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}/enable", method = RequestMethod.PATCH)
    public RestResponseEntity enableUserById(@PathVariable String userId,@RequestParam(value="enableAccount", defaultValue="true") Boolean enableAccount) {
        UserModel userModel = userService.getUserModelById(userId);
        if(Objects.isNull(userModel))
            return new RestResponseEntity<>(String.valueOf(HttpStatus.BAD_REQUEST.value())
                    ,String.format("Cannot find User with id %s",userId)
                    ,HttpStatus.BAD_REQUEST);

        if(Boolean.TRUE.equals(enableAccount)){
            userService.enableAccount(userModel);
        } else {
            userService.disableAccount(userModel);
        }
        return new RestResponseEntity(HttpStatus.ACCEPTED);
    }

}
