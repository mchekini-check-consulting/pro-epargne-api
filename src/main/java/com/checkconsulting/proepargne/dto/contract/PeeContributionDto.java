package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.ContributionType;
import com.checkconsulting.proepargne.validators.EnumValidator;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PeeContributionDto {

    @EnumValidator(enumClass = ContributionType.class)
    private ContributionType contributionType;
    @PositiveOrZero
    private Integer rateSimpleContribution;
    @PositiveOrZero
    private Integer ceilingSimpleContribution;
    @PositiveOrZero
    private Integer rateSeniorityContribution;
    @PositiveOrZero
    private Integer ceilingSeniorityContributionLessYear;
    @PositiveOrZero
    private Integer ceilingSeniorityContributionBetween1And3;
    @PositiveOrZero
    private Integer ceilingSeniorityContributionBetween3And5;
    @PositiveOrZero
    private Integer ceilingSeniorityContributionGreater5;
    @PositiveOrZero
    private Integer ceilingIntervalContributionFirst;
    @PositiveOrZero
    private Integer rateIntervalContributionFirst;
    @PositiveOrZero
    private Integer intervalContributionFirst;
    @PositiveOrZero
    private Integer ceilingIntervalContributionSecond;
    @PositiveOrZero
    private Integer rateIntervalContributionSecond;
    @PositiveOrZero
    private Integer intervalContributionSecond;
    @PositiveOrZero
    private Integer ceilingIntervalContributionThird;
    @PositiveOrZero
    private Integer rateIntervalContributionThird;
    @PositiveOrZero
    private Integer intervalContributionThird;
    @PositiveOrZero
    private Boolean peeInterestAccepted;
    @PositiveOrZero
    private Boolean peeVoluntaryDepositAccepted;
    @PositiveOrZero
    private Boolean peeProfitSharingAccepted;
}
