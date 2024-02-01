package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.ContractMapper;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.repository.ContractRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;


    public ContractService(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }


    public ContractOutDto getContract(Long contractId) throws GlobalException {
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new GlobalException("Contract not found with id: " + contractId, HttpStatus.NOT_FOUND));

        return contractMapper.mapToContractOutDto(contract);

    }
}

