package org.cos7els.subscriptionservice.dto;

import lombok.Data;
import org.cos7els.subscriptionservice.model.SubscriptionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SubscriptionDto {
    private Long id;
    private Long userId;
    private Long targetUserId;
    private SubscriptionType type;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal price;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}