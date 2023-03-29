package org.cos7els.storage.service;

import org.cos7els.storage.model.Plan;
import org.cos7els.storage.model.response.PlanResponse;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    Optional<List<Plan>> getAllActivePlans();

    PlanResponse planToResponse(Plan plan);

    List<PlanResponse> plansToResponses(List<Plan> plans);

    Optional<List<Plan>> getAllPlans();

    Optional<Plan> getPlan(Long id);

    Optional<Plan> savePlan(Plan plan);

    Integer deletePlan(Long id);
}