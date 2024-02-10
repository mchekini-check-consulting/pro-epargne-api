package com.checkconsulting.proepargne.resources;

import com.checkconsulting.proepargne.aspect.authentication.Authenticated;
import com.checkconsulting.proepargne.dto.transaction.TransactionDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Transaction;
import com.checkconsulting.proepargne.service.TransactionService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    @Authenticated(authenticated = true)
    public Page<TransactionOutDto> getTransactions(
            @RequestParam(defaultValue = "0", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int size,
            @RequestParam(defaultValue = "", required = false) String filter
    ) {
        return transactionService.getTransactions(page, size, filter);
    }
}

