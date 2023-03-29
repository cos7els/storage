package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Override
    public Optional<List<Plan>> getAllActivePlans() {
        return Optional.of(planRepository.findPlansByIsActiveTrueOrderById());
    }

    @Override
    public PlanResponse planToResponse(Plan plan) {
        return new PlanResponse(
                plan.getTitle(),
                plan.getAvailableSpace(),
                plan.getMonthlyPrice(),
                plan.getYearlyPrice()
        );
    }

    @Override
    public List<PlanResponse> plansToResponses(List<Plan> plans) {
        return plans.stream()
                .map(this::planToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<List<Plan>> getAllPlans() {
        return Optional.of(planRepository.findAll());
    }

    @Override
    public Optional<Plan> getPlan(Long id) {
        return planRepository.findById(id);
    }

    @Override
    public Optional<Plan> savePlan(Plan plan) {
        return Optional.of(planRepository.save(plan));
    }

    @Override
    public Integer deletePlan(Long id) {
        return planRepository.deletePlanById(id);
    }
}