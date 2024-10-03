package io.server.accountserviceserver.service;

import io.server.accountserviceserver.entity.Account;

public interface AccountService {
    Account getAccountById(Integer accountId);

    Account updateAccountBalance(Integer accountId, Integer amount);
}
