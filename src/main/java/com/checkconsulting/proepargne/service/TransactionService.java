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
import com.checkconsulting.proepargne.model.Transaction.TransactionBuilder;
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

        int currentYear = LocalDate.now().getYear();
        LocalDate startOfYear = LocalDate.of(currentYear, 1, 1);
        LocalDateTime startOfYearDateTime = startOfYear.atStartOfDay();

        List<Transaction> transactions = transactionRepository.findByAccountAndCreatedAtAfter(account,
                startOfYearDateTime);

        Float previousContributionsSum = transactions.stream().map((t) -> t.getContribution().getAmount()).reduce(0F,
                (subtotal, element) -> subtotal + element);

        if (transactionDto.getPlanType() == PlanType.PEE) {

            PeeContribution peeContribution = contract.getPeeContribution();
            if (peeContribution.getRateSimpleContribution() != null) {
                // SIMPLE
                // In case we already surpassed the ceiling, no contribution will be created
                if (previousContributionsSum >= peeContribution.getCeilingSimpleContribution()) {
                    return null;
                }
                amount = (transactionDto.getAmount() *
                        peeContribution.getRateSimpleContribution()) / 100;
                Float totalContribution = getTotalContributions(previousContributionsSum, amount);

                if (totalContribution > peeContribution.getCeilingSimpleContribution()) {
                    amount = adjustNewAmount(previousContributionsSum, amount,
                            peeContribution.getCeilingSimpleContribution());
                }
            } else if (peeContribution.getRateSeniorityContribution() != null) {
                // SENIORITY
                Period p = Period.between(account.getCollaborator().getEntryDate(),
                        LocalDate.now());
                amount = (transactionDto.getAmount() *
                        peeContribution.getRateSeniorityContribution()) / 100;
                Float totalContribution = getTotalContributions(previousContributionsSum, amount);

                if (p.getYears() > 5) {
                    if (previousContributionsSum >= peeContribution.getCeilingSeniorityContributionGreater5()) {
                        return null;
                    }
                    if (totalContribution > peeContribution.getCeilingSeniorityContributionGreater5()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingSeniorityContributionGreater5());
                    }
                } else if (p.getYears() >= 3) {
                    if (previousContributionsSum >= peeContribution.getCeilingSeniorityContributionBetween3And5()) {
                        return null;
                    }
                    if (totalContribution > peeContribution.getCeilingSeniorityContributionBetween3And5()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingSeniorityContributionBetween3And5());
                    }
                } else if (p.getYears() >= 1) {
                    if (previousContributionsSum >= peeContribution.getCeilingSeniorityContributionBetween1And3()) {
                        return null;
                    }
                    if (totalContribution > peeContribution.getCeilingSeniorityContributionBetween1And3()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingSeniorityContributionBetween1And3());
                    }
                } else {
                    if (previousContributionsSum >= peeContribution.getCeilingSeniorityContributionLessYear()) {
                        return null;
                    }
                    if (totalContribution > peeContribution.getCeilingSeniorityContributionLessYear()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingSeniorityContributionLessYear());
                    }
                }
            } else {
                // INTERVAL;
                if (transactionDto.getAmount() > peeContribution.getIntervalContributionSecond()
                        && transactionDto.getAmount() < peeContribution.getIntervalContributionThird()) {
                    if (previousContributionsSum >= peeContribution.getCeilingIntervalContributionThird()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            peeContribution.getRateIntervalContributionThird()) / 100;

                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > peeContribution.getCeilingIntervalContributionThird()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingIntervalContributionThird());
                    }
                } else if (transactionDto.getAmount() > peeContribution.getIntervalContributionFirst()
                        && transactionDto.getAmount() < peeContribution.getIntervalContributionSecond()) {
                    if (previousContributionsSum >= peeContribution.getCeilingIntervalContributionSecond()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            peeContribution.getRateIntervalContributionSecond()) / 100;
                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > peeContribution.getCeilingIntervalContributionSecond()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingIntervalContributionSecond());
                    }
                } else if (transactionDto.getAmount() <= peeContribution.getIntervalContributionFirst()) {
                    if (previousContributionsSum >= peeContribution.getCeilingIntervalContributionFirst()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            peeContribution.getRateIntervalContributionFirst()) / 100;
                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > peeContribution.getCeilingIntervalContributionFirst()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                peeContribution.getCeilingIntervalContributionFirst());
                    }
                }
            }

        } else if (transactionDto.getPlanType() == PlanType.PERECO) {
            PerecoContribution perecoContribution = contract.getPerecoContribution();
            if (perecoContribution.getRateSimpleContribution() != null) {
                // SIMPLE
                if (previousContributionsSum >= perecoContribution.getCeilingSimpleContribution()) {
                    return null;
                }
                amount = (transactionDto.getAmount() *
                        perecoContribution.getRateSimpleContribution()) / 100;
                Float totalContribution = getTotalContributions(previousContributionsSum, amount);

                if (totalContribution > perecoContribution.getCeilingSimpleContribution()) {
                    amount = adjustNewAmount(previousContributionsSum, amount,
                            perecoContribution.getCeilingSimpleContribution());
                }
            } else if (perecoContribution.getRateSeniorityContribution() != null) {
                // SENIORITY
                Period p = Period.between(account.getCollaborator().getEntryDate(),
                        LocalDate.now());
                amount = (transactionDto.getAmount() *
                        perecoContribution.getRateSeniorityContribution()) / 100;
                Float totalContribution = getTotalContributions(previousContributionsSum, amount);

                if (p.getYears() > 5) {
                    if (previousContributionsSum >= perecoContribution.getCeilingSeniorityContributionGreater5()) {
                        return null;
                    }
                    if (totalContribution > perecoContribution.getCeilingSeniorityContributionGreater5()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingSeniorityContributionGreater5());
                    }
                } else if (p.getYears() >= 3) {
                    if (previousContributionsSum >= perecoContribution.getCeilingSeniorityContributionBetween3And5()) {
                        return null;
                    }
                    if (totalContribution > perecoContribution.getCeilingSeniorityContributionBetween3And5()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingSeniorityContributionBetween3And5());
                    }
                } else if (p.getYears() >= 1) {
                    if (previousContributionsSum >= perecoContribution.getCeilingSeniorityContributionBetween1And3()) {
                        return null;
                    }
                    if (totalContribution > perecoContribution.getCeilingSeniorityContributionBetween1And3()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingSeniorityContributionBetween1And3());
                    }
                } else {
                    if (previousContributionsSum >= perecoContribution.getCeilingSeniorityContributionLessYear()) {
                        return null;
                    }
                    if (totalContribution > perecoContribution.getCeilingSeniorityContributionLessYear()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingSeniorityContributionLessYear());
                    }
                }

            } else {
                // INTERVAL;
                if (transactionDto.getAmount() > perecoContribution.getIntervalContributionSecond()
                        && transactionDto.getAmount() < perecoContribution.getIntervalContributionThird()) {
                    if (previousContributionsSum >= perecoContribution.getCeilingIntervalContributionThird()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            perecoContribution.getRateIntervalContributionThird()) / 100;
                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > perecoContribution.getCeilingIntervalContributionThird()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingIntervalContributionThird());
                    }

                } else if (transactionDto.getAmount() > perecoContribution.getIntervalContributionFirst()
                        && transactionDto.getAmount() < perecoContribution.getIntervalContributionSecond()) {
                    if (previousContributionsSum >= perecoContribution.getCeilingIntervalContributionSecond()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            perecoContribution.getRateIntervalContributionSecond())
                            / 100;
                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > perecoContribution.getCeilingIntervalContributionSecond()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingIntervalContributionSecond());
                    }

                } else if (transactionDto.getAmount() <= perecoContribution.getIntervalContributionFirst()) {
                    if (previousContributionsSum >= perecoContribution.getCeilingIntervalContributionFirst()) {
                        return null;
                    }
                    amount = (transactionDto.getAmount() *
                            perecoContribution.getRateIntervalContributionFirst()) / 100;
                    Float totalContribution = getTotalContributions(previousContributionsSum, amount);
                    if (totalContribution > perecoContribution.getCeilingIntervalContributionFirst()) {
                        amount = adjustNewAmount(previousContributionsSum, amount,
                                perecoContribution.getCeilingIntervalContributionFirst());
                    }

                }

            }
        }

        contribution.setAmount(amount);
        return contribution;
    }

    private Float getTotalContributions(Float previousContributionsAmount, Float newAmount) {
        return previousContributionsAmount + newAmount;
    }

    private Float adjustNewAmount(Float previousContributionsAmount, Float newAmount, Integer ceiling) {
        if (previousContributionsAmount + newAmount > ceiling.floatValue()) {
            return ceiling.floatValue() - previousContributionsAmount;
        }
        return newAmount;
    }

    private Transaction calculateCurrentValuesBeforeSaveTransaction(TransactionDto transactionDto, Account account) {
        float previousOperationAmount = 0;
        Contribution contribution = null;

        if (transactionDto.getOperationType() == DEPOSIT) {
            contribution = checkAndUpdateContributionAmount(transactionDto, account);
            // we compute the the contribution value just in case of a deposit kind of
            // transaction
            previousOperationAmount = account.getAmount() - transactionDto.getAmount();
        } else if (transactionDto.getOperationType() == WITHDRAWAL) {
            previousOperationAmount = account.getAmount() + transactionDto.getAmount();
        }
        TransactionBuilder transactionBuilder = Transaction.builder()
                .amount(transactionDto.getAmount())
                .type(transactionDto.getOperationType())
                .planType(transactionDto.getPlanType())
                .comment(transactionDto.getComment())
                .account(account)
                .previousAmount(previousOperationAmount)
                .nextAmount(account.getAmount())
                .createdAt(LocalDateTime.now());

        if (contribution != null) {
            transactionBuilder.contribution(contribution);
        }

        Transaction transaction = transactionBuilder.build();
        contribution.setTransaction(transaction);

        return transaction;
    }

}
