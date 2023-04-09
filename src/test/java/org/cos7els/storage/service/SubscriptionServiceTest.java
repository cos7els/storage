package org.cos7els.storage.service;

import org.cos7els.storage.mapper.SubscriptionToSubscriptionResponseMapper;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.repository.SubscriptionRepository;
import org.cos7els.storage.service.impl.SubscriptionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    private SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper;
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;
    private Long id;
    private Subscription subscription;
    private List<Subscription> subscriptions;
    private SubscriptionResponse subscriptionResponse;

    @BeforeEach
    public void init() {
        id = 1L;
        subscription = new Subscription();
        subscriptions = List.of(new Subscription());
        subscriptionResponse = new SubscriptionResponse();
    }

    @Test
    public void getCurrentSubscriptionTest() {
        when(subscriptionRepository.getSubscriptionByUserId(id)).thenReturn(Optional.of(subscription));
        when(subscriptionToSubscriptionResponseMapper.subscriptionToResponse(subscription)).thenReturn(subscriptionResponse);
        SubscriptionResponse returned = subscriptionService.getCurrentSubscription(id);
        verify(subscriptionRepository).getSubscriptionByUserId(id);
        verify(subscriptionToSubscriptionResponseMapper).subscriptionToResponse(subscription);
        assertEquals(subscriptionResponse, returned);
    }

    @Test
    public void getSubscriptionsTest() {
        when(subscriptionRepository.findAll()).thenReturn(subscriptions);
        List<Subscription> returned = subscriptionService.getSubscriptions();
        verify(subscriptionRepository).findAll();
        assertEquals(subscriptions, returned);
    }

    @Test
    public void selectSubscriptionTest() {
        when(subscriptionRepository.findById(id)).thenReturn(Optional.of(subscription));
        Subscription returned = subscriptionService.selectSubscription(id);
        verify(subscriptionRepository).findById(id);
        assertEquals(subscription, returned);
    }

    @Test
    public void selectCurrentSubscriptionTest() {
        when(subscriptionRepository.getSubscriptionByUserId(id)).thenReturn(Optional.of(subscription));
        Subscription returned = subscriptionService.selectCurrentSubscription(id);
        verify(subscriptionRepository).getSubscriptionByUserId(id);
        assertEquals(subscription, returned);
    }

    @Test
    public void insertSubscriptionTest() {
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);
        Subscription returned = subscriptionService.insertSubscription(subscription);
        verify(subscriptionRepository).save(subscription);
        assertEquals(subscription, returned);
    }

    @Test
    public void deleteSubscriptionTest() {
        when(subscriptionRepository.deleteSubscriptionById(id)).thenReturn(id.intValue());
        subscriptionService.deleteSubscription(id);
        verify(subscriptionRepository).deleteSubscriptionById(id);
    }
}