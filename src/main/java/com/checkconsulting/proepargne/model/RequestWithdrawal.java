package com.checkconsulting.proepargne.model;

import com.checkconsulting.proepargne.enums.PlanType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static jakarta.persistence.EnumType.STRING;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class RequestWithdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(STRING)
    private PlanType typeAccount;
    private String reasonUnblocking;
    private String rib;
    private Float amount ;
    private String attachedFile;
    private LocalDateTime createdAt;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

}
