package com.checkconsulting.proepargne.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plans;
    private String isin;
    private String supportLabel;
    private String managementCompany;

    @OneToMany(mappedBy = "asset")
    private List<AssetYear> assetYearsData;
}
