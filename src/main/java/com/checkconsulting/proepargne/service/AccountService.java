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

@Service
@Slf4j
public class AccountService {

    private final AccountMapper accountMapper;
    private final User user;
    private final CollaboratorRepository collaboratorRepository;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper, User user, CollaboratorRepository collaboratorRepository) {
        this.accountMapper = accountMapper;
        this.user = user;
        this.collaboratorRepository = collaboratorRepository;
    }

    public List<AccountOutDto> getCollaboratorAccounts(){
        Collaborator collaborator = this.collaboratorRepository.findByEmail(user.getEmail()).get();
       return collaborator.getAccountList().stream().map(account -> accountMapper.mapToAccountOutDto(account)).collect(Collectors.toList());
    }
}
