package org.cos7els.subscriptionservice.controller;

import org.cos7els.subscriptionservice.dto.CreateSubscriptionDto;
import org.cos7els.subscriptionservice.dto.SubscriptionDto;
import org.cos7els.subscriptionservice.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    @Autowired
    private SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionDto> createSubscription(@Valid @RequestBody CreateSubscriptionDto createSubscriptionDto) {
        SubscriptionDto subscriptionDto = subscriptionService.createSubscription(createSubscriptionDto);
        return new ResponseEntity<>(subscriptionDto, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubscriptionDto> getSubscriptionById(@PathVariable Long id) {
        SubscriptionDto subscriptionDto = subscriptionService.getSubscriptionById(id);
        return new ResponseEntity<>(subscriptionDto, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByUserId(@PathVariable Long userId) {
        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptionsByUserId(userId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/target/{targetUserId}")
    public ResponseEntity<List<SubscriptionDto>> getSubscriptionsByTargetUserId(@PathVariable Long targetUserId) {
        List<SubscriptionDto> subscriptions = subscriptionService.getSubscriptionsByTargetUserId(targetUserId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<SubscriptionDto>> getActiveSubscriptionsByUserId(@PathVariable Long userId) {
        List<SubscriptionDto> subscriptions = subscriptionService.getActiveSubscriptionsByUserId(userId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/target/{targetUserId}/active")
    public ResponseEntity<List<SubscriptionDto>> getActiveSubscriptionsByTargetUserId(@PathVariable Long targetUserId) {
        List<SubscriptionDto> subscriptions = subscriptionService.getActiveSubscriptionsByTargetUserId(targetUserId);
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<SubscriptionDto>> getAllSubscriptions() {
        List<SubscriptionDto> subscriptions = subscriptionService.getAllSubscriptions();
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionDto> updateSubscription(@PathVariable Long id, @Valid @RequestBody CreateSubscriptionDto updateSubscriptionDto) {
        SubscriptionDto subscriptionDto = subscriptionService.updateSubscription(id, updateSubscriptionDto);
        return new ResponseEntity<>(subscriptionDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long id) {
        subscriptionService.cancelSubscription(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}