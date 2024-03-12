package com.example.hwptotable.manifesto.Dto.PledgeFulfillmentRateDto;

import com.example.hwptotable.manifesto.Dto.DetailedPledgeExecutionStatusDto.DetailedPledgeExecutionStatusDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PledgeFulfillmentRateDto {
    private Long id;
    private String legislatorName;
    private String standingCommittee;
    private String affiliatedParty;
    private String electoralDistrict;
    private PledgeFulfillmentStatusDto pledgeFulfillmentStatus;
    private NatureCompletionStatusDto natureCompletionStatus;
    private LegislativeStatusDto legislativeStatus;
    private FinancialStatusDto financialStatus;
    private List<DetailedPledgeExecutionStatusDto> pledges;
}
