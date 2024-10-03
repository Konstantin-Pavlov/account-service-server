package io.server.accountserviceserver.repository;

import io.server.accountserviceserver.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
