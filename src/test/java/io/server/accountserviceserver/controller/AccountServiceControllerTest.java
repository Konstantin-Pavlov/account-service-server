package io.server.accountserviceserver.controller;

import io.server.accountserviceserver.dto.UpdateAccountBalanceRequest;
import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.exception.AccountNotFoundException;
import io.server.accountserviceserver.service.AccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class AccountServiceControllerTest {

    @Mock
    AccountService accountService;
    @InjectMocks
    AccountServiceController accountServiceController;
    private AutoCloseable closeable;
    private Account account;
    private static final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "Account with id %d not found";


    @BeforeEach
    void setUp() {
//        closeable = MockitoAnnotations.openMocks(this);
        account = new Account(1, BigDecimal.valueOf(10), LocalDateTime.now());
    }

    @AfterEach
    void tearDown() throws Exception {
//        closeable.close();
    }

    @Test
    void getAccountByIdTest() {
        Mockito.when(accountService.getAccountById(1)).thenReturn(account);

        Account accountById = accountService.getAccountById(1);

        Assertions.assertEquals(account, accountById);
    }

    @Test
    void getAmount() throws Exception {
        Mockito.when(accountService.getAccountById(1)).thenReturn(account);
        Account accountById = accountService.getAccountById(1);
        Assertions.assertEquals(account.getBalance(), accountById.getBalance());
    }

    @Test
    void addAmount() {
        UpdateAccountBalanceRequest request = new UpdateAccountBalanceRequest();
        request.setAccountId(1);
        request.setAmount(BigDecimal.valueOf(5));

        Mockito.when(accountService.updateAccountBalance(Mockito.eq(1), Mockito.any(BigDecimal.class)))
                .thenReturn(new Account(1, BigDecimal.valueOf(15), LocalDateTime.now()));

        ResponseEntity<Account> response = accountServiceController.addAmount(request);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody(), "Response body should not be null");
        Assertions.assertNotNull(response.getBody().getBalance(), "Balance should not be null");
        Assertions.assertEquals(BigDecimal.valueOf(15), response.getBody().getBalance());

    }

    @Test
    void getAmountAccountNotFound() {
        Mockito.when(accountService.getAccountById(2)).thenThrow(new AccountNotFoundException(
                String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, 2)
        ));

        Exception exception = Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountServiceController.getAmount(2);
        });

        Assertions.assertEquals(String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, 2), exception.getMessage());
    }

    @Test
    void addAmountAccountNotFound() {
        UpdateAccountBalanceRequest request = new UpdateAccountBalanceRequest();
        request.setAccountId(2);
        request.setAmount(BigDecimal.valueOf(5));

        Mockito.when(accountService.updateAccountBalance(Mockito.eq(2), Mockito.any(BigDecimal.class)))
                .thenThrow(new AccountNotFoundException(
                        String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, 2)
                ));

        Exception exception = Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountServiceController.addAmount(request);
        });

        Assertions.assertEquals(String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, 2), exception.getMessage());
    }
}