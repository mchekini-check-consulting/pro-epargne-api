package com.checkconsulting.proepargne.dto.contract;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContractInDto {
    @Min(0)
    @Max(11)
    Integer closingMonth;
    @Min(0)
    @Max(3)
    Integer eligibility;
    @Valid
    CompanyInDto company;
    @Valid
    CompanySignatoryInDto companySignatory;
    @Valid
    PeeContributionDto peeContribution;
    @Valid
    PerecoContributionDto perecoContribution;

}
