package org.cos7els.storage.service.impl;

import lombok.AllArgsConstructor;
import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public Optional<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest) {
//        Authentication authenticate = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authenticationRequest.getUsername(),
//                        authenticationRequest.getPassword()
//                )
//        );
//        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
//        String token = jwtService.generateToken(userDetails);
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
//        authenticationResponse.setToken(token);
//        return authenticationResponse;
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        AuthenticationResponse authenticationResponse = null;
        if (
                userDetails.getUsername().equals(authenticationRequest.getUsername()) &&
                        passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())
        ) {
            String token = jwtService.generateToken(userDetails);
            authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(token);
        }
        return Optional.ofNullable(authenticationResponse);
    }
}
