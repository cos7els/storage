package org.cos7els.storage.controller;

import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.service.PlanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlanControllerTest {
    @Mock
    private PlanService planService;
    @InjectMocks
    private PlanController planController;
    List<PlanResponse> plans;

    @BeforeEach
    public void init() {
        plans = List.of(new PlanResponse());
    }

    @Test
    public void getActivePlansTest() {
        when(planService.getActivePlans()).thenReturn(plans);
        ResponseEntity<List<PlanResponse>> response = planController.getActivePlans();
        verify(planService).getActivePlans();
        assertEquals(plans, response.getBody());
    }
}
