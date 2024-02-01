package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.LegalForm;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyDTO {
    private String siren;
    private String companyName;
    private LegalForm legalForm;
    private String siret;
    private String businessActivity;
    private String businessAddress;
    private Integer workforce;
    private Long totalWages;
}
