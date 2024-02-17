package com.checkconsulting.proepargne.service;

import com.checkconsulting.proepargne.dto.transaction.TransactionDto;
import com.checkconsulting.proepargne.dto.transaction.TransactionOutDto;
import com.checkconsulting.proepargne.enums.PlanType;
import com.checkconsulting.proepargne.exception.GlobalException;
import com.checkconsulting.proepargne.mapper.TransactionMapper;
import com.checkconsulting.proepargne.model.Account;
import com.checkconsulting.proepargne.model.Collaborator;
import com.checkconsulting.proepargne.model.Contract;
import com.checkconsulting.proepargne.model.Contribution;
import com.checkconsulting.proepargne.model.PeeContribution;
import com.checkconsulting.proepargne.model.PerecoContribution;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
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

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdAt")));

        Collaborator collaborator = collaboratorRepository.findByEmail(user.getEmail()).get();

        List<Account> accounts = accountRepository.findAccountByCollaboratorId(collaborator.getId());

        log.info("all Transactions related to user {}, in his {} account(s)", user.getUserName(), accounts.size());

        Page<TransactionOutDto> transactions = transactionRepository.findAllByAccountIn(accounts, pageable, filter)
                .map(transactionMapper::mapToTransactionDto);
        log.info("all Transactions related to user {}", transactions.getContent());
        return transactions;
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

        Optional<Account> opAccount = accountRepository.findAccountByCollaboratorIdAndType(collaborator.getId(),
                transactionDto.getPlanType());

        if (opAccount.isEmpty()) {
            log.info("User {} does not have an account", user.getEmail());
            throw new GlobalException("Aucun compte n'est trouvé pour l'utilisateur " + user.getEmail(),
                    HttpStatus.NOT_FOUND);
        }
        return opAccount.get();
    }

    private void checkAndUpdateAccountAmount(TransactionDto transactionDto, Account account) throws GlobalException {
        if (transactionDto.getOperationType() == WITHDRAWAL) {

            if (transactionDto.getAmount() > account.getAmount()) {
                throw new GlobalException(
                        "Le montant présent sur votre compte est insuffisant pour effectuer l'operation", BAD_REQUEST);
            }
            account.setAmount(account.getAmount() - transactionDto.getAmount());
        } else {
            account.setAmount(account.getAmount() + transactionDto.getAmount());
        }
    }

    private Transaction saveTransaction(TransactionDto transactionDto, Account account) {
        log.info("transaction dto: {}", transactionDto);
        Transaction transaction = transactionRepository
                .save(calculateCurrentValuesBeforeSaveTransaction(transactionDto, account));

        log.info("la transaction pour de l'utilisateur  {} sur le compte {} a ete effectue avec succes ",
                user.getEmail(),
                account.getAccountId());
        return transaction;
    }

    private Contribution checkAndUpdateContributionAmount(TransactionDto transactionDto, Account account) {
        Contract contract = account.getContract();
        Contribution contribution = new Contribution();
        Float amount = null;

        if (transactionDto.getPlanType() == PlanType.PEE) {

            PeeContribution peeContribution = contract.getPeeContribution();
            if (peeContribution.getRateSimpleContribution() != null) {
                // SIMPLE
                amount = (transactionDto.getAmount() * peeContribution.getRateSimpleContribution()) / 100;
                if (amount > peeContribution.getCeilingSimpleContribution()) {
                    amount = peeContribution.getCeilingSimpleContribution().floatValue();
                }
            } else if (peeContribution.getRateSeniorityContribution() != null) {
                // SENIORITY
                Period p = Period.between(LocalDate.now(), account.getCollaborator().getEntryDate());
                amount = (transactionDto.getAmount() * peeContribution.getRateSeniorityContribution()) / 100;

                if (p.getYears() > 5) {
                    if (amount > peeContribution.getCeilingSeniorityContributionGreater5()) {
                        amount = peeContribution.getCeilingSeniorityContributionGreater5().floatValue();
                    }
                } else if (p.getYears() >= 3) {
                    if (amount > peeContribution.getCeilingSeniorityContributionBetween3And5()) {
                        amount = peeContribution.getCeilingSeniorityContributionBetween3And5().floatValue();
                    }
                } else if (p.getYears() >= 1) {
                    if (amount > peeContribution.getCeilingSeniorityContributionBetween1And3()) {
                        amount = peeContribution.getCeilingSeniorityContributionBetween1And3().floatValue();
                    }
                } else {
                    if (amount > peeContribution.getCeilingSeniorityContributionLessYear()) {
                        amount = peeContribution.getCeilingSeniorityContributionLessYear().floatValue();
                    }
                }
            } else {
                // INTERVAL;
                if (transactionDto.getAmount() > peeContribution.getIntervalContributionSecond()
                        && transactionDto.getAmount() < peeContribution.getIntervalContributionThird()) {

                    amount = (transactionDto.getAmount() * peeContribution.getRateIntervalContributionThird()) / 100;
                    if (amount > peeContribution.getCeilingIntervalContributionThird()) {
                        amount = peeContribution.getCeilingIntervalContributionThird().floatValue();
                    }

                } else if (transactionDto.getAmount() > peeContribution.getIntervalContributionFirst()
                        && transactionDto.getAmount() < peeContribution.getIntervalContributionSecond()) {

                    amount = (transactionDto.getAmount() * peeContribution.getRateIntervalContributionSecond()) / 100;
                    if (amount > peeContribution.getCeilingIntervalContributionSecond()) {
                        amount = peeContribution.getCeilingIntervalContributionSecond().floatValue();
                    }

                } else if (transactionDto.getAmount() < peeContribution.getIntervalContributionFirst()) {

                    amount = (transactionDto.getAmount() * peeContribution.getIntervalContributionFirst()) / 100;
                    if (amount > peeContribution.getCeilingIntervalContributionFirst()) {
                        amount = peeContribution.getCeilingIntervalContributionFirst().floatValue();
                    }

                }
            }

        } else if (transactionDto.getPlanType() == PlanType.PERECO) {
            PerecoContribution perecoContribution = contract.getPerecoContribution();
            if (perecoContribution.getRateSimpleContribution() != null) {
                // SIMPLE
                amount = (transactionDto.getAmount() * perecoContribution.getRateSimpleContribution()) / 100;
                if (amount > perecoContribution.getCeilingSimpleContribution()) {
                    amount = perecoContribution.getCeilingSimpleContribution().floatValue();
                }
            } else if (perecoContribution.getRateSeniorityContribution() != null) {
                // SENIORITY
                Period p = Period.between(LocalDate.now(), account.getCollaborator().getEntryDate());
                amount = (transactionDto.getAmount() * perecoContribution.getRateSeniorityContribution()) / 100;

                if (p.getYears() > 5) {
                    if (amount > perecoContribution.getCeilingSeniorityContributionGreater5()) {
                        amount = perecoContribution.getCeilingSeniorityContributionGreater5().floatValue();
                    }
                } else if (p.getYears() >= 3) {
                    if (amount > perecoContribution.getCeilingSeniorityContributionBetween3And5()) {
                        amount = perecoContribution.getCeilingSeniorityContributionBetween3And5().floatValue();
                    }
                } else if (p.getYears() >= 1) {
                    if (amount > perecoContribution.getCeilingSeniorityContributionBetween1And3()) {
                        amount = perecoContribution.getCeilingSeniorityContributionBetween1And3().floatValue();
                    }
                } else {
                    if (amount > perecoContribution.getCeilingSeniorityContributionLessYear()) {
                        amount = perecoContribution.getCeilingSeniorityContributionLessYear().floatValue();
                    }
                }

            } else {
                // INTERVAL;
                if (transactionDto.getAmount() > perecoContribution.getIntervalContributionSecond()
                        && transactionDto.getAmount() < perecoContribution.getIntervalContributionThird()) {

                    amount = (transactionDto.getAmount() * perecoContribution.getRateIntervalContributionThird()) / 100;
                    if (amount > perecoContribution.getCeilingIntervalContributionThird()) {
                        amount = perecoContribution.getCeilingIntervalContributionThird().floatValue();
                    }

                } else if (transactionDto.getAmount() > perecoContribution.getIntervalContributionFirst()
                        && transactionDto.getAmount() < perecoContribution.getIntervalContributionSecond()) {

                    amount = (transactionDto.getAmount() * perecoContribution.getRateIntervalContributionSecond())
                            / 100;
                    if (amount > perecoContribution.getCeilingIntervalContributionSecond()) {
                        amount = perecoContribution.getCeilingIntervalContributionSecond().floatValue();
                    }

                } else if (transactionDto.getAmount() < perecoContribution.getIntervalContributionFirst()) {

                    amount = (transactionDto.getAmount() * perecoContribution.getIntervalContributionFirst()) / 100;
                    if (amount > perecoContribution.getCeilingIntervalContributionFirst()) {
                        amount = perecoContribution.getCeilingIntervalContributionFirst().floatValue();
                    }

                }

            }
        }

        contribution.setAmount(amount);
        return contribution;
    }

    private Transaction calculateCurrentValuesBeforeSaveTransaction(TransactionDto transactionDto, Account account) {
        float previousOperationAmount = 0;
        Contribution contribution = null;

        if (transactionDto.getOperationType() == DEPOSIT) {
            contribution = checkAndUpdateContributionAmount(transactionDto, account);
            //we compute the the contribution value just in case of a deposit kind of transaction
            previousOperationAmount = account.getAmount() - transactionDto.getAmount();
        } else if (transactionDto.getOperationType() == WITHDRAWAL) {
            previousOperationAmount = account.getAmount() + transactionDto.getAmount();
        }

        return Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(transactionDto.getOperationType())
                .planType(transactionDto.getPlanType())
                .comment(transactionDto.getComment())
                .account(account)
                .previousAmount(previousOperationAmount)
                .nextAmount(account.getAmount())
                .contribution(contribution)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
