package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.SubscriptionToSubscriptionResponseMapper;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.repository.SubscriptionRepository;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.SUBSCRIPTION_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_SUBSCRIPTION_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_SUBSCRIPTION_EXCEPTION;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionResponse getCurrentSubscription(Long subscriptionId) {
        return subscriptionToSubscriptionResponseMapper.subscriptionToResponse(selectSubscription(subscriptionId));
    }

    public List<Subscription> getSubscriptions() {
        return subscriptionRepository.findAll();
    }

    public Subscription selectSubscription(Long subscriptionId) {
        return subscriptionRepository.findById(subscriptionId)
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