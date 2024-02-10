package com.checkconsulting.proepargne.batch;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class AssetReader {

    @Bean
    public FlatFileItemReader reader() throws IOException {
        log.info("Job Reader of assets file started");

        return new FlatFileItemReaderBuilder()
                .name("assetItemReader")
                .resource(new ClassPathResource("files/Historique Assets.csv"))
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(generateColumnNames().toArray(new String[0]))
                .fieldSetMapper(fieldSet -> {

                    AssetDto assetDto = new AssetDto();
                    Map<String, Float> map = new HashMap<>();
                    List<String> columns;

                    try {
                        columns = generateColumnNames();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    assetDto.setPlans(fieldSet.readString(0));
                    assetDto.setIsin(fieldSet.readString(1));
                    assetDto.setSupportLabel(fieldSet.readString(2));
                    assetDto.setManagementCompany(fieldSet.readString(3));

                    for (int i = 4; i < fieldSet.getFieldCount(); i++) {
                        map.put(columns.get(i), Float.valueOf(fieldSet.readString(i).replace(',', '.')));
                    }
                    assetDto.setAssetYearsData(map);
                    return assetDto;
                })
                .build();
    }

    public List<String> generateColumnNames() throws IOException {
        File resource = new ClassPathResource("files/Historique Assets.csv").getFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(resource))) {
            String firstLine = reader.readLine();
            return List.of(firstLine.split(";"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}