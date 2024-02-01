package com.checkconsulting.proepargne.dto.contract;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanySignatoryDTO {
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
