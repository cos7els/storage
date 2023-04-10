package org.cos7els.storage.controller;

import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

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