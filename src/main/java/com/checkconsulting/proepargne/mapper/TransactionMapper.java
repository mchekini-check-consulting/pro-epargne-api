package com.checkconsulting.proepargne.mapper;

import com.checkconsulting.proepargne.dto.contribution.ContributionOutDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.enums.OperationType;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.checkconsulting.proepargne.enums.OperationType.*;

@Component
@Mapper(componentModel = "spring", uses = { ContributionMapper.class })

public interface TransactionMapper {

    ContributionMapper contributionMapper = Mappers.getMapper(ContributionMapper.class);

    @Mapping(target = "contribution", expression = "java(mapContributionAmount(transaction))")
    TransactionOutDto mapToTransactionDto(Transaction transaction);

    default String mapTransactionDate(LocalDateTime createdAt) {
        return createdAt.format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    default PlanType mapTransactionPlanType(PlanType plantype) {
        return plantype;
    }

    default ContributionOutDto mapContributionAmount(Transaction transaction) {
        return contributionMapper.mapToContributionDto(transaction.getContribution());
    }

    default String mapTransactionType(OperationType operationType) {
        if (operationType == CONTRIBUTION) return "Abondement";
        else if (operationType == WITHDRAWAL) return "Retrait";
        else if (operationType == INTEREST) return "Règlement de l’intéressement";
        else if (operationType == PARTICIPATION) return "Règlement de la participation";
        else if (operationType == DEPOSIT) return "Versement volontaire";
        else return "versement d’un compte épargne temps";
    }
}