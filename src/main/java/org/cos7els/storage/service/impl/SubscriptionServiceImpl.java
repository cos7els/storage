package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.repository.SubscriptionRepository;
import org.cos7els.storage.service.PlanService;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final PlanService planService;

    public Optional<List<Subscription>> getAllSubscription() {
        return Optional.of(subscriptionRepository.findAll());
    }

    public Optional<Subscription> getSubscription(Long id) {
        return subscriptionRepository.findById(id);
    }

    public Optional<Subscription> saveSubscription(Subscription subscription) {
        return Optional.of(subscriptionRepository.save(subscription));
    }

    public Integer deleteSubscription(Long id) {
        return subscriptionRepository.deleteSubscriptionById(id);
    }

    public SubscriptionResponse subscriptionToResponse(Subscription subscription) {
        return new SubscriptionResponse(
                planService.planToResponse(subscription.getPlan()),
                subscription.getIssuedDate(),
                subscription.getExpiredDate(),
                subscription.getIsActive()
        );
    }
}
