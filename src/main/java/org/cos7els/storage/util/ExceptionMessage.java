package org.cos7els.storage.util;

public interface ExceptionMessage {
    final String NOT_FOUND = "The requested URL was not found on this server";
    String OBJECT_NOT_FOUND = "Object not found";
    String USERS_NOT_FOUND = "Users not found";
    String USER_NOT_FOUND = "User not found";
    String CREATE_USER_EXCEPTION = "Create user exception";
    String INSERT_USER_EXCEPTION = "Insert user exception";
    String DELETE_USER_EXCEPTION = "Delete user exception";

    String ALBUMS_NOT_FOUND = "Albums not found";
    String ALBUM_NOT_FOUND = "Album not found";
    String INSERT_ALBUM_EXCEPTION = "Insert album exception";
    String DELETE_ALBUM_EXCEPTION = "Delete album exception";

    String AUTHORITIES_NOT_FOUND = "Authorities not found";
    String AUTHORITY_NOT_FOUND = "Authority not found";
    String INSERT_AUTHORITY_EXCEPTION = "Insert authority exception";
    String DELETE_AUTHORITY_EXCEPTION = "Delete user exception";

    String PLANS_NOT_FOUND = "Plans not found";
    String PLAN_NOT_FOUND = "Plan not found";
    String INSERT_PLAN_EXCEPTION = "Insert plan exception";
    String DELETE_PLAN_EXCEPTION = "Delete plan exception";

    String SUBSCRIPTION_NOT_FOUND = "Subscription not found";
    String SUBSCRIPTIONS_NOT_FOUND = "Subscriptions not found";
    String CREATE_SUBSCRIPTION_EXCEPTION = "Create subscription exception";
    String UPDATE_SUBSCRIPTION_EXCEPTION = "Create subscription exception";
    String DELETE_SUBSCRIPTION_EXCEPTION = "Delete subscription exception";

    String PHOTOS_NOT_FOUND = "Photos not found";
    String PHOTO_NOT_FOUND = "Photo not found";
    String INSERT_PHOTO_EXCEPTION = "Insert photo exception";
    String DELETE_PHOTO_EXCEPTION = "Delete photo exception";

    String REGISTER_EXCEPTION = "Register user exception";
    String CHANGE_PASSWORD_EXCEPTION = "Change password exception";
    String CHANGE_PASSWORD_BAD_CREDENTIALS = "Passwords doesn't match";
    String CHANGE_EMAIL_EXCEPTION = "Change email exception";
}