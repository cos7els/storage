package org.cos7els.subscriptionservice.dto;

import lombok.Data;
import org.cos7els.subscriptionservice.model.SubscriptionType;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class CreateSubscriptionDto {
    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Target User ID is required")
    private Long targetUserId;

    private SubscriptionType type = SubscriptionType.BASIC;

    @NotNull(message = "End date is required")
    private java.time.LocalDateTime endDate;

    @NotNull(message = "Price is required")
    private BigDecimal price;
}