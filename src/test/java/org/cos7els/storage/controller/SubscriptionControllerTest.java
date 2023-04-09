package org.cos7els.storage.controller;

import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionControllerTest {
    @Mock
    private SubscriptionService subscriptionService;
    @InjectMocks
    private SubscriptionController subscriptionController;
    UserDetailsImpl userDetails;
    SubscriptionResponse subscriptionResponse;

    @BeforeEach
    public void init() {
        userDetails = new UserDetailsImpl(1L, null, null, null);
        subscriptionResponse = new SubscriptionResponse();
    }

    @Test
    public void getCurrentSubscription() {
        when(subscriptionService.getCurrentSubscription(userDetails.getId())).thenReturn(subscriptionResponse);
        ResponseEntity<SubscriptionResponse> response = subscriptionController.getCurrentSubscription(userDetails);
        verify(subscriptionService).getCurrentSubscription(userDetails.getId());
        assertEquals(subscriptionResponse, response.getBody());
    }
}
