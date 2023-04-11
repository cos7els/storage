package org.cos7els.storage.controller;

import io.swagger.v3.oas.annotations.Parameter;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(userService.getUserResponse(userDetails.getId()), HttpStatus.OK);
    }

    @PutMapping("/user/change/email")
    public ResponseEntity<UserResponse> changeEmail(
            @RequestBody ChangeEmailRequest request,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        return bindingResult.hasErrors() ?
                new ResponseEntity<>(HttpStatus.CONFLICT) :
                new ResponseEntity<>(userService.changeEmail(request, userDetails.getId()), HttpStatus.OK);
    }

    @PutMapping("/user/change/password")
    public ResponseEntity<UserResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails,
            BindingResult bindingResult
    ) {
        return bindingResult.hasErrors() ?
                new ResponseEntity<>(HttpStatus.CONFLICT) :
                new ResponseEntity<>(userService.changePassword(request, userDetails.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/user")
    public ResponseEntity<HttpStatus> deleteUser(
            @AuthenticationPrincipal @Parameter(hidden = true) UserDetailsImpl userDetails
    ) {
        userService.deleteUser(userDetails.getId());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/admin/user/{userId}")
    public ResponseEntity<User> getUser(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUser(userId), HttpStatus.OK);
    }

    @PostMapping("/admin/user")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("/admin/user")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @DeleteMapping("/admin/user/{userId}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}