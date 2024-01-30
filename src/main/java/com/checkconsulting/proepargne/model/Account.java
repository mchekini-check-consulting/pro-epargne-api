package com.checkconsulting.proepargne.model;

import com.checkconsulting.proepargne.enums.PlanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;
    private Float amount;
    @Enumerated(STRING)
    private PlanType type;

    @ManyToOne
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @OneToMany(mappedBy = "account")
    private List<Transaction> transactionList;
}
