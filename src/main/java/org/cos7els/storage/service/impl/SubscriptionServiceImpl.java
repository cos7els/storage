package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.Subscription;
import org.cos7els.storage.repository.SubscriptionRepository;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    public Subscription saveSubscription(Subscription subscription) {
        return subscriptionRepository.save(subscription);
    }
}
