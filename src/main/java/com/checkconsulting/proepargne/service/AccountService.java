package com.checkconsulting.proepargne.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.checkconsulting.proepargne.dto.account.AccountOutDto;
import com.checkconsulting.proepargne.dto.account.AccountUpdateDto;
import com.checkconsulting.proepargne.enums.ManagementMode;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.mapper.AccoutMapper;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final User user;
    private final CollaboratorRepository collaboratorRepository;
    @Autowired
    private final AccoutMapper accoutMapper;

    public List<AccountOutDto> updateAccount(AccountUpdateDto payload) {
        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).get();

        for (Account account : collaborator.getAccountList()) {
            if (account.getType().equals(PlanType.PEE)) {
                if (payload.peeManagementMode().isPresent()) {
                    // we want to update pereco
                    if (payload.peeRiskLevel().isPresent()
                            && payload.peeManagementMode().get().equals(ManagementMode.DELEGATED)) {
                        account.setRiskLevel(payload.peeRiskLevel().get());
                    } else if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.FREE)) {
                        account.setRiskLevel(null);
                    }
                    account.setManagementMode(payload.peeManagementMode().get());
                } else {
                    if (payload.peeRiskLevel().isPresent()
                            && payload.peeManagementMode().get().equals(ManagementMode.DELEGATED)) {
                        account.setRiskLevel(payload.peeRiskLevel().get());
                    }
                }

            } else if (account.getType().equals(PlanType.PERECO)) {
                if (payload.perecoManagementMode().isPresent()) {
                    // we want to update pereco
                    if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.DELEGATED)) {
                        account.setRiskLevel(payload.perecoRiskLevel().get());
                    } else if (payload.perecoManagementMode().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.FREE)) {
                        account.setRiskLevel(null);
                    }
                    account.setManagementMode(payload.perecoManagementMode().get());
                } else {
                    if (payload.perecoRiskLevel().isPresent()
                            && payload.perecoManagementMode().get().equals(ManagementMode.DELEGATED)) {
                        account.setRiskLevel(payload.perecoRiskLevel().get());
                    }
                }

            }
        }
        collaborator = collaboratorRepository.saveAndFlush(collaborator);
        return collaborator.getAccountList().stream().map((obj) -> accoutMapper.mapAccountToAccountOutDto(obj))
                .collect(Collectors.toList());
    }
}
