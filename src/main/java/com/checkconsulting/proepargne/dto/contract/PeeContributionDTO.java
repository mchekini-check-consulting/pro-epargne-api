package com.checkconsulting.proepargne.dto.contract;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PeeContributionDTO {
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
        private boolean peeProfitSharingAccepte;
}
