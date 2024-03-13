package com.example.hwptotable.assembly.dto.PledgeFulfillmentRateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegislativeStatusDto {
    private String totalRequiredLegislativePledges;
    private String totalLegislativeResolutionCompletedPledges;
}
