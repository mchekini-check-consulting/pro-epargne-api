package com.checkconsulting.proepargne.dto.collaborator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaboratorOutDto {
    private Long id;
    private String lastName;
    private String firstName;
    private String email;
    private String gender;
    private LocalDate birthDate;
    private LocalDate entryDate;
    private Integer grossSalary;
}
