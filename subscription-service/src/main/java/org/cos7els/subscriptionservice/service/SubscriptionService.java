package org.cos7els.subscriptionservice.service;

import org.cos7els.subscriptionservice.dto.CreateSubscriptionDto;
import org.cos7els.subscriptionservice.dto.SubscriptionDto;
import org.cos7els.subscriptionservice.model.Subscription;
import org.cos7els.subscriptionservice.model.SubscriptionType;
import org.cos7els.subscriptionservice.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    public SubscriptionDto createSubscription(CreateSubscriptionDto createSubscriptionDto) {
        // Проверяем, не существует ли уже активной подписки на этого пользователя
        List<Subscription> existingSubscriptions = subscriptionRepository
                .findByUserIdAndTargetUserId(createSubscriptionDto.getUserId(), createSubscriptionDto.getTargetUserId());
        
        for (Subscription sub : existingSubscriptions) {
            if (sub.getIsActive() && sub.getEndDate().isAfter(LocalDateTime.now())) {
                // Если активная подписка все еще действует, обновляем дату окончания
                sub.setEndDate(createSubscriptionDto.getEndDate());
                sub.setPrice(createSubscriptionDto.getPrice());
                sub.setType(createSubscriptionDto.getType() != null ? createSubscriptionDto.getType() : SubscriptionType.BASIC);
                Subscription updatedSubscription = subscriptionRepository.save(sub);
                return convertToDto(updatedSubscription);
            }
        }

        // Создаем новую подписку
        Subscription subscription = new Subscription();
        subscription.setUserId(createSubscriptionDto.getUserId());
        subscription.setTargetUserId(createSubscriptionDto.getTargetUserId());
        subscription.setType(createSubscriptionDto.getType() != null ? createSubscriptionDto.getType() : SubscriptionType.BASIC);
        subscription.setStartDate(LocalDateTime.now());
        subscription.setEndDate(createSubscriptionDto.getEndDate());
        subscription.setPrice(createSubscriptionDto.getPrice());
        subscription.setIsActive(true);

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(savedSubscription);
    }

    public SubscriptionDto getSubscriptionById(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        return convertToDto(subscription);
    }

    public List<SubscriptionDto> getSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findByUserId(userId);
        return subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SubscriptionDto> getSubscriptionsByTargetUserId(Long targetUserId) {
        List<Subscription> subscriptions = subscriptionRepository.findByTargetUserId(targetUserId);
        return subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SubscriptionDto> getActiveSubscriptionsByUserId(Long userId) {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByUserId(userId, LocalDateTime.now());
        return subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SubscriptionDto> getActiveSubscriptionsByTargetUserId(Long targetUserId) {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByTargetUserId(targetUserId, LocalDateTime.now());
        return subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<SubscriptionDto> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public SubscriptionDto updateSubscription(Long id, CreateSubscriptionDto updateSubscriptionDto) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));

        subscription.setUserId(updateSubscriptionDto.getUserId());
        subscription.setTargetUserId(updateSubscriptionDto.getTargetUserId());
        subscription.setType(updateSubscriptionDto.getType() != null ? updateSubscriptionDto.getType() : SubscriptionType.BASIC);
        subscription.setEndDate(updateSubscriptionDto.getEndDate());
        subscription.setPrice(updateSubscriptionDto.getPrice());

        Subscription updatedSubscription = subscriptionRepository.save(subscription);
        return convertToDto(updatedSubscription);
    }

    public void cancelSubscription(Long id) {
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription not found with id: " + id));
        
        subscription.setIsActive(false);
        subscriptionRepository.save(subscription);
    }

    private SubscriptionDto convertToDto(Subscription subscription) {
        SubscriptionDto dto = new SubscriptionDto();
        dto.setId(subscription.getId());
        dto.setUserId(subscription.getUserId());
        dto.setTargetUserId(subscription.getTargetUserId());
        dto.setType(subscription.getType());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setPrice(subscription.getPrice());
        dto.setIsActive(subscription.getIsActive());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());
        return dto;
    }
}