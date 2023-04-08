package org.cos7els.storage.service.impl;

import lombok.RequiredArgsConstructor;
import org.cos7els.storage.exception.InternalException;
import org.cos7els.storage.exception.NotFoundException;
import org.cos7els.storage.mapper.PlanToPlanResponseMapper;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.service.PlanService;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.cos7els.storage.util.ExceptionMessage.DELETE_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.INSERT_PLAN_EXCEPTION;
import static org.cos7els.storage.util.ExceptionMessage.PLAN_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {
    private final PlanToPlanResponseMapper planToPlanResponseMapper;
    private final PlanRepository planRepository;

    public List<PlanResponse> getActivePlans() {
        List<Plan> plans = planRepository.getPlansByIsActiveTrueOrderById();
        return planToPlanResponseMapper.plansToResponses(plans);
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
            throw new InternalException(DELETE_PLAN_EXCEPTION);
        }
    }

    private List<Plan> selectAllPlans() {
        return planRepository.findAll();
    }

    private Plan selectPlan(Long id) {
        return planRepository.getPlanById(id)
                .orElseThrow(() -> new NotFoundException(PLAN_NOT_FOUND));
    }

    private Plan insertPlan(Plan plan) {
        Plan savedPlan = planRepository.save(plan);
        if (savedPlan == null) {
            throw new InternalException(INSERT_PLAN_EXCEPTION);
        }
        return savedPlan;
    }
}