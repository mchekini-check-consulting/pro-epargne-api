package com.checkconsulting.proepargne.dto.collaborator;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CollaboratorUpdateDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Le nom de l'employé est obligatoire ")
    private String lastName;
    @NotBlank(message = "Le prénom de l'employé est obligatoire")
    private String firstName;
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'adresse mail est invalide")
    private String email;
    @NotBlank(message = "Le sexe de l'employé est obligatoire")
    private String gender;
    @NotNull(message = "La date de naissance de l'employé est obligatoire")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    @NotNull(message = "La date d'entrée de l'employé a l'entreprisde est obligatoire")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate entryDate;
    @NotNull(message = "Le salaire brut est obligatoire")
    @NumberFormat(style = NumberFormat.Style.NUMBER)
    private Integer grossSalary;
}
