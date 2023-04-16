package org.cos7els.storage.util;

public interface ExceptionMessage {
    String EXCEPTION_MESSAGE = "Something went wrong";
    String VALIDATION_MESSAGE = "Incorrect data";
    String USERNAME_OR_EMAIL_EXISTS = "User with  the same username or email exists";
    String NO_AVAILABLE_SPACE = "You're don't have available space on your storage";
    String NOT_FOUND = "The requested URL was not found on this server";
    String OBJECT_NOT_FOUND = "Object not found";
    String USER_NOT_FOUND = "User not found";
    String INSERT_USER_EXCEPTION = "Insert user exception";
    String DELETE_USER_EXCEPTION = "Delete user exception";
    String ALBUM_NOT_FOUND = "Album not found";
    String INSERT_ALBUM_EXCEPTION = "Insert album exception";
    String DELETE_ALBUM_EXCEPTION = "Delete album exception";
    String AUTHORITY_NOT_FOUND = "Authority not found";
    String INSERT_AUTHORITY_EXCEPTION = "Insert authority exception";
    String DELETE_AUTHORITY_EXCEPTION = "Delete user exception";
    String PLAN_NOT_FOUND = "Plan not found";
    String INSERT_PLAN_EXCEPTION = "Insert plan exception";
    String DELETE_PLAN_EXCEPTION = "Delete plan exception";
    String SUBSCRIPTION_NOT_FOUND = "Subscription not found";
    String INSERT_SUBSCRIPTION_EXCEPTION = "Create subscription exception";
    String DELETE_SUBSCRIPTION_EXCEPTION = "Delete subscription exception";
    String INSERT_PHOTO_EXCEPTION = "Insert photo exception";
    String CHANGE_PASSWORD_BAD_CREDENTIALS = "Passwords doesn't match";
    String PUT_OBJECT_EXCEPTION = "Put object exception";
    String GET_OBJECT_EXCEPTION = "Get object exception";
    String LIST_OBJECT_EXCEPTION = "List object exception";
    String GENERATE_THUMBNAIL_EXCEPTION = "Generate thumbnail exception";
    String REMOVE_OBJECT_EXCEPTION = "Remove object exception";
}