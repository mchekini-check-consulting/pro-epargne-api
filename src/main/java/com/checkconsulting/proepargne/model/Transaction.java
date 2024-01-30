package com.checkconsulting.proepargne.model;

import com.checkconsulting.proepargne.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Float amount;
    @Enumerated(STRING)
    private OperationType type;
    private String comment;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

}
