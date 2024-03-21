package com.example.hwptotable.assembly.dto.AttendanceRateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRateDto {
    private Long id;
    // 의원명
    private String legislatorName;
    //  소속정당
    private String affiliatedParty;
    // 회의일수
    private int meetingDays;
    // 출석
    private int attendance;
    // 결석
    private int absence;
    // 청가
    private int leaves;
    // 출장
    private int businessTrip;
    // 결석신고서
    private int absenceReport;
}
