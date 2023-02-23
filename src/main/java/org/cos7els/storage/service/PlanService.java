package org.cos7els.storage.service;

import org.cos7els.storage.model.Plan;
import org.cos7els.storage.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlanService {
    private final PlanRepository planRepository;

    @Autowired
    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Optional<Plan> getPlan(long id) {
        return planRepository.findById(id);
    }

    public Iterable<Plan> getPlans() {
        return planRepository.findAll();
    }

    public Plan createPlan(Plan plan) {
        return planRepository.save(plan);
    }

    public void deletePlan(long id) {
        planRepository.deleteById(id);
    }

    public void deletePlans() {
        planRepository.deleteAll();
    }
}
