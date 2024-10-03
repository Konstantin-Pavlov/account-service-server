package io.server.accountserviceserver.service;

import io.server.accountserviceserver.entity.Account;

import java.util.List;

public interface AccountService {
    public long countAccounts();

    List<Account> getAccounts();
}
