package com.checkconsulting.proepargne.dto.account;

import java.util.Optional;

import com.checkconsulting.proepargne.enums.ManagementMode;
import com.checkconsulting.proepargne.enums.RiskLevel;

public record AccountUpdateDto(
                Optional<RiskLevel> peeRiskLevel,
                Optional<RiskLevel> perecoRiskLevel,
                Optional<ManagementMode> peeManagementMode,
                Optional<ManagementMode> perecoManagementMode) {

}
