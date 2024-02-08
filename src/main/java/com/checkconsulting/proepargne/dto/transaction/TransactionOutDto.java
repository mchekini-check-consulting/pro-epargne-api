package com.checkconsulting.proepargne.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class TransactionOutDto {
    private Long transactionId;
    private Float amount;
    private Float previousAmount;
    private Float nextAmount;
    private String createdAt;
    private String type;
    private String comment;

}
