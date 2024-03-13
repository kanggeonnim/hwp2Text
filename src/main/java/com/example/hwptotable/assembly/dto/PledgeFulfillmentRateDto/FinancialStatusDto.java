package com.example.hwptotable.assembly.dto.PledgeFulfillmentRateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialStatusDto {
    private String totalRequiredBudget;
    private String totalSecuredBudget;
    private String totalExecutedBudget;
}
