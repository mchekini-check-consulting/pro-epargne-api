package com.checkconsulting.proepargne.dto.contract;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySignatoryInDto {
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+",message = "Lastname has to be all characters")
    private String lastName;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z]+",message = "Firstname has to be all characters")
    private String firstName;
    @Email
    private String email;
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$")
    private String dateOfBirth;
    @NotBlank
    private String jobTitle;
    @Size(min = 0, max = 15, message = "SSN must be a 15 chars string")
    private String socialSecurityNumber;
    @NotBlank
    private String countryOfBirth;
    @NotBlank
    private String countryOfResidence;
    @NotNull
    private Boolean executive;

}
