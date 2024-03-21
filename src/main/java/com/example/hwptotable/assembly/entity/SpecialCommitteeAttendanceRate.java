package com.example.hwptotable.assembly.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SpecialCommitteeAttendanceRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long assemblyId;

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
    private int leaves;

    // 출장
    @Column(nullable = false)
    private int businessTrip;

    // 결석신고서
    @Column(nullable = false)
    private int absenceReport;

    @Builder
    public SpecialCommitteeAttendanceRate(Long assemblyId, int meetingDays, int attendance, int absence, int leaves, int businessTrip, int absenceReport) {
        this.assemblyId = assemblyId;
        this.meetingDays = meetingDays;
        this.attendance = attendance;
        this.absence = absence;
        this.leaves = leaves;
        this.businessTrip = businessTrip;
        this.absenceReport = absenceReport;
    }

    public void addDays(int meetingDays, int attendance, int absence, int leaves, int businessTrip, int absenceReport) {
        this.meetingDays += meetingDays;
        this.attendance += attendance;
        this.absence += absence;
        this.leaves += leaves;
        this.businessTrip += businessTrip;
        this.absenceReport += absenceReport;
    }
}
