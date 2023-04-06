package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.BadDataException;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.repository.AlbumRepository;
import org.cos7els.storage.repository.PhotoRepository;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.PhotoService;
import org.cos7els.storage.service.SubscriptionService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.CHANGE_PASSWORD_BAD_REQUEST;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_USER_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.USERS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final AlbumRepository albumRepository;
    private final PasswordEncoder passwordEncoder;
    private final SubscriptionService subscriptionService;

    public List<User> getAllUsers() {
        return selectAllUsers();
    }

    public User getUser(Long id) {
        return selectUser(id);
    }

    public UserResponse getUserResponse(Long id) {
        return userToResponse(selectUser(id));
    }

    public User saveUser(User user) {
        return insertUser(user);
    }

    public UserResponse changePassword(ChangePasswordRequest request, Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()) &&
                request.getNewPassword().equals(request.getRepeatNewPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            return userToResponse(insertUser(user));
        } else {
            throw new BadDataException(CHANGE_PASSWORD_BAD_REQUEST);
        }
    }

    public UserResponse changeEmail(ChangeEmailRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        user.setEmail(request.getEmail());
        return userToResponse(insertUser(user));
    }

    public void updateUsedSpace(Long id, Long size) {
        User user = getUser(id);
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
                subscriptionService.subscriptionToResponse(user.getSubscription())
        );
    }

    private List<User> selectAllUsers() {
        List<User> users = userRepository.findAll();
        if (users == null || users.isEmpty()) {
            throw new NotFoundException(USERS_NOT_FOUND);
        }
        return users;
    }

    private User selectUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
    }

    private User insertUser(User user) {
        User savedUser = userRepository.save(user);
        if (savedUser == null) {
            throw new CustomException(UPDATE_USER_EXCEPTION);
        }
        return savedUser;
    }

    @Transactional
    public void deleteUser(Long id) {
        photoRepository.deletePhotosByUserId(id);
        albumRepository.deleteAlbumsByUserId(id);
        int delUserResult = userRepository.deleteUserById(id);
        if (delUserResult == 0) {
            throw new CustomException(DELETE_USER_EXCEPTION);
        }
    }
}