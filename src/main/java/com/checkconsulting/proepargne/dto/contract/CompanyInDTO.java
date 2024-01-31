package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.LegalForm;

public record CompanyInDTO(
        String siren,
        String companyName,
        LegalForm legalForm,
        String siret,
        String businessActivity,
        String businessAddress,
        Integer workforce,
        Long totalWages) {

}
