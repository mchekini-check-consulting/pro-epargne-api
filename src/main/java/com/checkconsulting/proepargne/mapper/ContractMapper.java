package com.checkconsulting.proepargne.mapper;


import com.checkconsulting.proepargne.dto.contract.*;
import com.checkconsulting.proepargne.enums.ContributionType;
import com.checkconsulting.proepargne.enums.Eligibility;
import com.checkconsulting.proepargne.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import static com.checkconsulting.proepargne.enums.ContributionType.*;

@Component
@Mapper(componentModel = "spring")
public interface ContractMapper {


    @Mapping(target = "peeContribution.contributionType", expression = "java(mapContributionPee(peeContribution))")
    @Mapping(target = "perecoContribution.contributionType", expression = "java(mapContributionPereco(perecoContribution))")
    ContractOutDto mapToContractOutDto(Contract contract);

    PeeContributionDto mapToPeeContributionOutDto(PeeContribution peeContribution);

    PerecoContributionDto mapToPerecoContributionOutDto(PerecoContribution peeContribution);


    default ContributionType mapContributionPee(PeeContribution peeContribution) {
        if (peeContribution.getRateSimpleContribution() != null) return SIMPLE;
        else if (peeContribution.getRateSeniorityContribution() != null) return SENIORITY;
        return INTERVAL;
    }

    default ContributionType mapContributionPereco(PerecoContribution perecoContribution) {
        if (perecoContribution.getRateSimpleContribution() != null) return SIMPLE;
        else if (perecoContribution.getRateSeniorityContribution() != null) return SENIORITY;
        return INTERVAL;
    }

    Company mapToCompany(CompanyInDto companyInDto);

    @Mapping(target = "eligibility", expression = "java(mapEligibility(contractInDto))")
    Contract mapToContract(ContractInDto contractInDto);

    CompanySignatory mapToCompanySignatory(CompanySignatoryInDto companySignatory);

    default Eligibility mapEligibility(ContractInDto contractInDto) {
        if (contractInDto.getEligibility() == 0) return Eligibility.ZERO_MONTH;
        if (contractInDto.getEligibility() == 1) return Eligibility.ONE_MONTH;
        if (contractInDto.getEligibility() == 2) return Eligibility.TWO_MONTH;
        return Eligibility.THREE_MONTH;
    }


}
