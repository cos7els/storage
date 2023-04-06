package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.CustomException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.service.PlanService;
import org.cos7els.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.cos7els.storage.util.ExceptionMessage.DELETE_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.PLANS_NOT_FOUND;
import static org.cos7els.storage.util.ExceptionMessage.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    public List<PlanResponse> getAllActivePlans() {
        List<Plan> plans = planRepository.getPlansByIsActiveTrueOrderById();
        if (plans == null || plans.isEmpty()) {
            throw new NotFoundException(PLANS_NOT_FOUND);
        }
        return plansToResponses(plans);
    }

    public List<PlanResponse> plansToResponses(List<Plan> plans) {
        return plans.stream()
                .map(this::planToResponse)
                .collect(Collectors.toList());
    }

    public PlanResponse planToResponse(Plan plan) {
        return new PlanResponse(
                plan.getTitle(),
                plan.getAvailableSpace(),
                plan.getMonthlyPrice(),
                plan.getYearlyPrice()
        );
    }

    public List<Plan> getAllPlans() {
        return selectAllPlans();
    }

    public Plan getPlan(Long id) {
        return selectPlan(id);
    }

    public Plan savePlan(Plan plan) {
        return insertPlan(plan);
    }

    public Plan updatePlan(Plan plan) {
        return insertPlan(plan);
    }

    public void deletePlan(Long id) {
        int result = planRepository.deletePlanById(id);
        if (result == 0) {
            throw new CustomException(DELETE_PLAN_EXCEPTION);
        }
    }

    private List<Plan> selectAllPlans() {
        List<Plan> plans = planRepository.findAll();
        if (plans == null || plans.isEmpty()) {
            throw new NotFoundException(PLANS_NOT_FOUND);
        }
        return plans;
    }

    private Plan selectPlan(Long id) {
        return planRepository.getPlanById(id)
                .orElseThrow(() -> new NotFoundException(PLAN_NOT_FOUND));
    }

    private Plan insertPlan(Plan plan) {
        Plan savedPlan = planRepository.save(plan);
        if (savedPlan == null) {
            throw new CustomException(INSERT_PLAN_EXCEPTION);
        }
        return savedPlan;
    }
}