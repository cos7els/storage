package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.User;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.PlanService;
import org.cos7els.storage.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final UserService userService;

    @GetMapping("/plan")
    public ResponseEntity<Plan> getPlan(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Optional<User> user = userService.getUser(userDetails.getId());
        return user.isPresent() ?
                new ResponseEntity<>(user.get().getSubscription().getPlan(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        Optional<Plan> album = planService.getPlan(id);
        return album.isPresent() ?
                new ResponseEntity<>(album.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planService.getAllPlans();
        return plans.isEmpty() ?
                new ResponseEntity<>(HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.savePlan(plan), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Plan> updatePlan(@PathVariable Long id, @RequestBody Plan plan) {
        return planService.isPlanExists(id) ?
                new ResponseEntity<>(planService.savePlan(plan), HttpStatus.OK) :
                new ResponseEntity<>(planService.savePlan(plan), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/all")
    public ResponseEntity<HttpStatus> deleteAllPlans() {
        planService.deleteAllPlans();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}