package com.checkconsulting.proepargne.dto.contract;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractInDto {

    Integer closingMonth;
    Integer eligibility;
    CompanyInDto company;
    CompanySignatoryInDto companySignatory;
    PeeContributionDto peeContribution;
    PerecoContributionDto perecoContribution;

}
