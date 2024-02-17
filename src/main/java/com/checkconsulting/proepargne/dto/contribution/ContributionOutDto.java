package com.checkconsulting.proepargne.dto.contribution;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ContributionOutDto {
    private Long id;
    private Float amount;
}
