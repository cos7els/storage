package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.BadDataException;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_BAD_REQUEST;
import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> getUserByUsername(String username) {
        return Optional.of(userRepository.getUserByUsername(username));
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public Optional<List<User>> getAllUsers() {
        return Optional.of(userRepository.findAll());
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public boolean isUserExists(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> changePassword(ChangePasswordRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()) &&
                request.getNewPassword().equals(request.getRepeatNewPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            return Optional.of(userRepository.save(user));
        } else {
            throw new BadDataException(CHANGE_PASSWORD_BAD_REQUEST);
        }
    }

    @Override
    public Optional<User> changeEmail(ChangeEmailRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(request.getEmail());
        return Optional.of(userRepository.save(user));
    }
}