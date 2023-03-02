package org.cos7els.storage.controller.request;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
    private Long planId;
}
