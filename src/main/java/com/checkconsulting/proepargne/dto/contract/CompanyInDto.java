package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.LegalForm;
import com.checkconsulting.proepargne.validators.EnumValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInDto {
    @Size(max = 13, min = 13, message = "siren must exactly 13 chars")
    private String siren;
    @NotBlank()
    private String companyName;
    @EnumValidator(enumClass = LegalForm.class)
    private String legalForm;
    @Size(max = 13, min = 13, message = "siret must exactly 13 chars")
    private String siret;
    @NotBlank
    private String businessActivity;
    @NotBlank
    private String businessAddress;
    @Positive
    private Integer workforce;
    @Positive
    private Long totalWages;
}
