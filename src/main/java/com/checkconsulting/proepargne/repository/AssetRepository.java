package com.checkconsulting.proepargne.repository;

import com.checkconsulting.proepargne.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}
