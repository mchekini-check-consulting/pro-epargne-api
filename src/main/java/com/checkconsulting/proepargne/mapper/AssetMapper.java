package com.checkconsulting.proepargne.mapper;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import com.checkconsulting.proepargne.model.Asset;
import com.checkconsulting.proepargne.model.AssetYear;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Mapper(componentModel = "spring")
public interface AssetMapper {

    @Mapping(target = "assetYearsData", expression = "java(mapPreviousYears(assetDto.getAssetYearsData()))")
    Asset mapAssetDtoToAsset(AssetDto assetDto);

    default List<AssetYear> mapPreviousYears(Map<String, Float> previousYearsData) {
        return previousYearsData.entrySet().stream()
                .map(entry -> AssetYear.builder()
                        .year(Integer.parseInt(entry.getKey()))
                        .value(entry.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}