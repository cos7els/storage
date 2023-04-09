package org.cos7els.storage.controller;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.service.RegistrationService;
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
public class RegistrationControllerTest {
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private RegistrationController registrationController;
    private User user;
    private UserResponse userResponse;
    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void init() {
        user = new User();
        userResponse = new UserResponse();
        registrationRequest = new RegistrationRequest();
    }

    @Test
    public void registerUserTest() {
        when(registrationService.registerUser(registrationRequest)).thenReturn(userResponse);
        ResponseEntity<UserResponse> response = registrationController.registerUser(registrationRequest);
        verify(registrationService).registerUser(registrationRequest);
        assertEquals(userResponse, response.getBody());
    }

    @Test
    public void registerAdminTest() {
        when(registrationService.registerAdmin(registrationRequest)).thenReturn(user);
        ResponseEntity<User> response = registrationController.registerAdmin(registrationRequest);
        verify(registrationService).registerAdmin(registrationRequest);
        assertEquals(user, response.getBody());
    }
}