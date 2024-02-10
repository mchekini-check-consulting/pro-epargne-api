package com.checkconsulting.proepargne.mapper;


import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

import static com.checkconsulting.proepargne.enums.OperationType.*;

@Component
@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(target = "createdAt", expression = "java(mapTransactionDate(transaction))")
    @Mapping(target = "type", expression = "java(mapTransactionType(transaction))")
    TransactionOutDto mapToTransactionDto(Transaction transaction);

    default String mapTransactionDate(Transaction transaction) {
        return transaction.getCreatedAt().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }

    default String mapTransactionType(Transaction transaction) {
        if (transaction.getType() == CONTRIBUTION) return "Abondement";
        else if (transaction.getType() == WITHDRAWAL) return "Retrait";
        else if (transaction.getType() == INTEREST) return "Règlement de l’intéressement";
        else if (transaction.getType() == PARTICIPATION) return "Règlement de la participation";
        else if (transaction.getType() == DEPOSIT) return "Versement volontaire";
        else return "versement d’un compte épargne temps";
    }

}
