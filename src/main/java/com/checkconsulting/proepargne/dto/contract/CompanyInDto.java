package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.LegalForm;
import com.checkconsulting.proepargne.validators.EnumValidator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInDto {
    @Pattern(regexp = "^\\d{13}$", message = "sirent has to 13 characters and all numbers")
    private String siren;
    @NotBlank()
    private String companyName;
    @EnumValidator(enumClass = LegalForm.class)
    private String legalForm;
    @Pattern(regexp = "^\\d{13}$", message = "sirent has to 13 characters and all numbers")
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
