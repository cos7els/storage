package org.cos7els.storage.controller.response;

import lombok.Data;

@Data
public class AuthenticationResponse {
    private String username;
    private String token;
}
