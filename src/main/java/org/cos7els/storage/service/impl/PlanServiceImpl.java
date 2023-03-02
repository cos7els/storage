package org.cos7els.storage.service.impl;

import org.cos7els.storage.model.Plan;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlanServiceImpl implements PlanService {
    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Optional<Plan> getPlan(Long id) {
        return planRepository.findById(id);
    }

    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    public Plan savePlan(Plan plan) {
        return planRepository.save(plan);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    public void deleteAllPlans() {
        planRepository.deleteAll();
    }

    public boolean isPlanExists(Long id) {
        return planRepository.existsById(id);
    }
}