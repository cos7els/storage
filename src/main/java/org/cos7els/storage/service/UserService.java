package org.cos7els.storage.service;

import org.cos7els.storage.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> getUser(Long id);

    List<User> getAllUsers();

    User saveUser(User user);

    void deleteUser(Long id);

    void deleteAllUsers();

    boolean isUserExists(Long id);
}