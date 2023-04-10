package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {
    @Size(min = 4, max = 50)
    private String oldPassword;
    @Size(min = 4, max = 50)
    private String newPassword;
    @Email
    private String repeatNewPassword;
}