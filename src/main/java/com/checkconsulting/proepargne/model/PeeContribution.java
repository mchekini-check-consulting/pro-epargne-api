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
public class PeeContribution {

    @Id
    @Column(name = "contract_id")
    private Long contractId;
    @OneToOne
    @MapsId
    @JoinColumn(name = "contract_id")
    private Contract contract;

    private int rateSimpleContribution;
    private int ceilingSimpleContribution;
    private int rateSeniorityContribution;
    private int ceilingSeniorityContributionLessYear;
    private int ceilingSeniorityContributionBetween1And3;
    private int ceilingSeniorityContributionBetween3And5;
    private int ceilingSeniorityContributionGreater5;
    private int ceilingIntervalContributionFirst;
    private int rateIntervalContributionFirst;
    private int intervalContributionFirst;
    private int ceilingIntervalContributionSecond;
    private int rateIntervalContributionSecond;
    private int intervalContributionSecond;
    private int ceilingIntervalContributionThird;
    private int rateIntervalContributionThird;
    private int intervalContributionThird;
    private boolean peeInterestAccepted;
    private boolean peeVoluntaryDepositAccepted;
    private boolean peeProfitSharingAccepted;


}
