package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.User;
import org.cos7els.storage.model.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserToUserResponseMapper {

    public UserResponse userToResponse(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setUsername(user.getUsername());
        userResponse.setEmail(user.getEmail());
        userResponse.setUsedSpace(user.getUsedSpace());
        return userResponse;
    }
}
