package org.cos7els.storage.mapper;

import org.cos7els.storage.model.domain.Plan;
import org.cos7els.storage.model.response.PlanResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanToPlanResponseMapper {
    public List<PlanResponse> plansToResponses(List<Plan> plans) {
        return plans.stream().map(this::planToResponse).collect(Collectors.toList());
    }

    public PlanResponse planToResponse(Plan plan) {
        return new PlanResponse(plan.getTitle(), plan.getAvailableSpace(), plan.getMonthlyPrice(), plan.getYearlyPrice());
    }
}
