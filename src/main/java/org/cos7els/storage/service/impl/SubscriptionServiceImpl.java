package org.cos7els.storage.service.impl;

import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.SubscriptionToSubscriptionResponseMapper;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.repository.SubscriptionRepository;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.DELETE_SUBSCRIPTION_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_SUBSCRIPTION_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.SUBSCRIPTION_NOT_FOUND;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionServiceImpl(
            SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper,
            SubscriptionRepository subscriptionRepository
    ) {
        this.subscriptionToSubscriptionResponseMapper = subscriptionToSubscriptionResponseMapper;
        this.subscriptionRepository = subscriptionRepository;
    }

    public SubscriptionResponse getCurrentSubscription(Long userId) {
        return subscriptionToSubscriptionResponseMapper.subscriptionToResponse(selectCurrentSubscription(userId));
    }

    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription selectSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_NOT_FOUND));
    }

    public Subscription selectCurrentSubscription(Long userId) {
        return subscriptionRepository.getSubscriptionByUserId(userId)
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_NOT_FOUND));
    }

    public Subscription insertSubscription(Subscription subscription) {
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        if (savedSubscription == null) {
            throw new InternalException(INSERT_SUBSCRIPTION_EXCEPTION);
        }
        return savedSubscription;
    }

    public void deleteSubscription(Long subscriptionId) {
        int result = subscriptionRepository.deleteSubscriptionById(subscriptionId);
        if (result == 0) {
            throw new InternalException(DELETE_SUBSCRIPTION_EXCEPTION);
        }
    }
}