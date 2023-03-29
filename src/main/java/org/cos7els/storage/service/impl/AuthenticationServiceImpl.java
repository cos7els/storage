package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.security.JwtService;
import org.cos7els.storage.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
//    private final AuthenticationManager authenticationManager;

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
//        return Optional.of(authenticationResponse);
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        AuthenticationResponse authenticationResponse = null;
        if (passwordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            authenticationResponse = new AuthenticationResponse();
            authenticationResponse.setToken(jwtService.generateToken(userDetails));
        }
        return Optional.ofNullable(authenticationResponse);
    }
}
