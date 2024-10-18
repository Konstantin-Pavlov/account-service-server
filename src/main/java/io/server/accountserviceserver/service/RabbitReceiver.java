package io.server.accountserviceserver.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.server.accountserviceserver.dto.UpdateAccountBalanceRequest;
import io.server.accountserviceserver.entity.Account;
import io.server.accountserviceserver.exception.AccountNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RabbitReceiver {
    private final AccountService accountService;
    private final ObjectMapper objectMapper;

    //    @RabbitListener(queues = "${queue.name}")
//    @RabbitListener(queues = {"${queue.name}", "secondQueue", "hello"})
//    public void receive(String message) {
//        log.info("Received message '{}'", message);
//    }

    @RabbitListener(queues = "${queue.request.name}")
    public BigDecimal handleGetAmountRequest(Integer accountId) {
        // This handles the request asynchronously via RabbitMQ
        Account account = accountService.getAccountById(accountId);
        log.info("Received request for account '{}' balance", account);
        return account.getBalance();  // Respond with the balance, which RabbitMQ will send back
    }

    @RabbitListener(queues = "${queue.response.name}")
    public String handleUpdateAmountRequest(String requestJson) {
        try {
            // Deserialize the request JSON to UpdateAccountBalanceRequest
            UpdateAccountBalanceRequest request = objectMapper.readValue(requestJson, UpdateAccountBalanceRequest.class);
            // Process the request
            Account account = accountService.updateAccountBalance(request.getAccountId(), request.getAmount());
            // Create the response object
            UpdateAccountBalanceRequest response = new UpdateAccountBalanceRequest(account.getId(), account.getBalance());
            // Serialize the response object to JSON
            return objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize/deserialize request/response", e);
        } catch (AccountNotFoundException e) {
            return "Account not found";
        }

//        // This handles the request asynchronously via RabbitMQ
//        Account account = accountService.updateAccountBalance(updateAccountBalanceRequest.getAccountId(), updateAccountBalanceRequest.getAmount());
//        log.info("Updated balance for account: '{}'", account);
//        return new UpdateAccountBalanceRequest(account.getId(), account.getBalance());
////        return String.format("Updated balance for account: %s", account);
    }
}
