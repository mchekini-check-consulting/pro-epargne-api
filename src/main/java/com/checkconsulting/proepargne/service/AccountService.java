package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.account.AccountOutDto;
import com.checkconsulting.proepargne.mapper.AccountMapper;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.AccountRepository;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.*;

import java.util.ArrayList;


@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final ContractService contractService;
    private final AccountMapper accountMapper;
    private final User user;
    private final CollaboratorRepository collaboratorRepository;

    public AccountService(AccountRepository accountRepository, ContractService contractService, AccountMapper accountMapper, User user, CollaboratorRepository collaboratorRepository) {
        this.contractService = contractService;
        this.accountMapper = accountMapper;
        this.user = user;
        this.collaboratorRepository = collaboratorRepository;
        this.accountRepository = accountRepository;

    }

    public List<AccountOutDto> getCollaboratorAccounts() {
        Collaborator collaborator = this.collaboratorRepository.findByEmail(user.getEmail()).get();
        return collaborator.getAccountList().stream().map(account -> accountMapper.mapToAccountOutDto(account)).collect(Collectors.toList());
}

    public List<Account>  createCollaboratorAccounts (Collaborator collaborator)  throws GlobalException {
        ArrayList<Account> accounts = new ArrayList<>();

        Contract contract =  contractService.getContractByAdminId();
        log.info("Get contract");
        PerecoContribution perecoContribution = contract.getPerecoContribution();
        PeeContribution peeContribution = contract.getPeeContribution();

        if(perecoContribution.getRateSimpleContribution() != null
                || perecoContribution.getRateSeniorityContribution() != null
                || perecoContribution.getIntervalContributionFirst() != null){
            Account perecoAccount = Account.builder()
                    .type(PlanType.PERECO)
                    .amount(0F)
                    .contract(contract)
                    .collaborator(collaborator)
                    .build();
            accounts.add(perecoAccount);
        }

        if(peeContribution.getRateSimpleContribution() != null
                || peeContribution.getRateSeniorityContribution() != null
                || peeContribution.getIntervalContributionFirst() != null ){
            Account peeAccount = Account.builder()
                    .type(PlanType.PEE)
                    .amount(0F)
                    .contract(contract)
                    .collaborator(collaborator)
                    .build();
            accounts.add(peeAccount);
        }

        log.info("Saving new Collaborator Accounts to database");
        return accountRepository.saveAll(accounts);
    }
}
