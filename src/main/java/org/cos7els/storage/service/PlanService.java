package org.cos7els.storage.service;

import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;

import java.util.List;

public interface PlanService {
    List<PlanResponse> getActivePlans();

    List<Plan> getAllPlans();

    Plan getPlan(Long id);

    Plan savePlan(Plan plan);

    void deletePlan(Long id);
}