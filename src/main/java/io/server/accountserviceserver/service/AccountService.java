package io.server.accountserviceserver.service;

import io.server.accountserviceserver.entity.Account;

import java.math.BigDecimal;

public interface AccountService {
    Account getAccountById(Integer accountId);

    Account updateAccountBalance(Integer accountId, BigDecimal amount);
}
