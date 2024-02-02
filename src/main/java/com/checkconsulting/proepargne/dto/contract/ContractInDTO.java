package com.checkconsulting.proepargne.dto.contract;

import java.util.Optional;

import com.checkconsulting.proepargne.enums.Eligibility;

public record ContractInDTO(
        Integer closingMonth,
        Eligibility eligibility,
        CompanyDTO company,
        CompanySignatoryDTO companySignatory,
        Optional<PeeContributionDTO> peeContribution,
        Optional<PercoContributionDTO> percoContribution) {
}
