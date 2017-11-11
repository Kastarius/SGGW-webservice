package pl.sggw.support.webservice.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.controller.exception.InvalidDataException;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.service.UserService;

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
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> getCurrentUser() {
        return new ResponseEntity<>(userService.getCurrentUser(),HttpStatus.OK);
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> addUser(@Valid @RequestBody User user, BindingResult result) {
        if(result.hasErrors()){
            throw new InvalidDataException(result.getFieldErrors());
        }
        return new ResponseEntity<>(userService.saveOrUpdateUser(user),HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody User user) {
        UserModel userModel = userService.getUserModelById(String.valueOf(user.getId()));
        if(Objects.isNull(userModel)) return new ResponseEntity<>(String.format("Cannot find User with id %s",String.valueOf(user.getId())),HttpStatus.BAD_REQUEST);
        userService.saveOrUpdateUser(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/user/password", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity updateCurrentUserPassword(@PathVariable String userId,@RequestParam(value="password", defaultValue="") String password) {
        if(!StringUtils.isBlank(password)){
            UserModel userModel = userService.getCurrentUserModel();
            userModel.setPassword(password);
            userService.save(userModel);
        } else {
            return new ResponseEntity<>("Password cannot be empty",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}/password", method = RequestMethod.PATCH)
    @ResponseBody
    public ResponseEntity updateUserPassword(@PathVariable String userId,@RequestParam(value="password", defaultValue="") String password) {
        if(!StringUtils.isBlank(password)){
            UserModel userModel = userService.getUserModelById(userId);
            if(Objects.isNull(userModel)) return new ResponseEntity<>(String.format("Cannot find User with id %s",userId),HttpStatus.BAD_REQUEST);
            userModel.setPassword(password);
            userService.save(userModel);
        } else {
            return new ResponseEntity<>("Password cannot be empty",HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity removeUserById(@PathVariable String userId) {
        UserModel userModel = userService.getUserModelById(userId);
        if(Objects.isNull(userModel)) return new ResponseEntity<>(String.format("Cannot find User with id %s",userId),HttpStatus.BAD_REQUEST);
        userService.removeUserById(Long.valueOf(userId));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
