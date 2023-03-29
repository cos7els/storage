package org.cos7els.storage.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeSubscriptionRequest {
    private String planName;
    private LocalDate expiredDate;
}
