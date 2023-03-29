package org.cos7els.storage.repository;

import org.cos7els.storage.model.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    List<Plan> findPlansByIsActiveTrueOrderById();

    Integer deletePlanById(Long id);
}