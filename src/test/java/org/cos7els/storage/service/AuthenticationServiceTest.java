package org.cos7els.storage.service;

import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.security.service.JwtService;
import org.cos7els.storage.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private JwtService jwtService;
    @Mock
    private UserDetailsService userDetailsService;
    @Mock
    private AuthenticationManager authenticationManager;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    private String string;
    private UserDetailsImpl userDetails;
    private AuthenticationRequest authenticationRequest;
    private UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken;

    @BeforeEach
    public void init() {
        string = "root";
        userDetails = new UserDetailsImpl();
        authenticationRequest = new AuthenticationRequest(string, string);
        usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(string, string);
    }

    @Test
    public void authenticateTest() {
        when(userDetailsService.loadUserByUsername(authenticationRequest.getUsername())).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn(string);
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        verify(authenticationManager).authenticate(usernamePasswordAuthenticationToken);
        verify(userDetailsService).loadUserByUsername(string);
        verify(jwtService).generateToken(userDetails);
        assertEquals(string, authenticationResponse.getToken());
    }
}