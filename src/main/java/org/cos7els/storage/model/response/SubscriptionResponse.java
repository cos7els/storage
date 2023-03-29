package org.cos7els.storage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionResponse {
    private PlanResponse plan;
    private LocalDate issuedDate;
    private LocalDate expiredDate;
    private Boolean isActive;
}