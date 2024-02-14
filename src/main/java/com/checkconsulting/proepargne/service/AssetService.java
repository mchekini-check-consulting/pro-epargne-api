package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import com.checkconsulting.proepargne.mapper.AssetMapper;
import com.checkconsulting.proepargne.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssetService {

    final AssetRepository assetRepository;
    final AssetMapper assetMapper;

    public AssetService(AssetRepository assetRepository, AssetMapper assetMapper) {
        this.assetRepository = assetRepository;
        this.assetMapper = assetMapper;
    }
    public List<AssetDto> getAll() {
        return assetRepository.findAll().stream().map(assetMapper::mapAssetToAssetDto).collect(Collectors.toList());
    }
}
