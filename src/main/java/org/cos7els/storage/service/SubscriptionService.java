package org.cos7els.storage.service;

import org.cos7els.storage.model.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;

import java.util.List;
import java.util.Optional;

public interface SubscriptionService {
    Optional<Subscription> getSubscription(Long id);

    Optional<List<Subscription>> getAllSubscription();

    Optional<Subscription> saveSubscription(Subscription subscription);

    Integer deleteSubscription(Long id);

    SubscriptionResponse subscriptionToResponse(Subscription subscription);
}
