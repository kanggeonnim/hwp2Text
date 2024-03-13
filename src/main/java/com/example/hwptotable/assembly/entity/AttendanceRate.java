package com.example.hwptotable.assembly.entity;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class AttendanceRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 의원명
    @Column(nullable = false)
    private String legislatorName;

    //  소속정당
    @Column(nullable = false)
    private String affiliatedParty;

    // 회의일수
    @Column(nullable = false)
    private int meetingDays;

    // 출석
    @Column(nullable = false)
    private int attendance;

    // 결석
    @Column(nullable = false)
    private int absence;

    // 청가
    @Column(nullable = false)
    private int leave;

    // 출장
    @Column(nullable = false)
    private int businessTrip;

    // 결석신고서
    @Column(nullable = false)
    private int absenceReport;
}
