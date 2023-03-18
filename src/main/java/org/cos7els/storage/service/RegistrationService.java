package org.cos7els.storage.service;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.RegistrationRequest;

import java.util.Optional;

public interface RegistrationService {
    Optional<User> registerUser(RegistrationRequest request);
}
