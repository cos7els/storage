//package org.cos7els.storage.service;
//
//import org.cos7els.storage.model.domain.User;
//import org.cos7els.storage.repository.UserRepository;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UserServiceTest {
//    @Mock
//    private UserRepository userRepository;
//    @InjectMocks
//    private UserService userService;
//    private final Long id = 1L;
//    private final User user = User.builder().id(id).userName("test").password("test").email("test@test.org").build();
//
//    @Test
//    public void getUserTest() {
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        User returnedUser = userService.getUser(id).orElse(new User());
//        verify(userRepository).findById(id);
//        assertEquals(user, returnedUser);
//    }
//
//    @Test
//    public void getUsersTest() {
//        Iterable<User> list = List.of(new User(), new User(), new User());
//        when(userRepository.findAll()).thenReturn(list);
//        Iterable<User> users = userService.getUsers();
//        verify(userRepository).findAll();
//        assertEquals(list, users);
//    }
//
//    @Test
//    public void createUserTest() {
//        when(userService.createUser(user)).thenReturn(user);
//        User createdUser = userService.createUser(user);
//        verify(userRepository).save(user);
//        assertEquals(user, createdUser);
//    }
//
//    @Test
//    public void deleteUserTest() {
//        userService.deleteUser(id);
//        verify(userRepository).deleteById(id);
//    }
//
//    @Test
//    public void deleteUsersTest() {
//        userService.deleteUsers();
//        verify(userRepository).deleteAll();
//    }
//}