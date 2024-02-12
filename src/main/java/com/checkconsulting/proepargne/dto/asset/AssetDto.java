package com.checkconsulting.proepargne.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssetDto {

    private String plans;
    private String isin;
    private String supportLabel;
    private String managementCompany;
    private Map<String, Float> assetYearsData;
}
