package org.cos7els.storage.mapper;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionToSubscriptionResponseMapper {
    private final PlanToPlanResponseMapper planToPlanResponseMapper;

    public SubscriptionResponse subscriptionToResponse(Subscription subscription) {
        return new SubscriptionResponse(planToPlanResponseMapper.planToResponse(subscription.getPlan()), subscription.getIssuedDate(), subscription.getExpiredDate(), subscription.getIsActive());
    }
}
