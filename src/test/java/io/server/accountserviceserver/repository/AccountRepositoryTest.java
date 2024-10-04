package io.server.accountserviceserver.repository;

import io.server.accountserviceserver.entity.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Sql(scripts = "/schema.sql")
class AccountRepositoryTest {

    @Container
    public static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testUpdateBalance() {
        // Create and save a new account
        Account account = new Account(null, BigDecimal.valueOf(100), null);
        account = accountRepository.save(account);

        // Flush and clear the persistence context to ensure the account is saved
        entityManager.flush();
        entityManager.clear();

        // Update the account balance
        int updatedRows = accountRepository.updateBalance(account.getId(), BigDecimal.valueOf(50));
        assertEquals(1, updatedRows);

        // Flush and clear the persistence context to ensure the update is persisted
        entityManager.flush();
        entityManager.clear();

        // Fetch the updated account
        Optional<Account> updatedAccount = accountRepository.findById(account.getId());
        assertTrue(updatedAccount.isPresent());
        assertEquals(0, updatedAccount.get().getBalance().compareTo(BigDecimal.valueOf(150)));
    }

    @Test
    void testFindAccountById() {
        // Create and save a new account
        Account account = new Account(null, BigDecimal.valueOf(300), null);
        account = accountRepository.save(account);

        // Flush and clear the persistence context to ensure the account is saved
        entityManager.flush();
        entityManager.clear();

        // Fetch the account by ID
        Optional<Account> fetchedAccount = accountRepository.findById(account.getId());
        assertTrue(fetchedAccount.isPresent());
        assertEquals(0, fetchedAccount.get().getBalance().compareTo(BigDecimal.valueOf(300)));
    }
}