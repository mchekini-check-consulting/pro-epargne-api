package com.checkconsulting.proepargne.batch;

import com.checkconsulting.proepargne.model.Asset;
import com.checkconsulting.proepargne.model.AssetYear;
import com.checkconsulting.proepargne.repository.AssetRepository;
import com.checkconsulting.proepargne.repository.AssetYearRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AssetWriter implements ItemWriter {

    final AssetRepository assetRepository;
    final AssetYearRepository assetYearRepository;

    public AssetWriter(AssetRepository assetRepository, AssetYearRepository assetYearRepository) {
        this.assetRepository = assetRepository;
        this.assetYearRepository = assetYearRepository;
    }

    @Override
    public void write(Chunk chunk) throws Exception {
        log.info("Job Writer for assets file started with data : {}", chunk.getItems());

        for (Object asset : chunk.getItems()) {
            Asset createdAsset = assetRepository.saveAndFlush((Asset) asset);
            for (AssetYear assetYear : ((Asset) asset).getAssetYearsData()) {
                assetYear.setAsset(createdAsset);
                assetYearRepository.save(assetYear);
            }
        }
    }
}