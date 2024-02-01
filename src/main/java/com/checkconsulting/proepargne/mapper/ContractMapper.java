package com.checkconsulting.proepargne.mapper;


import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.dto.contract.PeeContributionOutDto;
import com.checkconsulting.proepargne.dto.contract.PerecoContributionOutDto;
import com.checkconsulting.proepargne.enums.ContributionType;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.PeeContribution;
import com.checkconsulting.proepargne.model.PerecoContribution;
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

    PeeContributionOutDto mapToPeeContributionOutDto(PeeContribution peeContribution);

    PerecoContributionOutDto mapToPerecoContributionOutDto(PerecoContribution peeContribution);


    default ContributionType mapContributionPee(PeeContribution peeContribution){
        if (peeContribution.getRateSimpleContribution() != null) return SIMPLE;
        else if (peeContribution.getRateSeniorityContribution() != null) return SENIORITY;
        return INTERVAL;
    }

    default ContributionType mapContributionPereco(PerecoContribution perecoContribution){
        if (perecoContribution.getRateSimpleContribution() != null) return SIMPLE;
        else if (perecoContribution.getRateSeniorityContribution() != null) return SENIORITY;
        return INTERVAL;
    }


}
