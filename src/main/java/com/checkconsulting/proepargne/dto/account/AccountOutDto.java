package com.checkconsulting.proepargne.dto.account;

import com.checkconsulting.proepargne.enums.ManagementMode;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.enums.RiskLevel;
import com.checkconsulting.proepargne.model.Contract;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountOutDto {
    private Long accountId;
    private Float amount;
    private PlanType type;
    private RiskLevel riskLevel;
    private ManagementMode managementMode;
    @JsonIgnore
    Contract contract;
}
