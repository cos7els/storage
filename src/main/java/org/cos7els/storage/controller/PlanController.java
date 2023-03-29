package org.cos7els.storage.controller;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.User;
import org.cos7els.storage.model.response.PlanResponse;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.PLANS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.PLAN_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.CREATE_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.UPDATE_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.DELETE_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.USER_NOT_FOUND;

@RestController
@RequiredArgsConstructor
public class PlanController {
    private final PlanService planService;
    private final UserService userService;

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResponse>> getPlans() {
        List<Plan> plans = planService.getAllActivePlans()
                .orElseThrow(() -> new NotFoundException(PLANS_NOT_FOUND));
        return new ResponseEntity<>(
                planService.plansToResponses(plans),
                HttpStatus.OK
        );
    }

    @GetMapping("/plan")
    public ResponseEntity<PlanResponse> getPlan(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        User user = userService.getUser(userDetails.getId())
                .orElseThrow(() -> new NotFoundException(USER_NOT_FOUND));
        return new ResponseEntity<>(
                planService.planToResponse(user.getSubscription().getPlan()),
                HttpStatus.OK
        );
    }

    @GetMapping("/admin/plans")
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planService.getAllPlans()
                .orElseThrow(() -> new NotFoundException(PLANS_NOT_FOUND));
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @GetMapping("/admin/plan/{id}")
    public ResponseEntity<Plan> getPlan(@PathVariable Long id) {
        Plan plan = planService.getPlan(id)
                .orElseThrow(() -> new NotFoundException(PLAN_NOT_FOUND));
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @PostMapping("/admin/plan")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan request) {
        Plan plan = planService.savePlan(request)
                .orElseThrow(() -> new CustomException(CREATE_PLAN_EXCEPTION));
        return new ResponseEntity<>(plan, HttpStatus.CREATED);
    }

    @PutMapping("/admin/plan")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan request) {
        Plan plan = planService.savePlan(request)
                .orElseThrow(() -> new CustomException(UPDATE_PLAN_EXCEPTION));
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }

    @DeleteMapping("/admin/plan/{id}")
    public ResponseEntity<HttpStatus> deletePlan(@PathVariable Long id) {
        Integer result = planService.deletePlan(id);
        if (result == 0) {
            throw new CustomException(DELETE_PLAN_EXCEPTION);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}