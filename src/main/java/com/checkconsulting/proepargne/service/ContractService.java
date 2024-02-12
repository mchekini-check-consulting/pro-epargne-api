package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.contract.ContractInDto;
import com.checkconsulting.proepargne.dto.contract.ContractOutDto;
import com.checkconsulting.proepargne.dto.contract.PeeContributionDto;
import com.checkconsulting.proepargne.dto.contract.PerecoContributionDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.ContractMapper;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.ContractRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final User user;

    public ContractService(ContractRepository contractRepository, ContractMapper contractMapper, User user) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.user = user;
    }

    public ContractOutDto getContract() throws GlobalException {
        Contract contract = contractRepository.findByCompanyAdminId(user.getKeycloakId())
                .orElseThrow(() -> new GlobalException("Contract not found", HttpStatus.NOT_FOUND));
        return contractMapper.mapToContractOutDto(contract);

    }

    public Contract getContractByAdminId() throws GlobalException {
        return contractRepository.findByCompanyAdminId(user.getKeycloakId())
                .orElseThrow(() -> new GlobalException("Contract not found with id: " + user.getKeycloakId(), HttpStatus.NOT_FOUND));
    }

    @Transactional
    public Contract createContract(ContractInDto contractInDto) {

        Contract contract = contractMapper.mapToContract(contractInDto);
        contract.setCompanyAdminId(user.getKeycloakId());
        contractRepository.saveAndFlush(contract);


        contract.getCompany().setContract(contract);
        contract.getCompanySignatory().setContract(contract);
        contract.getPerecoContribution().setContract(contract);
        contract.getPeeContribution().setContract(contract);

        PeeContributionDto peeContribution = contractInDto.getPeeContribution();
        PerecoContributionDto perecoContribution = contractInDto.getPerecoContribution();


        checkIfPeeAndPerecoEnabled(contract, peeContribution, perecoContribution);

        return contract;
    }

    private void checkIfPeeAndPerecoEnabled(Contract contract, PeeContributionDto peeContribution, PerecoContributionDto perecoContribution) {
        if (peeContribution.getRateSimpleContribution() != null ||
                peeContribution.getRateSeniorityContribution() != null ||
                peeContribution.getRateIntervalContributionFirst() != null)
            contract.setPeeEnabled(true);

        if (perecoContribution.getRateSimpleContribution() != null ||
                perecoContribution.getRateSeniorityContribution() != null ||
                perecoContribution.getRateIntervalContributionFirst() != null)
            contract.setPerecoEnabled(true);
    }
}

