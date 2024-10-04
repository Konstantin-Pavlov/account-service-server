package io.server.accountserviceserver.service;

import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.exception.AccountNotFoundException;
import io.server.accountserviceserver.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account(1, BigDecimal.valueOf(100), LocalDateTime.now());
    }

    @Test
    void getAccountById_AccountExists() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(1);

        assertNotNull(foundAccount, "Account should not be null");
        assertEquals(account.getId(), foundAccount.getId());
        assertEquals(account.getBalance(), foundAccount.getBalance());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void getAccountById_AccountNotFound() {
        when(accountRepository.findById(1)).thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(1));

        assertEquals("Account with id 1 not found", exception.getMessage());
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void updateAccountBalance_AccountExists() {
        when(accountRepository.updateBalance(eq(1), any(BigDecimal.class))).thenReturn(1);
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        Account updatedAccount = accountService.updateAccountBalance(1, BigDecimal.valueOf(50));

        assertNotNull(updatedAccount, "Account should not be null");
        assertEquals(account.getId(), updatedAccount.getId());
        verify(accountRepository, times(1)).updateBalance(eq(1), any(BigDecimal.class));
        verify(accountRepository, times(1)).findById(1);
    }

    @Test
    void updateAccountBalance_AccountNotFound() {
        when(accountRepository.updateBalance(eq(1), any(BigDecimal.class))).thenReturn(0);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () -> accountService.updateAccountBalance(1, BigDecimal.valueOf(50)));

        assertEquals("Account with id 1 not found", exception.getMessage());
        verify(accountRepository, times(1)).updateBalance(eq(1), any(BigDecimal.class));
    }
}