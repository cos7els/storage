package org.cos7els.storage.controller.request;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}
