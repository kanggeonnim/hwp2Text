package com.example.hwptotable.assembly.dto.DetailedPledgeExecutionStatusDto;

import com.example.hwptotable.assembly.dto.PledgeFulfillmentRateDto.PledgeFulfillmentRateDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetailedPledgeExecutionStatusDto {
    private Long id;
    private int turn;
    private String pledgeName;
    private String pledgeSummary;
    private ClassificationDto classification;
    private String fulfillmentRate;
    private LegislativeDto legislation;
    private BudgetAmountDto budgetAmount;
    private BudgetDetailsDto budgetDetails;
    private String otherImplementationBasis;
    private PledgeFulfillmentRateDto pledgeFulfillmentRate;
}
