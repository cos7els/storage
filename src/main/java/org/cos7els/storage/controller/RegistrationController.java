package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
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

import static org.cos7els.storage.util.ExceptionMessage.REGISTER_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping("/registration")
    public ResponseEntity<User> register(
            @RequestBody RegistrationRequest request
    ) {
        User user = registrationService.registerUser(request)
                .orElseThrow(() -> new CustomException(REGISTER_EXCEPTION));
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
