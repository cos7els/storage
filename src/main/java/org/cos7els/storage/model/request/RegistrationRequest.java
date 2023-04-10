package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    @Size(min = 4, max = 50)
    private String username;
    @Size(min = 4, max = 50)
    private String password;
    @Email
    private String email;
}