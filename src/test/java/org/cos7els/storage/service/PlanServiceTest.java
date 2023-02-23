package org.cos7els.storage.service;

import org.cos7els.storage.model.Plan;
import org.cos7els.storage.repository.PlanRepository;
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
    private PlanRepository planRepository;
    @InjectMocks
    private PlanService planService;
    private final Plan plan = new Plan(1L, "test", 0.0D, 0.0D, 0.0D);
    private final Long id = 1L;

    @Test
    public void getPlanTest() {
        when(planRepository.findById(id)).thenReturn(Optional.of(plan));
        Plan returnedPlan = planService.getPlan(id).orElse(new Plan());
        verify(planRepository).findById(id);
        assertEquals(plan, returnedPlan);
    }

    @Test
    public void getPlansTest() {
        Iterable<Plan> list = List.of(new Plan(), new Plan(), new Plan());
        when(planRepository.findAll()).thenReturn(list);
        Iterable<Plan> plans = planService.getPlans();
        verify(planRepository).findAll();
        assertEquals(list, plans);
    }

    @Test
    public void createPlanTest() {
        when(planService.createPlan(plan)).thenReturn(plan);
        Plan createdPlan = planService.createPlan(plan);
        verify(planRepository).save(plan);
        assertEquals(plan, createdPlan);
    }

    @Test
    public void deletePlanTest() {
        planService.deletePlan(id);
        verify(planRepository).deleteById(id);
    }

    @Test
    public void deletePlansTest() {
        planService.deletePlans();
        verify(planRepository).deleteAll();
    }
}
