package com.checkconsulting.proepargne.batch;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import com.checkconsulting.proepargne.mapper.AssetMapper;
import com.checkconsulting.proepargne.model.Asset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AssetProcessor implements ItemProcessor<AssetDto, Asset> {

    final AssetMapper assetMapper;

    public AssetProcessor(AssetMapper assetMapper) {
        this.assetMapper = assetMapper;
    }

    @Override
    public Asset process(AssetDto assetDto) throws Exception {
        log.info("Job Processor for assets file started with data : {} ", assetDto);
        return assetMapper.mapAssetDtoToAsset(assetDto);
    }

}
