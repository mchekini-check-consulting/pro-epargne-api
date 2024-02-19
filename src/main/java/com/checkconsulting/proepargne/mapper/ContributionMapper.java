package com.checkconsulting.proepargne.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.checkconsulting.proepargne.dto.contribution.ContributionOutDto;
import com.checkconsulting.proepargne.model.Contribution;

@Component
@Mapper(componentModel = "spring")
public interface ContributionMapper {
    ContributionOutDto mapToContributionDto(Contribution contribution);
}
