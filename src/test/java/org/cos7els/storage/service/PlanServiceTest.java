package org.cos7els.storage.service;

import org.cos7els.storage.mapper.PlanToPlanResponseMapper;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.repository.PlanRepository;
import org.cos7els.storage.service.impl.PlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @Mock
    private PlanToPlanResponseMapper planToPlanResponseMapper;
    @Mock
    private PlanRepository planRepository;
    @InjectMocks
    private PlanServiceImpl planService;
    private Long id;
    private Plan plan;
    private List<Plan> plans;
    private List<PlanResponse> planResponses;

    @BeforeEach
    public void init() {
        id = 1L;
        plan = new Plan();
        plans = List.of(new Plan());
        planResponses = List.of(new PlanResponse());
    }

    @Test
    public void getActivePlansTest() {
        when(planRepository.getPlansByIsActiveTrueOrderById()).thenReturn(plans);
        when(planToPlanResponseMapper.plansToResponses(plans)).thenReturn(planResponses);
        List<PlanResponse> activePlans = planService.getActivePlans();
        verify(planRepository).getPlansByIsActiveTrueOrderById();
        verify(planToPlanResponseMapper).plansToResponses(plans);
        assertEquals(planResponses, activePlans);
    }

    @Test
    public void getAllPlansTest() {
        when(planRepository.findAll()).thenReturn(plans);
        List<Plan> returnedPlans = planService.getAllPlans();
        verify(planRepository).findAll();
        assertEquals(plans, returnedPlans);
    }

    @Test
    public void getPlanTest() {
        when(planRepository.getPlanById(id)).thenReturn(Optional.of(plan));
        Plan returnedPlan = planService.getPlan(id);
        verify(planRepository).getPlanById(id);
        assertEquals(plan, returnedPlan);
    }

    @Test
    public void savePlanTest() {
        when(planRepository.save(plan)).thenReturn(plan);
        Plan savedPlan = planService.savePlan(plan);
        verify(planRepository).save(plan);
        assertEquals(plan, savedPlan);
    }

    @Test
    public void deletePlanTest() {
        when(planRepository.deletePlanById(id)).thenReturn(id.intValue());
        planService.deletePlan(id);
        verify(planRepository).deletePlanById(id);
    }
}