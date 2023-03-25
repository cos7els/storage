package org.cos7els.storage.model.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
}
