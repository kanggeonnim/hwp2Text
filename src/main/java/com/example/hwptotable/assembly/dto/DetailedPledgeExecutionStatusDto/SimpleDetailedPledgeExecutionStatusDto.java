package com.example.hwptotable.assembly.dto.DetailedPledgeExecutionStatusDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimpleDetailedPledgeExecutionStatusDto {
    private String pledgeName;
    private String pledgeSummary;
    private ClassificationDto classification;
    private String fulfillmentRate;
    private LegislativeDto legislation;
    private BudgetAmountDto budgetAmount;
    private String otherImplementationBasis;

//    public static SimpleDetailedPledgeExecutionStatusDto of(DetailedPledgeExecutionStatus pledge) {
//        return SimpleDetailedPledgeExecutionStatusDto.builder()
//                .pledgeName(pledge.getPledgeName())
//                .pledgeSummary(pledge.getPledgeSummary())
//                .classification(pledge.get)
//                .build();
//    }
}
