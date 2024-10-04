package io.server.accountserviceserver.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAccountBalanceRequest {
    Integer accountId;
    BigDecimal amount;
}
