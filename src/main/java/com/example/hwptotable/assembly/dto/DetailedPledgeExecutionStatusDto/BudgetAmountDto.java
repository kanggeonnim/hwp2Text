package com.example.hwptotable.assembly.dto.DetailedPledgeExecutionStatusDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetAmountDto {
    private String requiredBudgetAmount;
    private String securedBudgetAmount;
    private String executedBudgetAmount;
}
