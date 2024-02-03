package com.checkconsulting.proepargne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class PerecoContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private Boolean perecoInterestAccepted;
    private Boolean perecoVoluntaryDepositAccepted;
    private Boolean perecoProfitSharingAccepted;
    private Boolean perecoTimeSavingAccountAccepted;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;
}
