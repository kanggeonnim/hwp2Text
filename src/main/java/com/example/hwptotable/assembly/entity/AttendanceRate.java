package com.example.hwptotable.assembly.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
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

    @Builder
    public AttendanceRate(String legislatorName, String affiliatedParty, int meetingDays, int attendance, int absence, int leave, int businessTrip, int absenceReport) {
        this.legislatorName = legislatorName;
        this.affiliatedParty = affiliatedParty;
        this.meetingDays = meetingDays;
        this.attendance = attendance;
        this.absence = absence;
        this.leave = leave;
        this.businessTrip = businessTrip;
        this.absenceReport = absenceReport;
    }

    public void addDays(int meetingDays, int attendance, int absence, int leave, int businessTrip, int absenceReport) {
        this.meetingDays += meetingDays;
        this.attendance += attendance;
        this.absence += absence;
        this.leave += leave;
        this.businessTrip += businessTrip;
        this.absenceReport += absenceReport;
    }
}
