package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.Eligibility;

public record ContractInDTO(
        Integer closingMonth,
        Eligibility eligibility,
        CompanyInDTO company,
        CompanySignatoryInDTO companySignatory,
        PeeContributionInDTO peeContribution,
        PerecoContributionInDTO perecoContribution) {
}
