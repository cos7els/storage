package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseMapper {

    public UserResponse userToResponse(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .usedSpace(user.getUsedSpace())
                .build();
    }
}
