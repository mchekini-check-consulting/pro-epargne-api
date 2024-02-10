package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.transaction.TransactionDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.TransactionMapper;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.Transaction;
import com.checkconsulting.proepargne.model.User;
import com.checkconsulting.proepargne.repository.AccountRepository;
import com.checkconsulting.proepargne.repository.CollaboratorRepository;
import com.checkconsulting.proepargne.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
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
    final TransactionMapper transactionMapper;

    private final User user;

    public TransactionService(TransactionRepository transactionRepository,
                              AccountRepository accountRepository,
                              CollaboratorRepository collaboratorRepository, User user, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.user = user;
        this.transactionMapper = transactionMapper;
    }


    public Page<TransactionOutDto> getTransactions(int page, int size, String filter) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(filter)));

        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).get();
        List<Account> accounts = accountRepository.findAccountByCollaboratorId(collaborator.getId());

        log.info("all Transactions related to user {}, in his {} account(s)", user.getUserName(), accounts.size());

        return transactionRepository.findAllByAccountIn(accounts, pageable).map(transactionMapper::mapToTransactionDto);

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
        if (transactionDto.getOperationType() == WITHDRAWAL) {

            if (transactionDto.getAmount() > account.getAmount()) {
                throw new GlobalException("Le montant présent sur votre compte est insuffisant pour effectuer l'operation", BAD_REQUEST);
            }
            account.setAmount(account.getAmount() - transactionDto.getAmount());
        } else {
            account.setAmount(account.getAmount() + transactionDto.getAmount());
        }
    }


    private Transaction saveTransaction(TransactionDto transactionDto, Account account) {
        Transaction transaction = transactionRepository.save(calculateCurrentValuesBeforeSaveTransaction(transactionDto, account));

        log.info("la transaction pour de l'utilisateur  {} sur le compte {} a ete effectue avec succes ", user.getEmail(),
                account.getAccountId());
        return transaction;
    }

    private Transaction calculateCurrentValuesBeforeSaveTransaction(TransactionDto transactionDto, Account account) {
        float previousOperationAmount = 0;

        if (transactionDto.getOperationType() == DEPOSIT) {
            previousOperationAmount = account.getAmount() - transactionDto.getAmount();
        } else if (transactionDto.getOperationType() == WITHDRAWAL) {
            previousOperationAmount = account.getAmount() + transactionDto.getAmount();
        }
        return Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(transactionDto.getOperationType())
                .comment(transactionDto.getComment())
                .account(account)
                .previousAmount(previousOperationAmount)
                .nextAmount(account.getAmount())
                .createdAt(LocalDateTime.now())
                .build();
    }


}
