package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.request.AuthenticationRequest;
import org.cos7els.storage.model.response.AuthenticationResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.AuthenticationService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AuthenticationService authenticationService;
    @InjectMocks
    private AuthenticationController authenticationController;
    private ObjectWriter objectWriter;
    AuthenticationResponse authenticationResponse;
    AuthenticationRequest authenticationRequest;

    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        authenticationResponse = new AuthenticationResponse();
        authenticationRequest = new AuthenticationRequest("test", "test");
        mockMvc = MockMvcBuilders
                .standaloneSetup(authenticationController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void authenticate() throws Exception {
        when(authenticationService.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(authenticationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(authenticationService).authenticate(authenticationRequest);
    }

    private final HandlerMethodArgumentResolver putUserDetails = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
        }

        @Override
        public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return new UserDetailsImpl(1L, null, null, null);
        }
    };
}