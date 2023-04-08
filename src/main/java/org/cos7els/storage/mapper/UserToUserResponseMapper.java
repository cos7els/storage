package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.response.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseMapper {
    private final SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper;

    @Autowired
    public UserToUserResponseMapper(SubscriptionToSubscriptionResponseMapper subscriptionToSubscriptionResponseMapper) {
        this.subscriptionToSubscriptionResponseMapper = subscriptionToSubscriptionResponseMapper;
    }

    public UserResponse userToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsedSpace(user.getUsedSpace());
        userResponse.setSubscription(subscriptionToSubscriptionResponseMapper.subscriptionToResponse(user.getSubscription()));
        return userResponse;
    }
}
