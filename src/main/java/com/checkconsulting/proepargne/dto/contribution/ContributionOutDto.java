package com.checkconsulting.proepargne.dto.contribution;

import com.checkconsulting.proepargne.enums.ContributionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContributionOutDto {
    private Long id;
    private Float amount;
    private ContributionStatus status;
}
