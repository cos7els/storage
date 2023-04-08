package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.service.PlanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getActivePlans() {
        return new ResponseEntity<>(planService.getActivePlans(), HttpStatus.OK);
    }

    @GetMapping("/admin/plans")
    public ResponseEntity<List<Plan>> getPlans() {
        return new ResponseEntity<>(planService.getAllPlans(), HttpStatus.OK);
    }

    @GetMapping("/admin/plan/{planId}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long planId) {
        return new ResponseEntity<>(planService.getPlan(planId), HttpStatus.OK);
    }

    @PostMapping("/admin/plan")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.savePlan(plan), HttpStatus.CREATED);
    }

    @PutMapping("/admin/plan")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.savePlan(plan), HttpStatus.OK);
    }

    @DeleteMapping("/admin/plan/{planId}")
    public ResponseEntity<HttpStatus> deletePlan(@PathVariable Long planId) {
        planService.deletePlan(planId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}