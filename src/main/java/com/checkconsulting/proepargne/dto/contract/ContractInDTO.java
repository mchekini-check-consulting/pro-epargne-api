package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.Eligibility;

public record ContractInDTO(
        Integer closingMonth,
        Eligibility eligibility,
        CompanyDTO company,
        CompanySignatoryDTO companySignatory,
        PeeContributionDTO peeContribution,
        PercoContributionDTO percoContribution) {
}
