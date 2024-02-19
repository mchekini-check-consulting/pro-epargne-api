package com.checkconsulting.proepargne.dto.contract;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractOutDto {

    private Long contractId;
    private LocalDateTime createdAt;
    private String closingMonth;
    private String eligibility;
    private CompanyInDto company;
    private CompanySignatoryInDto companySignatory;
    private PeeContributionDto peeContribution;
    private PerecoContributionDto perecoContribution;
}

