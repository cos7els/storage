package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.security.UserDetailsImpl;
import org.cos7els.storage.service.PlanService;
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

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        return new ResponseEntity<>(
                planService.getAllActivePlans(),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/plans")
    public ResponseEntity<List<Plan>> getAllPlans() {
        return new ResponseEntity<>(planService.getAllPlans(), HttpStatus.OK);
    }

    @GetMapping("/admin/plan/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        return new ResponseEntity<>(planService.getPlan(id), HttpStatus.OK);
    }

    @PostMapping("/admin/plan")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.savePlan(plan), HttpStatus.CREATED);
    }

    @PutMapping("/admin/plan")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.savePlan(plan), HttpStatus.OK);
    }

    @DeleteMapping("/admin/plan/{id}")
    public ResponseEntity<HttpStatus> deletePlan(@PathVariable Long id) {
        planService.deletePlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}