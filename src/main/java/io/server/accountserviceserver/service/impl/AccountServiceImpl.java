package io.server.accountserviceserver.service.impl;

import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.repository.AccountRepository;
import io.server.accountserviceserver.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public long countAccounts() {
        return accountRepository.count();
    }


    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }
}
