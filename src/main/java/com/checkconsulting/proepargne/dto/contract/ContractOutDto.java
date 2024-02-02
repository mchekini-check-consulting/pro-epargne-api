package com.checkconsulting.proepargne.dto.contract;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContractOutDto {

    private Long contractId;
    private PeeContributionOutDto peeContribution;
    private PerecoContributionOutDto perecoContribution;
}
