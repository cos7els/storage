package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionToSubscriptionResponseMapper {
    private final PlanToPlanResponseMapper planToPlanResponseMapper;

    @Autowired
    public SubscriptionToSubscriptionResponseMapper(PlanToPlanResponseMapper planToPlanResponseMapper) {
        this.planToPlanResponseMapper = planToPlanResponseMapper;
    }

    public SubscriptionResponse subscriptionToResponse(Subscription subscription) {
        return new SubscriptionResponse(
                planToPlanResponseMapper
                        .planToResponse(
                                subscription
                                        .getPlan()
                        ),
                subscription.getIssuedDate(),
                subscription.getExpiredDate(),
                subscription.getIsActive()
        );
    }
}