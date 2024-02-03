package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.TransactionDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Transaction;
import com.checkconsulting.proepargne.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("api/v1/transaction")
public class TransactionResource {

    final TransactionService transactionService;

    public TransactionResource(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Authenticated(authenticated = true)
    public ResponseEntity saveTransaction(@RequestBody TransactionDto transactionDto) throws GlobalException {
        Transaction transaction = transactionService.saveTransaction(transactionDto);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(transaction.getTransactionId())
                .toUri();

        return ResponseEntity.created(location).build();

    }
}

