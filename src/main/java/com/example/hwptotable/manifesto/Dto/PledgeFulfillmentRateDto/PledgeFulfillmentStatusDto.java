package com.example.hwptotable.manifesto.Dto.PledgeFulfillmentRateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PledgeFulfillmentStatusDto {
    private String totalPledges;
    private String completedPledges;
    private String ongoingPledges;
    private String pendingPledges;
    private String discardedPledges;
    private String otherPledges;
}
