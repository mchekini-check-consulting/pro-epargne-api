package com.checkconsulting.proepargne.dto;

import com.checkconsulting.proepargne.enums.OperationType;
import com.checkconsulting.proepargne.enums.PlanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionDto {

    private Float amount;
    private PlanType planType;
    private String comment;
    private OperationType operationType;
}
