package org.cos7els.storage.service;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUser(Long id);

    Optional<List<User>> getAllUsers();

    Optional<User> saveUser(User user);

    Integer deleteUser(Long id);

    boolean isUserExists(Long id);

    Optional<User> changePassword(ChangePasswordRequest request, Long id);

    Optional<User> changeEmail(ChangeEmailRequest request, Long id);

    UserResponse userToResponse(User user);

    List<UserResponse> usersToResponses(List<User> users);
}