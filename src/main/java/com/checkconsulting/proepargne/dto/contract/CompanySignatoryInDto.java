package com.checkconsulting.proepargne.dto.contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySignatoryInDto {

    private String lastName;
    private String firstName;
    private String email;
    private String dateOfBirth;
    private String jobTitle;
    private String socialSecurityNumber;
    private String countryOfBirth;
    private String countryOfResidence;
    private Boolean executive;

}
