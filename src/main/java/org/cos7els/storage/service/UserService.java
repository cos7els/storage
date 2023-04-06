package org.cos7els.storage.service;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserResponse(Long id);
    User getUser(Long id);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long id);

    UserResponse changePassword(ChangePasswordRequest request, Long id);

    UserResponse changeEmail(ChangeEmailRequest request, Long id);

    void updateUsedSpace(Long id, Long size);

    List<UserResponse> usersToResponses(List<User> users);

    UserResponse userToResponse(User user);
}