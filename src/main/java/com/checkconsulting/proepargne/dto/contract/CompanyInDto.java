package com.checkconsulting.proepargne.dto.contract;

import com.checkconsulting.proepargne.enums.LegalForm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanyInDto {

    private String siren;
    private String companyName;
    private LegalForm legalForm;
    private String siret;
    private String businessActivity;
    private String businessAddress;
    private Integer workforce;
    private Long totalWages;
}
