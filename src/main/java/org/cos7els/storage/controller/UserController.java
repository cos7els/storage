package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
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

import static org.cos7els.storage.util.ExceptionMessage.USERS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.CREATE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.CHANGE_EMAIL_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    public ResponseEntity<UserResponse> getUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.getUser(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return new ResponseEntity<>(
                userService.userToResponse(user),
                HttpStatus.OK
        );
    }

    @PutMapping("/user/change/email")
    public ResponseEntity<UserResponse> changeEmail(
            @RequestBody ChangeEmailRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.changeEmail(request, userDetails.getId())
                .orElseThrow(() -> new CustomException(CHANGE_EMAIL_EXCEPTION));
        return new ResponseEntity<>(
                userService.userToResponse(user),
                HttpStatus.OK
        );
    }

    @PutMapping("/user/change/password")
    public ResponseEntity<UserResponse> changePassword(
            @RequestBody ChangePasswordRequest request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.changePassword(request, userDetails.getId())
                .orElseThrow(() -> new CustomException(CHANGE_PASSWORD_EXCEPTION));
        return new ResponseEntity<>(
                userService.userToResponse(user),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<User> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/user")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers()
                .orElseThrow(() -> new NotFoundException(USERS_NOT_FOUND));
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/user/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        User user = userService.getUser(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/admin/user")
    public ResponseEntity<User> createUser(@RequestBody User request) {
        User user = userService.saveUser(request)
                .orElseThrow(() -> new CustomException(CREATE_USER_EXCEPTION));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("admin/user")
    public ResponseEntity<User> updateUser(@RequestBody User request) {
        User user = userService.saveUser(request)
                .orElseThrow(() -> new CustomException(UPDATE_USER_EXCEPTION));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
    
    @DeleteMapping("/admin/user/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        Integer result = userService.deleteUser(id);
        if (result == 0) {
            throw new CustomException(DELETE_USER_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}