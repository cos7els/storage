package org.cos7els.storage.controller;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class RegistrationController {
    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(
            @RequestBody RegistrationRequest registrationRequest
    ) {
        Optional<User> user = registrationService.registerUser(registrationRequest);
        return user.isPresent() ?
                new ResponseEntity<>(user.get(), HttpStatus.CREATED) :
                new ResponseEntity<>(HttpStatus.CONFLICT);
    }
}
