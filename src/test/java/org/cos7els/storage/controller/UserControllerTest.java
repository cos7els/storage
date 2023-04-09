package org.cos7els.storage.controller;

import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserResponse userResponse;
    private UserDetailsImpl userDetails;
    private ChangeEmailRequest changeEmailRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    public void init() {
        userResponse = new UserResponse();
        userDetails = new UserDetailsImpl(1L, null, null, null);
        changeEmailRequest = new ChangeEmailRequest();
        changePasswordRequest = new ChangePasswordRequest();
    }

    @Test
    public void getUserTest() {
        when(userService.getUserResponse(userDetails.getId())).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.getUser(userDetails);
        verify(userService).getUserResponse(userDetails.getId());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    public void changeEmailTest() {
        when(userService.changeEmail(changeEmailRequest, userDetails.getId())).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.changeEmail(changeEmailRequest, userDetails);
        verify(userService).changeEmail(changeEmailRequest, userDetails.getId());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    public void changePasswordTest() {
        when(userService.changePassword(changePasswordRequest, userDetails.getId())).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = userController.changePassword(changePasswordRequest, userDetails);
        verify(userService).changePassword(changePasswordRequest, userDetails.getId());
        assertEquals(userResponse, response.getBody());
    }

    @Test
    public void deleteUserTest() {
        userController.deleteUser(userDetails);
        verify(userService).deleteUser(userDetails.getId());
    }
}