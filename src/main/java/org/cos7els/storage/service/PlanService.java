package org.cos7els.storage.service;

import org.cos7els.storage.model.Plan;

import java.util.List;
import java.util.Optional;

public interface PlanService {
    Optional<Plan> getPlan(Long id);

    List<Plan> getAllPlans();

    Plan savePlan(Plan plan);

    void deletePlan(Long id);

    void deleteAllPlans();

    boolean isPlanExists(Long id);
}
