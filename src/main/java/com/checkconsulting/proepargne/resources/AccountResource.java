package com.checkconsulting.proepargne.resources;
import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/account")
public class AccountResource {
    private final AccountService accountService;

    public AccountResource(AccountService accountService) {
        this.accountService = accountService;
    }


    @GetMapping
    @Authenticated(authenticated = true)
    public ResponseEntity getCollaboratorAccounts (){
        return ResponseEntity.ok(accountService.getCollaboratorAccounts());
    }
}
