package com.example.hwptotable.assembly.dto.DetailedPledgeExecutionStatusDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegislativeProgressDto {
    private String proposal;
    private String standingCommittee;
    private String generalScope;
    private String plenarySessionResolution;
}
