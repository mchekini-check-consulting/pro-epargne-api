package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.contribution.ContributionOutDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.enums.ContributionStatus;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.ContributionMapper;
import com.checkconsulting.proepargne.mapper.TransactionMapper;
import com.checkconsulting.proepargne.model.Contribution;
import com.checkconsulting.proepargne.repository.ContributionRepository;
import com.checkconsulting.proepargne.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ContributionService {

    private  final ContributionRepository contributionRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final ContributionMapper contributionMapper;

    public ContributionService(ContributionRepository contributionRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper, ContributionMapper contributionMapper) {
        this.contributionRepository = contributionRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.contributionMapper = contributionMapper;
    }

    public Page<TransactionOutDto> findAllContribution(int page, int size, PlanType planType){
        Pageable pageable = PageRequest.of(page,size);
        return transactionRepository.findAllByPlanType(pageable,planType).map(transactionMapper::mapToTransactionDto);
    }

    @Transactional
    public ContributionOutDto updateContributionStatus(Long id) throws GlobalException {
        log.info("Find contribution by id");
        Contribution contribution = contributionRepository.findById(id).orElseThrow(
                ()-> new GlobalException("Aucun abondement avec cet identifiant.")
        );

        log.info("Update contribution");
        contribution.setStatus(ContributionStatus.APPROVED);
        contributionRepository.save(contribution);
        return contributionMapper.mapToContributionDto(contribution);
    }
}
