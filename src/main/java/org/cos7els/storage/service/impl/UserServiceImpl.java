package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.BadDataException;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NoContentException;
import org.cos7els.storage.mapper.SubscriptionToSubscriptionResponseMapper;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_BAD_CREDENTIALS;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_USER_EXCEPTION;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return selectUser(userId);
    }

    public UserResponse getUserResponse(Long userId) {
        return userToResponse(selectUser(userId));
    }

    public User saveUser(User user) {
        return insertUser(user);
    }

    public UserResponse changeEmail(ChangeEmailRequest request, Long userId) {
        User user = selectUser(userId);
        user.setEmail(request.getEmail());
        return userToResponse(insertUser(user));
    }

    public UserResponse changePassword(ChangePasswordRequest request, Long userId) {
        User user = selectUser(userId);
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()) &&
                request.getNewPassword().equals(request.getRepeatNewPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            return userToResponse(insertUser(user));
        } else {
            throw new BadDataException(CHANGE_PASSWORD_BAD_CREDENTIALS);
        }
    }

    public void updateUsedSpace(Long userId, Long size) {
        User user = getUser(userId);
        user.setUsedSpace(user.getUsedSpace() + size);
        saveUser(user);
    }

    public List<UserResponse> usersToResponses(List<User> users) {
        return users.stream()
                .map(this::userToResponse)
                .collect(Collectors.toList());
    }

    public UserResponse userToResponse(User user) {
        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getUsedSpace(),
                subscriptionToSubscriptionResponseMapper.subscriptionToResponse(user.getSubscription())
        );
    }

    private User selectUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(NoContentException::new);
    }

    private User insertUser(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new InternalException(INSERT_USER_EXCEPTION);
        }
        return savedUser;
    }

    public void deleteUser(Long userId) {
        int result = userRepository.deleteUserById(userId);
        if (result == 0) {
            throw new InternalException(DELETE_USER_EXCEPTION);
        }
    }
}