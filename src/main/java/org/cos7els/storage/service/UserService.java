package org.cos7els.storage.service;

import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByUsername(String username);

    Optional<User> getUser(Long id);

    Optional<List<User>> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long id);

    void deleteAllUsers();

    boolean isUserExists(Long id);

    Optional<User> changePassword(ChangePasswordRequest request, Long id);

    Optional<User> changeEmail(ChangeEmailRequest request, Long id);
}