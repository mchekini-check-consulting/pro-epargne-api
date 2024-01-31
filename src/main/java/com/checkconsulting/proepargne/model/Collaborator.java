package com.checkconsulting.proepargne.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Collaborator {
    @Id
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String gender;
    private LocalDate birthDate;
    private LocalDate entryDate;
    private Integer grossSalary;

    @OneToMany(mappedBy = "collaborator")
    private List<Account> accountList;
}
