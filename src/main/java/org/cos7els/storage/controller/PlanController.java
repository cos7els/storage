package org.cos7els.storage.controller;

import org.cos7els.storage.model.Plan;
import org.cos7els.storage.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable long id) {
        Optional<Plan> plan = planService.getPlan(id);
        return plan.isPresent() ?
                new ResponseEntity<>(plan.get(), HttpStatus.OK) :
                new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<Iterable<Plan>> getPlans() {
        return new ResponseEntity<>(planService.getPlans(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        return new ResponseEntity<>(planService.createPlan(plan), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable long id) {
        planService.deletePlan(id);
    }

    @DeleteMapping
    public void deletePlans() {
        planService.deletePlans();
    }
}
