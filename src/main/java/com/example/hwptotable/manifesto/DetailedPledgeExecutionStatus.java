package com.example.hwptotable.manifesto;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class DetailedPledgeExecutionStatus {  // 공약이행현황 상세

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 순번
    @Column(nullable = false)
    private int turn;

    // 공약명
    @Column(nullable = false)
    private String pledgeName;

    // 공약내용요약
    @Column(nullable = false, columnDefinition = "TEXT")
    private String pledgeSummary;

    // 성격별 구분 (국정/지역)
    @Column(nullable = true)
    private String natureDivision_NationalRegional;

    // 성격별 구분 (입법/재정)
    @Column(nullable = true)
    private String natureDivision_LegislationFinance;

    // 내용별 구분 (임기내/임기후)
    @Column(nullable = true)
    private String contentDivision_InTerm_OutTerm;

    // 내용별 구분 (지속/신규)
    @Column(nullable = true)
    private String contentDivision_Continued_New;

    // 이행도
    @Column(nullable = true)
    private String fulfillmentRate;

    // 입법명
    @Column(nullable = true)
    private String legislativeName;

    // 발의
    @Column(nullable = true)
    private String proposal;

    // 상임위
    @Column(nullable = true)
    private String standingCommittee;

    // 법사위
    @Column(nullable = true)
    private String generalScope;

    // 본회의 의결
    @Column(nullable = true)
    private String plenarySessionResolution;

    // 입법 내역
    @Column(nullable = true, columnDefinition = "TEXT")
    private String legislativeDetails;

    // 필요 재정액
    @Column(nullable = true)
    private String requiredBudgetAmount;

    // 확보 재정액
    @Column(nullable = true)
    private String securedBudgetAmount;

    // 집행 재정액
    @Column(nullable = true)
    private String executedBudgetAmount;

    // 확보 내역
    @Column(nullable = true, columnDefinition = "TEXT")
    private String securedDetails;

    // 집행 내역
    @Column(nullable = true, columnDefinition = "TEXT")
    private String executionDetails;

    // 기타 이행근거
    @Column(nullable = true, columnDefinition = "TEXT")
    private String otherImplementationBasis;

    // 공약 이행률
    @ManyToOne(fetch = FetchType.LAZY)
    private PledgeFulfillmentRate pledgeFulfillmentRate;

    @Builder
    public DetailedPledgeExecutionStatus(int turn, String pledgeName, String pledgeSummary, String natureDivision_NationalRegional, String natureDivision_LegislationFinance, String contentDivision_InTerm_OutTerm, String contentDivision_Continued_New, String fulfillmentRate, String legislativeName, String proposal, String standingCommittee, String generalScope, String plenarySessionResolution, String legislativeDetails, String requiredBudgetAmount, String securedBudgetAmount, String executedBudgetAmount, String securedDetails, String executionDetails, String otherImplementationBasis, PledgeFulfillmentRate pledgeFulfillmentRate) {
        this.turn = turn;
        this.pledgeName = pledgeName;
        this.pledgeSummary = pledgeSummary;
        this.natureDivision_NationalRegional = natureDivision_NationalRegional;
        this.natureDivision_LegislationFinance = natureDivision_LegislationFinance;
        this.contentDivision_InTerm_OutTerm = contentDivision_InTerm_OutTerm;
        this.contentDivision_Continued_New = contentDivision_Continued_New;
        this.fulfillmentRate = fulfillmentRate;
        this.legislativeName = legislativeName;
        this.proposal = proposal;
        this.standingCommittee = standingCommittee;
        this.generalScope = generalScope;
        this.plenarySessionResolution = plenarySessionResolution;
        this.legislativeDetails = legislativeDetails;
        this.requiredBudgetAmount = requiredBudgetAmount;
        this.securedBudgetAmount = securedBudgetAmount;
        this.executedBudgetAmount = executedBudgetAmount;
        this.securedDetails = securedDetails;
        this.executionDetails = executionDetails;
        this.otherImplementationBasis = otherImplementationBasis;
        this.pledgeFulfillmentRate = pledgeFulfillmentRate;
    }
}
