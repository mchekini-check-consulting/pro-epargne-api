package com.checkconsulting.proepargne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PercoContribution {

    @Id
    @Column(name = "contract_id")
    private Long contractId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private Integer rateSimpleContribution;
    private Integer ceilingSimpleContribution;

    private Integer rateSeniorityContribution;
    private Integer ceilingSeniorityContributionLessYear;
    private Integer ceilingSeniorityContributionBetween1And3;
    private Integer ceilingSeniorityContributionBetween3And5;
    private Integer ceilingSeniorityContributionGreater5;

    private Integer ceilingIntervalContributionFirst;
    private Integer rateIntervalContributionFirst;
    private Integer intervalContributionFirst;

    private Integer ceilingIntervalContributionSecond;
    private Integer rateIntervalContributionSecond;
    private Integer intervalContributionSecond;

    private Integer ceilingIntervalContributionThird;
    private Integer rateIntervalContributionThird;
    private Integer intervalContributionThird;

    private Boolean percoInterestAccepted;
    private Boolean percoVoluntaryDepositAccepted;
    private Boolean percoProfitSharingAccepted;
    private Boolean percoTimeSavingAccountAccepted;
}

// 0 - 1000
// 1000 - 2000
// 2000 - 3000