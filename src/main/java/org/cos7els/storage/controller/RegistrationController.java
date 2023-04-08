package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> registerUser(@RequestBody RegistrationRequest registrationRequest) {
        return new ResponseEntity<>(registrationService.registerUser(registrationRequest), HttpStatus.CREATED);
    }
}