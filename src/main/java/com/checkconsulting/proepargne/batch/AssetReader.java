package com.checkconsulting.proepargne.batch;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Configuration
public class AssetReader {


    private final ApplicationContext context;

    private final static String ASSET_FILE_PATH = "classpath:files/historique-assets.csv";

    public AssetReader(ApplicationContext context) {
        this.context = context;
    }

    @Bean
    public FlatFileItemReader reader() throws IOException {
        log.info("Job Reader of assets file started");


        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource resource = resolver.getResource(ASSET_FILE_PATH);


        return new FlatFileItemReaderBuilder()
                .name("assetItemReader")
                .resource(resource)
                .linesToSkip(1)
                .delimited()
                .delimiter(";")
                .names(generateColumnNames(resource).toArray(new String[0]))
                .fieldSetMapper(fieldSet -> {

                    AssetDto assetDto = new AssetDto();
                    Map<String, Float> map = new HashMap<>();
                    List<String> columns;

                    try {
                        columns = generateColumnNames(resource);
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

    public List<String> generateColumnNames(Resource res) throws IOException {

        Resource csv = context.getResource(ASSET_FILE_PATH);
        InputStream inputStream = csv.getInputStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String firstLine = reader.readLine();
            return List.of(firstLine.split(";"));
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}