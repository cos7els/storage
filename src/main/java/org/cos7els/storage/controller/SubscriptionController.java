package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Subscription;
import org.cos7els.storage.model.response.SubscriptionResponse;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.CREATE_SUBSCRIPTION_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_SUBSCRIPTION_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.SUBSCRIPTIONS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.SUBSCRIPTION_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_SUBSCRIPTION_EXCEPTION;

@RestController
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @GetMapping("/subscription")
    public ResponseEntity<SubscriptionResponse> getSubscription(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Subscription subscription = subscriptionService.getSubscription(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_NOT_FOUND));
        return new ResponseEntity<>(
                subscriptionService.subscriptionToResponse(subscription),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/subscriptions")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscription()
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTIONS_NOT_FOUND));
        return new ResponseEntity<>(subscriptions, HttpStatus.OK);
    }

    @GetMapping("/admin/subscription/{id}")
    public ResponseEntity<Subscription> getSubscription(@PathVariable Long id) {
        Subscription subscription = subscriptionService.getSubscription(id)
                .orElseThrow(() -> new NotFoundException(SUBSCRIPTION_NOT_FOUND));
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @PostMapping("/admin/subscription")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription request) {
        Subscription subscription = subscriptionService.saveSubscription(request)
                .orElseThrow(() -> new InternalException(CREATE_SUBSCRIPTION_EXCEPTION));
        return new ResponseEntity<>(subscription, HttpStatus.CREATED);
    }

    @PutMapping("admin/subscription")
    public ResponseEntity<Subscription> updateSubscription(@RequestBody Subscription request) {
        Subscription subscription = subscriptionService.saveSubscription(request)
                .orElseThrow(() -> new InternalException(UPDATE_SUBSCRIPTION_EXCEPTION));
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @DeleteMapping("/admin/subscription/{id}")
    public ResponseEntity<HttpStatus> deleteSubscription(@PathVariable Long id) {
        Integer result = subscriptionService.deleteSubscription(id);
        if (result == 0) {
            throw new InternalException(DELETE_SUBSCRIPTION_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}