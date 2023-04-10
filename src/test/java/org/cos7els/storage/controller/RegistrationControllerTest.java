package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.request.RegistrationRequest;
import org.cos7els.storage.model.response.UserResponse;
import org.cos7els.storage.service.RegistrationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RegistrationControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private RegistrationService registrationService;
    @InjectMocks
    private RegistrationController registrationController;
    private User user;
    private UserResponse userResponse;
    private RegistrationRequest registrationRequest;

    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        user = new User();
        userResponse = new UserResponse();
        registrationRequest = new RegistrationRequest("test", "test", "test@test.com");
        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .build();
    }

    @Test
    public void registerUserTest() throws Exception {
        when(registrationService.registerUser(registrationRequest)).thenReturn(userResponse);
        mockMvc.perform(post("/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        verify(registrationService).registerUser(registrationRequest);
    }

    @Test
    public void registerAdminTest() throws Exception {
        when(registrationService.registerAdmin(registrationRequest)).thenReturn(user);
        mockMvc.perform(post("/admin/signup")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON));
        verify(registrationService).registerAdmin(registrationRequest);
    }
}