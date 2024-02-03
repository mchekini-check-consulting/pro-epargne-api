package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.TransactionDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.Transaction;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.AccountRepository;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.checkconsulting.proepargne.enums.OperationType.DEPOSIT;
import static com.checkconsulting.proepargne.enums.OperationType.WITHDRAWAL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@Slf4j
public class TransactionService {

    final TransactionRepository transactionRepository;
    final AccountRepository accountRepository;
    final CollaboratorRepository collaboratorRepository;
    private final User user;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              CollaboratorRepository collaboratorRepository, User user) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.user = user;
    }

    @Transactional
    public Transaction saveTransaction(TransactionDto transactionDto) throws GlobalException {

        Account account = getUserAccount(transactionDto);
        checkAndUpdateAccountAmount(transactionDto, account);
        return saveTransaction(transactionDto, account);
    }

    private Account getUserAccount(TransactionDto transactionDto) throws GlobalException {
        log.info("Start Handle Transaction for user {} and amount {}", user.getUserName(), transactionDto.getAmount());

        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).get();

        Optional<Account> opAccount =
                accountRepository.findAccountByCollaboratorIdAndType(collaborator.getId(), transactionDto.getPlanType());

        if (opAccount.isEmpty()) {
            log.info("User {} does not have an account", user.getEmail());
            throw new GlobalException("Aucun compte n'est trouvé pour l'utilisateur " + user.getEmail(), HttpStatus.NOT_FOUND);
        }
        return opAccount.get();
    }

    private void checkAndUpdateAccountAmount(TransactionDto transactionDto, Account account) throws GlobalException {
        if (transactionDto.getOperationType() == DEPOSIT) {
            account.setAmount(account.getAmount() + transactionDto.getAmount());
        } else if (transactionDto.getOperationType() == WITHDRAWAL) {

            if (transactionDto.getAmount() > account.getAmount()) {
                throw new GlobalException("Le montant présent sur votre compte est insuffisant pour effectuer l'operation", BAD_REQUEST);
            }
            account.setAmount(account.getAmount() - transactionDto.getAmount());
        }
    }

    private Transaction saveTransaction(TransactionDto transactionDto, Account account) {
        Transaction transaction = transactionRepository.save(Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(transactionDto.getOperationType())
                .comment(transactionDto.getComment())
                .account(account)
                .build());

        log.info("la transaction pour de l'utilisateur  {} sur le compte {} a ete effectue avec succes ", user.getEmail(),
                account.getAccountId());
        return transaction;
    }
}
