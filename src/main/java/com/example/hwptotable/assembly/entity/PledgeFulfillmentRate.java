package com.example.hwptotable.assembly.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class PledgeFulfillmentRate { // 공약 이행률

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 의원명
    @Column(nullable = false)
    private String legislatorName;

    // 상임위원회
    @Column(nullable = false)
    private String standingCommittee;

    //  소속정당
    @Column(nullable = false)
    private String affiliatedParty;

    // 지역구
    @Column(nullable = false)
    private String electoralDistrict;

    // 총 공약 수
    @Column(nullable = false)
    private String totalPledges;

    // 완료
    @Column(nullable = true)
    private String completedPledges;

    // 추진중
    @Column(nullable = true)
    private String ongoingPledges;

    // 보류
    @Column(nullable = true)
    private String pendingPledges;

    // 폐기
    @Column(nullable = true)
    private String discardedPledges;

    // 기타
    @Column(nullable = true)
    private String otherPledges;

    // 국정공약
    @Column(nullable = true)
    private String nationalPledges;

    // 지역공약
    @Column(nullable = true)
    private String regionalPledges;

    // 입법공약
    @Column(nullable = true)
    private String legislativePledges;

    // 재정공약
    @Column(nullable = true)
    private String financialPledges;

    // 임기내
    @Column(nullable = true)
    private String inTermPledges;

    // 임기후
    @Column(nullable = true)
    private String outTermPledges;

    // 지속사업
    @Column(nullable = true)
    private String ongoingProjects;

    // 신규사업
    @Column(nullable = true)
    private String newProjects;

    // 필요입법 공약 총 수
    @Column(nullable = true)
    private String totalRequiredLegislativePledges;

    // 입법 의결 완료 공약 총 수
    @Column(nullable = true)
    private String totalLegislativeResolutionCompletedPledges;

    // 필요재정 총액
    @Column(nullable = true)
    private String totalRequiredBudget;

    // 확보재정 총액
    @Column(nullable = true)
    private String totalSecuredBudget;

    // 집행재정 총액
    @Column(nullable = true)
    private String totalExecutedBudget;

    // 공약이행현황 상세
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pledgeFulfillmentRate")
    private List<DetailedPledgeExecutionStatus> pledges = new ArrayList<>();

    // 공약이행현황 상세 추가
    public void addPledge(DetailedPledgeExecutionStatus pledge) {
        pledges.add(pledge);
    }

    @Builder
    public PledgeFulfillmentRate(String legislatorName, String standingCommittee, String affiliatedParty, String electoralDistrict, String totalPledges, String completedPledges, String ongoingPledges, String pendingPledges, String discardedPledges, String otherPledges, String nationalPledges, String regionalPledges, String legislativePledges, String financialPledges, String inTermPledges, String outTermPledges, String ongoingProjects, String newProjects, String totalRequiredLegislativePledges, String totalLegislativeResolutionCompletedPledges, String totalRequiredBudget, String totalSecuredBudget, String totalExecutedBudget) {
        this.legislatorName = legislatorName;
        this.standingCommittee = standingCommittee;
        this.affiliatedParty = affiliatedParty;
        this.electoralDistrict = electoralDistrict;
        this.totalPledges = totalPledges;
        this.completedPledges = completedPledges;
        this.ongoingPledges = ongoingPledges;
        this.pendingPledges = pendingPledges;
        this.discardedPledges = discardedPledges;
        this.otherPledges = otherPledges;
        this.nationalPledges = nationalPledges;
        this.regionalPledges = regionalPledges;
        this.legislativePledges = legislativePledges;
        this.financialPledges = financialPledges;
        this.inTermPledges = inTermPledges;
        this.outTermPledges = outTermPledges;
        this.ongoingProjects = ongoingProjects;
        this.newProjects = newProjects;
        this.totalRequiredLegislativePledges = totalRequiredLegislativePledges;
        this.totalLegislativeResolutionCompletedPledges = totalLegislativeResolutionCompletedPledges;
        this.totalRequiredBudget = totalRequiredBudget;
        this.totalSecuredBudget = totalSecuredBudget;
        this.totalExecutedBudget = totalExecutedBudget;
    }
}
