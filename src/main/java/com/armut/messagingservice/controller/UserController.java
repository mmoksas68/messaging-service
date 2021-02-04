package com.armut.messagingservice.controller;

import com.armut.messagingservice.entity.Errors;
import com.armut.messagingservice.entity.User;
import com.armut.messagingservice.exception.NoUserFoundException;
import com.armut.messagingservice.request.UserRequest;
import com.armut.messagingservice.request.UserUpdateRequest;
import com.armut.messagingservice.security.CustomUserDetailsService;
import com.armut.messagingservice.service.ErrorsService;
import com.armut.messagingservice.service.OperatorService;
import com.armut.messagingservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    ErrorsService errorsService;

    @ApiOperation("Add a new user")
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody UserRequest request) {
        User addedUser = operatorService.addUser(request.toUser());
        return new ResponseEntity<>(addedUser, HttpStatus.OK);
    }

    @ApiOperation("Get current user")
    @GetMapping
    public ResponseEntity<User> getCurrent() throws NoUserFoundException {
        User user = customUserDetailsService.getCurrentUser();
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @ApiOperation("Get all users")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @ApiOperation("Get user by given id")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            User user = userService.get(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoUserFoundException e) {
            errorsService.add(Errors.newError(id, null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation("Update user by given id")
    @PutMapping
    public ResponseEntity<?> updateUserById(@RequestBody UserUpdateRequest request) {
        try {
            User updatedUser = userService.update(request.toUser());
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (NoUserFoundException e) {
            errorsService.add(Errors.newError(request.getId(), null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }

    @ApiOperation("Delete user by given id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        try {
            User userToDelete = userService.get(id);
            operatorService.deleteUser(userToDelete);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoUserFoundException e) {
            errorsService.add(Errors.newError(id, null, e.getMessage()));
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
