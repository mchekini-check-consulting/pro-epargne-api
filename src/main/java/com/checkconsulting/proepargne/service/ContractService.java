package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.contract.ContractInDto;
import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.ContractMapper;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.repository.ContractRepository;
import jakarta.transaction.Transactional;
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
    public Contract getContractById(Long contractId) throws GlobalException{
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new GlobalException("Contract not found with id: " + contractId, HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Contract createContract(ContractInDto contractInDto) {

        Contract contract = contractMapper.mapToContract(contractInDto);
        contractRepository.saveAndFlush(contract);


        contract.getCompany().setContract(contract);
        contract.getCompanySignatory().setContract(contract);
        contract.getPeeContribution().setContract(contract);
        contract.getPerecoContribution().setContract(contract);

        return contract;
    }
}

