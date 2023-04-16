package org.cos7els.storage.service.impl;

import org.cos7els.storage.exception.BadDataException;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NoAvailableSpaceException;
import org.cos7els.storage.exception.NoContentException;
import org.cos7els.storage.mapper.UserToUserResponseMapper;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.StorageService;
import org.cos7els.storage.service.SubscriptionService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_BAD_CREDENTIALS;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.NO_AVAILABLE_SPACE;

@Service
public class UserServiceImpl implements UserService {
    private final UserToUserResponseMapper userToUserResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final SubscriptionService subscriptionService;
    private final StorageService storageService;

    @Autowired
    public UserServiceImpl(
            UserToUserResponseMapper userToUserResponseMapper,
            PasswordEncoder passwordEncoder,
            UserRepository userRepository,
            SubscriptionService subscriptionService,
            StorageService storageService
    ) {
        this.userToUserResponseMapper = userToUserResponseMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.subscriptionService = subscriptionService;
        this.storageService = storageService;
    }

    public UserResponse getUserResponse(Long userId) {
        return userToUserResponseMapper.userToResponse(selectUser(userId));
    }

    public UserResponse changeEmail(ChangeEmailRequest request, Long userId) {
        User user = selectUser(userId);
        user.setEmail(request.getEmail());
        return userToUserResponseMapper.userToResponse(insertUser(user));
    }

    public UserResponse changePassword(ChangePasswordRequest request, Long userId) {
        User user = selectUser(userId);
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()) &&
                request.getNewPassword().equals(request.getRepeatNewPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            return userToUserResponseMapper.userToResponse(insertUser(user));
        } else {
            throw new BadDataException(CHANGE_PASSWORD_BAD_CREDENTIALS);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return selectUser(userId);
    }

    public User saveUser(User user) {
        return insertUser(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        storageService.removePhotos(selectUser(userId).getUsername());
        int result = userRepository.deleteUserById(userId);
        if (result == 0) {
            throw new InternalException(DELETE_USER_EXCEPTION);
        }
    }

    public void updateUsedSpace(Long userId, Long size) {
        User user = selectUser(userId);
        user.setUsedSpace(user.getUsedSpace() + size);
        saveUser(user);
    }

    public void checkUsedSpace(Long userId) {
        User user = getUser(userId);
        if (user.getUsedSpace() >= subscriptionService.getCurrentSubscription(userId).getPlan().getAvailableSpace()) {
            throw new NoAvailableSpaceException(NO_AVAILABLE_SPACE);
        }
    }

    public boolean isUserExists(String username) {
        return userRepository.existsUserByUsername(username);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsUserByEmail(email);
    }

    private User selectUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(NoContentException::new);
    }

    private User insertUser(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new InternalException(INSERT_USER_EXCEPTION);
        }
        return savedUser;
    }
}