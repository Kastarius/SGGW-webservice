package pl.sggw.support.webservice.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import pl.sggw.support.webservice.dto.User;
import pl.sggw.support.webservice.model.UserModel;
import pl.sggw.support.webservice.service.UserService;

import java.util.List;

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
        return new ResponseEntity<>(userService.fetchUserById(userId),HttpStatus.OK);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveOrUpdateUser(user),HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResponseEntity updateUser(@RequestBody User user) {
        userService.saveOrUpdateUser(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}/password", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity updateUserPassword(@PathVariable String userId,@RequestParam(value="password", defaultValue="") String password) {
        if(!StringUtils.isBlank(password)){
            UserModel userModel = userService.loadUserByCredentials(userId);
            userModel.setPassword(password);
            userService.save(userModel);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity removeUserById(@PathVariable String userId) {
        userService.removeUserById(Long.valueOf(userId));
        return new ResponseEntity(HttpStatus.ACCEPTED);
    }

}
