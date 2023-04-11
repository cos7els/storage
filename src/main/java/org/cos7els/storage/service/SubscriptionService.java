package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse getCurrentSubscription(Long userId);

    List<Subscription> getSubscriptions();

    Subscription selectSubscription(Long subscriptionId);

    Subscription insertSubscription(Subscription subscription);

    void deleteSubscription(Long subscriptionId);

    void deleteSubscriptionByUserId(Long subscriptionId);
}