package org.cos7els.storage.service;

import org.cos7els.storage.mapper.UserToUserResponseMapper;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.repository.UserRepository;
import org.cos7els.storage.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserToUserResponseMapper userToUserResponseMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SubscriptionService subscriptionService;
    @InjectMocks
    private UserServiceImpl userService;
    private Long id;
    private String email;
    private User user;
    private List<User> users;
    private UserResponse userResponse;
    private ChangeEmailRequest changeEmailRequest;
    private ChangePasswordRequest changePasswordRequest;

    @BeforeEach
    public void init() {
        id = 1L;
        email = "test@test.com";
        user = new User();
        users = List.of(new User());
        userResponse = new UserResponse();
        changeEmailRequest = new ChangeEmailRequest();
        changePasswordRequest = new ChangePasswordRequest();
    }

    @Test
    public void getUserResponseTest() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userToUserResponseMapper.userToResponse(user)).thenReturn(userResponse);
        UserResponse returned = userService.getUserResponse(id);
        verify(userRepository).findById(id);
        verify(userToUserResponseMapper).userToResponse(user);
        assertEquals(userResponse, returned);
    }

    @Test
    public void changeEmailTest() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        changeEmailRequest.setEmail(email);
        user.setEmail(email);
        when(userRepository.save(user)).thenReturn(user);
        when(userToUserResponseMapper.userToResponse(user)).thenReturn(userResponse);
        UserResponse returned = userService.changeEmail(changeEmailRequest, id);
        verify(userRepository).findById(id);
        verify(userRepository).save(user);
        verify(userToUserResponseMapper).userToResponse(user);
        assertEquals(userResponse, returned);
    }

    @Test
    public void changePasswordTest() {
        String oldPassword = "OldPassword2023";
        String newPassword = "NewPassword2023";
        changePasswordRequest.setOldPassword(oldPassword);
        changePasswordRequest.setNewPassword(newPassword);
        changePasswordRequest.setRepeatNewPassword(newPassword);
        user.setPassword(oldPassword);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(true);
        when(userRepository.save(user)).thenReturn(user);
        when(userToUserResponseMapper.userToResponse(user)).thenReturn(userResponse);
        UserResponse returned = userService.changePassword(changePasswordRequest, id);
        verify(userRepository).findById(id);
        verify(passwordEncoder).matches(oldPassword, oldPassword);
        verify(userRepository).save(user);
        verify(userToUserResponseMapper).userToResponse(user);
        assertEquals(userResponse, returned);
    }

    @Test
    public void getAllUsersTest() {
        when(userRepository.findAll()).thenReturn(users);
        List<User> returned = userService.getAllUsers();
        verify(userRepository).findAll();
        assertEquals(users, returned);
    }

    @Test
    public void getUserTest() {
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        User returned = userService.getUser(id);
        verify(userRepository).findById(id);
        assertEquals(user, returned);
    }

    @Test
    public void saveUserTest() {
        when(userRepository.save(user)).thenReturn(user);
        User returned = userService.saveUser(user);
        verify(userRepository).save(user);
        assertEquals(user, returned);
    }

    @Test
    public void deleteUserTest() {
        when(userRepository.deleteUserById(id)).thenReturn(id.intValue());
        userService.deleteUser(id);
        verify(userRepository).deleteUserById(id);
    }

    @Test
    public void updateUsedSpace() {
        user.setUsedSpace(id);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        userService.updateUsedSpace(id, id);
        verify(userRepository).findById(id);
        verify(userRepository).save(user);
    }

    @Test
    public void checkUsedSpaceTest() {
        user.setUsedSpace(id);
        PlanResponse planResponse = new PlanResponse();
        planResponse.setAvailableSpace(100L);
        SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
        subscriptionResponse.setPlan(planResponse);
        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(subscriptionService.getCurrentSubscription(id)).thenReturn(subscriptionResponse);
        userService.checkUsedSpace(id);
        verify(userRepository).findById(id);
        verify(subscriptionService).getCurrentSubscription(id);
    }
}