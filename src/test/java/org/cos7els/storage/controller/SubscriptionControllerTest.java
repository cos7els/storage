package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.SubscriptionService;
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
public class SubscriptionControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private SubscriptionService subscriptionService;
    @InjectMocks
    private SubscriptionController subscriptionController;
    private Long id;
    private Subscription subscription;
    private List<Subscription> subscriptions;
    private UserDetailsImpl userDetails;
    private SubscriptionResponse subscriptionResponse;
    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        subscription = new Subscription();
        subscriptions = List.of(new Subscription());
        userDetails = new UserDetailsImpl(1L, null, null, null);
        subscriptionResponse = new SubscriptionResponse();
        mockMvc = MockMvcBuilders
                .standaloneSetup(subscriptionController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getCurrentSubscription() throws Exception {
        when(subscriptionService.getCurrentSubscription(userDetails.getId())).thenReturn(subscriptionResponse);
        mockMvc.perform(get("/subscription"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(subscriptionService).getCurrentSubscription(userDetails.getId());
    }

    @Test
    public void getAllSubscriptionsTest() throws Exception {
        when(subscriptionService.getSubscriptions()).thenReturn(subscriptions);
        mockMvc.perform(get("/admin/subscriptions"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(subscriptionService).getSubscriptions();
    }

    @Test
    public void getSubscriptionTest() throws Exception {
        when(subscriptionService.selectSubscription(id)).thenReturn(subscription);
        mockMvc.perform(get("/admin/subscription/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(subscriptionService).selectSubscription(id);
    }

    @Test
    public void createSubscriptionTest() throws Exception {
        when(subscriptionService.insertSubscription(subscription)).thenReturn(subscription);
        mockMvc.perform(post("/admin/subscription")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(subscription)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(subscriptionService).insertSubscription(subscription);
    }

    @Test
    public void updateSubscriptionTest() throws Exception {
        when(subscriptionService.insertSubscription(subscription)).thenReturn(subscription);
        mockMvc.perform(put("/admin/subscription")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(subscription)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(subscriptionService).insertSubscription(subscription);
    }

    @Test
    public void deleteSubscriptionTest() throws Exception {
        mockMvc.perform(delete("/admin/subscription/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(subscriptionService).deleteSubscription(id);
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