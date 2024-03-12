package com.example.hwptotable.manifesto.Dto.DetailedPledgeExecutionStatusDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LegislativeDto {
    private String legislativeName;
    private LegislativeProgressDto legislativeProgress;
    private String legislativeDetails;
}
