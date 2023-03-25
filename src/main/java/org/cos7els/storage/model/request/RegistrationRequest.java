package org.cos7els.storage.model.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RegistrationRequest {
    private String username;
    private String password;
    private String email;
}
