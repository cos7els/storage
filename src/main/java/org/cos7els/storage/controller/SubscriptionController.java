package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.domain.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/subscription")
    public ResponseEntity<SubscriptionResponse> getCurrentSubscription(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(subscriptionService.getCurrentSubscription(userDetails.getId()), HttpStatus.OK);
    }

    @GetMapping("/admin/subscriptions")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        return new ResponseEntity<>(subscriptionService.getSubscriptions(), HttpStatus.OK);
    }

    @GetMapping("/admin/subscription/{subscriptionId}")
    public ResponseEntity<Subscription> getCurrentSubscription(@PathVariable Long subscriptionId) {
        return new ResponseEntity<>(subscriptionService.selectSubscription(subscriptionId), HttpStatus.OK);
    }

    @PostMapping("/admin/subscription")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        return new ResponseEntity<>(subscriptionService.insertSubscription(subscription), HttpStatus.CREATED);
    }

    @PutMapping("admin/subscription")
    public ResponseEntity<Subscription> updateSubscription(@RequestBody Subscription subscription) {
        return new ResponseEntity<>(subscriptionService.insertSubscription(subscription), HttpStatus.OK);
    }

    @DeleteMapping("/admin/subscription/{subscriptionId}")
    public ResponseEntity<HttpStatus> deleteSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.deleteSubscription(subscriptionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}