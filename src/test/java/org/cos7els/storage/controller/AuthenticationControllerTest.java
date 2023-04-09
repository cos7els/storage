package org.cos7els.storage.controller;

import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.service.AuthenticationService;
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
public class AuthenticationControllerTest {
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;
    AuthenticationResponse authenticationResponse;
    AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void init() {
        authenticationResponse = new AuthenticationResponse();
        authenticationRequest = new AuthenticationRequest();
    }

    @Test
    public void getAlbumsTest() {
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        ResponseEntity<AuthenticationResponse> authenticate = authenticationController.authenticate(authenticationRequest);
        verify(authenticationService).authenticate(authenticationRequest);
        assertEquals(authenticationResponse, authenticate.getBody());
    }
}
