package io.server.accountserviceserver.service.impl;

import io.server.accountserviceserver.AccountNotFoundException;
import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.repository.AccountRepository;
import io.server.accountserviceserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "Account with id %d not found";
    private final AccountRepository accountRepository;


    @Override
    public Account getAccountById(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                        () -> {
                            log.warn(String.format("account with id %d not found", accountId));
                            return new AccountNotFoundException(
                                    String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, accountId)
                            );
                        }
                );
        log.info(String.format("account with id %d and balance %s found",accountId, account.getBalance()));
        return account;
    }

    @Override
    public Account updateAccountBalance(Integer accountId, BigDecimal amount) {
        int updatedRows = accountRepository.updateBalance(accountId, amount);
        if (updatedRows == 0) {
            log.warn(String.format("account with id %d not found", accountId));
            throw new AccountNotFoundException(
                    String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, accountId)
            );
        }
        Account account = getAccountById(accountId);
        log.info(String.format("added %s to account with id %d; now account balance is %s", amount, accountId, account.getBalance()));
        return account;
    }
}
