package io.server.accountserviceserver.controller;

import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.exception.AccountNotFoundException;
import io.server.accountserviceserver.exception.ErrorResponseBody;
import io.server.accountserviceserver.service.AccountService;
import io.server.accountserviceserver.service.ErrorService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountServiceController.class)
class AccountServiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private ErrorService errorService;

    private static final String ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE = "Account with id %d not found";

    @Test
    void getAmount() throws Exception {
        Account account = new Account(1, BigDecimal.valueOf(10), LocalDateTime.now());

        Mockito.when(accountService.getAccountById(1)).thenReturn(account);

        mockMvc.perform(get("/account-service/get-amount/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));
    }

    @Test
    void getAmountAccountNotFound() throws Exception {
        int nonExistentAccountId = 1000;
        String errorMessage = String.format(ACCOUNT_NOT_FOUND_EXCEPTION_MESSAGE, nonExistentAccountId);

        Mockito.when(accountService.getAccountById(nonExistentAccountId))
                .thenThrow(new AccountNotFoundException(errorMessage));

        Mockito.when(errorService.makeResponse(any(AccountNotFoundException.class)))
                .thenReturn(ErrorResponseBody.builder()
                        .title(errorMessage)
                        .reasons(Map.of("errors", List.of(errorMessage)))
                        .build());

        mockMvc.perform(get("/account-service/get-amount/{id}", nonExistentAccountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value(errorMessage))
                .andExpect(jsonPath("$.reasons.errors[0]").value(errorMessage));
    }

}