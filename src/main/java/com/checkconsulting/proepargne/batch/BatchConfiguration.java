package com.checkconsulting.proepargne.batch;

import com.checkconsulting.proepargne.dto.asset.AssetDto;
import com.checkconsulting.proepargne.model.Asset;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.IOException;

@Slf4j
@Configuration
public class BatchConfiguration {

    final AssetReader assetReader;
    final AssetProcessor assetProcessor;
    final AssetWriter assetWriter;


    public BatchConfiguration(AssetReader assetReader, AssetProcessor assetProcessor, AssetWriter assetWriter) {
        this.assetReader = assetReader;
        this.assetProcessor = assetProcessor;
        this.assetWriter = assetWriter;
    }

    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws IOException {

        return new StepBuilder("step", jobRepository)
                .<AssetDto, Asset>chunk(15, transactionManager)
                .reader(assetReader.reader())
                .processor(assetProcessor)
                .writer(assetWriter)
                .build();
    }

    @Bean
    public Job migrationJob(JobRepository jobRepository, Step step) {

        Flow flow = new FlowBuilder<SimpleFlow>("Getting data from assets file Flow")
                .start(step)
                .end();

        return new JobBuilder("AssetMigrationJob", jobRepository)
                .start(flow)
                .end()
                .build();
    }
}
