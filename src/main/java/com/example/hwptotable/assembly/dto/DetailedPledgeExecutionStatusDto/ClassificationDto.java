package com.example.hwptotable.assembly.dto.DetailedPledgeExecutionStatusDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassificationDto {
    private String natureDivision_NationalRegional;
    private String natureDivision_LegislationFinance;
    private String contentDivision_InTerm_OutTerm;
    private String contentDivision_Continued_New;
}
