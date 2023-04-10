package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.ChangeEmailRequest;
import org.cos7els.storage.model.request.ChangePasswordRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private Long id;
    private User user;
    private List<User> users;
    private UserResponse userResponse;
    private UserDetailsImpl userDetails;
    private ChangeEmailRequest changeEmailRequest;
    private ChangePasswordRequest changePasswordRequest;
    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        user = new User();
        users = List.of(new User());
        userResponse = new UserResponse();
        userDetails = new UserDetailsImpl(1L, null, null, null);
        changeEmailRequest = new ChangeEmailRequest("test@test.com");
        changePasswordRequest = new ChangePasswordRequest("test", "test", "test");
        mockMvc = MockMvcBuilders
                .standaloneSetup(userController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getUserTest() throws Exception {
        when(userService.getUserResponse(userDetails.getId())).thenReturn(userResponse);
        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).getUserResponse(userDetails.getId());
    }

    @Test
    public void changeEmailTest() throws Exception {
        when(userService.changeEmail(changeEmailRequest, userDetails.getId())).thenReturn(userResponse);
        mockMvc.perform(put("/user/change/email")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(changeEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).changeEmail(changeEmailRequest, userDetails.getId());
    }

    @Test
    public void changePasswordTest() throws Exception {
        when(userService.changePassword(changePasswordRequest, userDetails.getId())).thenReturn(userResponse);
        mockMvc.perform(put("/user/change/password")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).changePassword(changePasswordRequest, userDetails.getId());
    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform(delete("/user"))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userService).deleteUser(userDetails.getId());
    }

    @Test
    public void getAllUsersTest() throws Exception {
        when(userService.getAllUsers()).thenReturn(users);
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).getAllUsers();
    }

    @Test
    public void getUserAdminTest() throws Exception {
        when(userService.getUser(id)).thenReturn(user);
        mockMvc.perform(get("/admin/user/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).getUser(id);
    }

    @Test
    public void createUserTest() throws Exception {
        when(userService.saveUser(user)).thenReturn(user);
        mockMvc.perform(post("/admin/user")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).saveUser(user);
    }

    @Test
    public void updateUserTest() throws Exception {
        when(userService.saveUser(user)).thenReturn(user);
        mockMvc.perform(put("/admin/user")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(userService).saveUser(user);
    }

    @Test
    public void deleteUserAdminTest() throws Exception {
        mockMvc.perform(delete("/admin/user/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(userService).deleteUser(id);
    }

    private final HandlerMethodArgumentResolver putUserDetails = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
        }

        @Override
        public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return userDetails;
        }
    };
}