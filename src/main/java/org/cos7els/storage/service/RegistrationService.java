package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;

public interface RegistrationService {
    UserResponse registerUser(RegistrationRequest request);

    User registerAdmin(RegistrationRequest request);
}