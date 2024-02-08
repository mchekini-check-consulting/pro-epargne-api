package com.checkconsulting.proepargne.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.PERSIST;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String gender;
    private LocalDate birthDate;
    private LocalDate entryDate;
    private Integer grossSalary;
    private String keycloakId;

    @OneToMany(mappedBy = "collaborator",cascade = PERSIST)
    private List<Account> accountList;
}
