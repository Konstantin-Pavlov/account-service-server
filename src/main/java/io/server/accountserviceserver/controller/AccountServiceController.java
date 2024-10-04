package io.server.accountserviceserver.controller;

import io.server.accountserviceserver.dto.UpdateAccountBalanceRequest;
import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("account-service")
public class AccountServiceController {
    private final AccountService accountService;

    @GetMapping("get-amount/{account_id}")
    public BigDecimal getAmount(@PathVariable Integer account_id) {
        Account account = accountService.getAccountById(account_id);
        return account.getBalance();
    }

    @PatchMapping("add-amount")
    public ResponseEntity<Account> addAmount(@RequestBody UpdateAccountBalanceRequest request) {
        Account account = accountService.updateAccountBalance(request.getAccountId(), request.getAmount());
        return ResponseEntity.status(HttpStatus.OK).body(account);
    }
}
