package com.checkconsulting.proepargne.model;

import com.checkconsulting.proepargne.enums.LegalForm;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.EnumType.STRING;

import org.hibernate.annotations.GenericGenerator;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Company {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String siren;
    private String companyName;
    @Enumerated(STRING)
    private LegalForm legalForm;
    private String siret;
    private String businessActivity;
    private String businessAddress;
    private Integer workforce;
    private Long totalWages;

    @OneToOne
    @JoinColumn(name = "contract_id")
    private Contract contractId;

}
