package com.checkconsulting.proepargne.dto.contract;

public record CompanySignatoryInDTO(
                String lastName,
                String firstName,
                String email,
                String dateOfBirth,
                String jobTitle,
                String socialSecurityNumber,
                String countryOfBirth,
                String countryOfResidence,
                Boolean executive) {

}
