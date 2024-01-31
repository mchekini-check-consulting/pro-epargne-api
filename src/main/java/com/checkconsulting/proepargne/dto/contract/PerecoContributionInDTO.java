package com.checkconsulting.proepargne.dto.contract;

public record PerecoContributionInDTO(
        int rateSimpleContribution,
        int ceilingSimpleContribution,
        int rateSeniorityContribution,
        int ceilingSeniorityContributionLessYear,
        int ceilingSeniorityContributionBetween1And3,
        int ceilingSeniorityContributionBetween3And5,
        int ceilingSeniorityContributionGreater5,
        int ceilingIntervalContributionFirst,
        int rateIntervalContributionFirst,
        int intervalContributionFirst,
        int ceilingIntervalContributionSecond,
        int rateIntervalContributionSecond,
        int intervalContributionSecond,
        int ceilingIntervalContributionThird,
        int rateIntervalContributionThird,
        int intervalContributionThird,
        boolean perecoInterestAccepted,
        boolean perecoVoluntaryDepositAccepted,
        boolean perecoProfitSharingAccepted,
        boolean perecoTimeSavingAccountAccepted) {

}
