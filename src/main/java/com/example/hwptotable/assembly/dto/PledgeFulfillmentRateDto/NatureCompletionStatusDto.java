package com.example.hwptotable.assembly.dto.PledgeFulfillmentRateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NatureCompletionStatusDto {
    private String nationalPledges;
    private String regionalPledges;
    private String legislativePledges;
    private String financialPledges;
    private String inTermPledges;
    private String outTermPledges;
    private String ongoingProjects;
    private String newProjects;
}
