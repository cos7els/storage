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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/plan")
public class PlanController {
    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
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