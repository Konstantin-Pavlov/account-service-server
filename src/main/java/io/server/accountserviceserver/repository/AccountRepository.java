package io.server.accountserviceserver.repository;

import io.server.accountserviceserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.balance = a.balance + :amount WHERE a.id = :accountId")
    int updateBalance(@Param("accountId") Integer accountId, @Param("amount") Integer amount);
}

