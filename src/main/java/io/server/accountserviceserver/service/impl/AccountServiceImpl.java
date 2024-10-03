package io.server.accountserviceserver.service.impl;

import io.server.accountserviceserver.AccountNotFoundException;
import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.repository.AccountRepository;
import io.server.accountserviceserver.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;


    @Override
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(
                        () -> new AccountNotFoundException(
                                String.format("Account with id %d not found", accountId)
                        )
                );
    }

    @Override
    public Account updateAccountBalance(Integer accountId, Integer amount) {
        int updatedRows = accountRepository.updateBalance(accountId, amount);
        if (updatedRows == 0) {
            throw new AccountNotFoundException(
                    String.format("Account with id %d not found", accountId)
            );
        }
        return accountRepository.findById(accountId)
                .orElseThrow(
                        () -> new AccountNotFoundException(
                                String.format("Account with id %d not found", accountId)
                        )
                );
    }
}
