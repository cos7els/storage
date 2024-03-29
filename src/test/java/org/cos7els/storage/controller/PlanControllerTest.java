package org.cos7els.storage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.cos7els.storage.security.model.UserDetailsImpl;
import org.cos7els.storage.service.PlanService;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PlanControllerTest {
    private MockMvc mockMvc;
    private ObjectWriter objectWriter;
    @Mock
    private PlanService planService;
    @InjectMocks
    private PlanController planController;
    private UserDetailsImpl userDetails;
    private Long id;
    private Plan plan;
    private List<Plan> plans;
    private List<PlanResponse> planResponses;
    @BeforeEach
    public void init() {
        objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        id = 1L;
        plan = new Plan();
        plans = List.of(new Plan());
        planResponses = List.of(new PlanResponse());
        userDetails = new UserDetailsImpl(id, null, null, null);
        mockMvc = MockMvcBuilders
                .standaloneSetup(planController)
                .setCustomArgumentResolvers(putUserDetails)
                .build();
    }

    @Test
    public void getActivePlansTest() throws Exception {
        when(planService.getActivePlans()).thenReturn(planResponses);
        mockMvc.perform(get("/plans"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(planService).getActivePlans();
    }

    @Test
    public void getPlansTest() throws Exception {
        when(planService.getAllPlans()).thenReturn(plans);
        mockMvc.perform(get("/admin/plans"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(planService).getAllPlans();
    }

    @Test
    public void getPlanTest() throws Exception {
        when(planService.getPlan(id)).thenReturn(plan);
        mockMvc.perform(get("/admin/plan/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(planService).getPlan(id);
    }

    @Test
    public void createPlanTest() throws Exception {
        when(planService.savePlan(plan)).thenReturn(plan);
        mockMvc.perform(post("/admin/plan")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(plan)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(planService).savePlan(plan);
    }

    @Test
    public void updatePlanTest() throws Exception {
        when(planService.savePlan(plan)).thenReturn(plan);
        mockMvc.perform(put("/admin/plan")
                        .contentType(APPLICATION_JSON)
                        .content(objectWriter.writeValueAsString(plan)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON))
                .andReturn();
        verify(planService).savePlan(plan);
    }

    @Test
    public void deletePlanTest() throws Exception {
        mockMvc.perform(delete("/admin/plan/{id}", id))
                .andExpect(status().isNoContent())
                .andReturn();
        verify(planService).deletePlan(id);
    }

    private final HandlerMethodArgumentResolver putUserDetails = new HandlerMethodArgumentResolver() {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return parameter.getParameterType().isAssignableFrom(UserDetailsImpl.class);
        }

        @Override
        public Object resolveArgument(@NotNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                      @NotNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
            return userDetails;
        }
    };
}