package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.account.AccountUpdateDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/account")
public class AccountResource {
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    @Authenticated(authenticated = true)
    public ResponseEntity<?> getCollaboratorAccounts() throws GlobalException {
        return ResponseEntity.ok(accountService.getCollaboratorAccounts());
    }

    @PutMapping
    @Authenticated(authenticated = true)
    public ResponseEntity<?> updateAccount(@RequestBody AccountUpdateDto payload) {
        return ResponseEntity.ok().body(this.accountService.updateAccount(payload));
    }

}
