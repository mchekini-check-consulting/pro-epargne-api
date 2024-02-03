package com.checkconsulting.proepargne.dto.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PercoContributionDTO {
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
        private boolean percoInterestAccepted;
        private boolean percoVoluntaryDepositAccepted;
        private boolean percoProfitSharingAccepted;
        private boolean percoTimeSavingAccountAccepted;

}
