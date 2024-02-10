package com.checkconsulting.proepargne.dto.account;

import com.checkconsulting.proepargne.enums.ManagementMode;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.enums.RiskLevel;

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
}
