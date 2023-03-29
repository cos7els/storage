package org.cos7els.storage.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanResponse {
    private String title;
    private Long availableSpace;
    private Double monthlyPrice;
    private Double yearlyPrice;
}