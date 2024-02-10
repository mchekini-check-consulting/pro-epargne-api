package com.checkconsulting.proepargne.resources;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.account.AccountUpdateDto;
import com.checkconsulting.proepargne.service.AccountService;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1/account")
public class AccountResource {

    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping()
    @Authenticated(authenticated = true)
    public ResponseEntity<?> updateAccoutn(@RequestBody AccountUpdateDto payload) {
        var reponse = this.accountService.updateAccount(payload);
        return ResponseEntity.ok().body(reponse);
    }

    @GetMapping()
    @Authenticated(authenticated = true)
    public ResponseEntity<?> getCollaboratorAccounts() {
        var collaboratorAccounts = this.accountService.getCollaboratorAccounts();
        return ResponseEntity.ok().body(collaboratorAccounts);
    }

}
