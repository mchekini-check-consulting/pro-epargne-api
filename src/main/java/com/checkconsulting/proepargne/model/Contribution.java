package com.checkconsulting.proepargne.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Contribution {
    @Id
    @Column(name = "transaction_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;


    private Float amount;

}
