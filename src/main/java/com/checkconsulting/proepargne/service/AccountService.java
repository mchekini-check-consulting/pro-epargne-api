package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.account.AccountOutDto;
import com.checkconsulting.proepargne.dto.account.AccountUpdateDto;
import com.checkconsulting.proepargne.enums.ManagementMode;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.AccountMapper;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.AccountRepository;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.checkconsulting.proepargne.enums.ManagementMode.DELEGATED;
import static com.checkconsulting.proepargne.enums.RiskLevel.PRUDENT;

@Service
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final User user;
    private final CollaboratorRepository collaboratorRepository;

    public AccountService(AccountRepository accountRepository,
                          AccountMapper accountMapper,
                          User user,
                          CollaboratorRepository collaboratorRepository) {
        this.accountMapper = accountMapper;
        this.user = user;
        this.collaboratorRepository = collaboratorRepository;
        this.accountRepository = accountRepository;

    }

    public List<AccountOutDto> getCollaboratorAccounts() throws GlobalException {
        Collaborator collaborator = this.collaboratorRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new GlobalException("L'utilisateur connecte n'est pas enregistre en base de données", HttpStatus.NOT_FOUND));
        return collaborator.getAccountList().stream()
                .map(accountMapper::mapToAccountOutDto)
                .sorted(Comparator.comparing(AccountOutDto::getType))
                .toList();
    }

    public void createCollaboratorAccounts(Collaborator collaborator, Contract contract) {
        ArrayList<Account> accounts = new ArrayList<>();

        if (contract.isPeeEnabled()) {
            Account peeAccount = Account.builder()
                    .type(PlanType.PEE)
                    .amount(0F)
                    .contract(contract)
                    .collaborator(collaborator)
                    .managementMode(DELEGATED)
                    .riskLevel(PRUDENT)
                    .build();
            accounts.add(peeAccount);
        }

        if (contract.isPerecoEnabled()) {
            Account perecoAccount = Account.builder()
                    .type(PlanType.PERECO)
                    .amount(0F)
                    .contract(contract)
                    .collaborator(collaborator)
                    .managementMode(DELEGATED)
                    .riskLevel(PRUDENT)
                    .build();
            accounts.add(perecoAccount);
        }


        log.info("Saving new Collaborator Accounts to database");
        accountRepository.saveAll(accounts);
    }


    //TODO DO refactoring
    public List<AccountOutDto> updateAccount(AccountUpdateDto payload) {
        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).get();

        for (Account account : collaborator.getAccountList()) {
            if (account.getType().equals(PlanType.PEE)) {
                if (payload.peeManagementMode().isPresent()) {
                    // we want to update pereco
                    if (payload.peeRiskLevel().isPresent()
                            && payload.peeManagementMode().get().equals(DELEGATED)) {
                        account.setRiskLevel(payload.peeRiskLevel().get());
                    } else if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.FREE)) {
                        account.setRiskLevel(null);
                    }
                    account.setManagementMode(payload.peeManagementMode().get());
                } else {
                    if (payload.peeRiskLevel().isPresent()
                            && payload.peeManagementMode().get().equals(DELEGATED)) {
                        account.setRiskLevel(payload.peeRiskLevel().get());
                    }
                }

            } else if (account.getType().equals(PlanType.PERECO)) {
                if (payload.perecoManagementMode().isPresent()) {
                    // we want to update pereco
                    if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(DELEGATED)) {
                        account.setRiskLevel(payload.perecoRiskLevel().get());
                    } else if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.FREE)) {
                        account.setRiskLevel(null);
                    }
                    account.setManagementMode(payload.perecoManagementMode().get());
                } else {
                    if (payload.perecoRiskLevel().isPresent()
                            && payload.perecoManagementMode().get().equals(DELEGATED)) {
                        account.setRiskLevel(payload.perecoRiskLevel().get());
                    }
                }

            }
        }
        collaborator = collaboratorRepository.saveAndFlush(collaborator);
        return collaborator.getAccountList().stream().map(accountMapper::mapToAccountOutDto)
                .toList();
    }
}
