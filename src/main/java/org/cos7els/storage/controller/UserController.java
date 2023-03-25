package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.*;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<User> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userService.getUser(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/change/password")
    public ResponseEntity<User> changePassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.changePassword(request, userDetails.getId())
                .orElseThrow(() -> new CustomException(CHANGE_PASSWORD_EXCEPTION));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/user/change/email")
    public ResponseEntity<User> changeEmail(
            @RequestBody ChangeEmailRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.changeEmail(request, userDetails.getId())
                .orElseThrow(() -> new CustomException(CHANGE_EMAIL_EXCEPTION));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    @PutMapping("/user/change/plan")
//    public ResponseEntity<User> changePlan(
//            @RequestBody ChangePlanRequest request,
//            @AuthenticationPrincipal UserDetailsImpl userDetails
//    ) {
//
//    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<User> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("admin/user/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("admin/user/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers().orElseThrow(() -> new NotFoundException(USERS_NOT_FOUND));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("admin/user/create")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @PutMapping("admin/user/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.isUserExists(id) ?
                new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK) :
                new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("admin/user/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("admin/user/delete/all")
    public ResponseEntity<HttpStatus> deleteAllUsers() {
        userService.deleteAllUsers();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}