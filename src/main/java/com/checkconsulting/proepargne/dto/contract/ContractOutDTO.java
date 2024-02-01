package com.checkconsulting.proepargne.dto.contract;

import java.util.List;

import com.checkconsulting.proepargne.enums.Eligibility;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.CompanySignatory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractOutDTO {
        private Integer closingMonth;
        private Eligibility eligibility;
        private CompanyDTO company;
        private CompanySignatoryDTO companySignatory;
        private PeeContributionDTO peeContribution;
        private PercoContributionDTO percoContribution;
        private List<Account> accounts;
}
