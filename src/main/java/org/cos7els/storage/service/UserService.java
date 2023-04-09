package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserResponse(Long userId);

    User getUser(Long userId);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long userId);

    UserResponse changePassword(ChangePasswordRequest request, Long userId);

    UserResponse changeEmail(ChangeEmailRequest request, Long userId);

    void updateUsedSpace(Long userId, Long size);

    void checkUsedSpace(Long userId);
}