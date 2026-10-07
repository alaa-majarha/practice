package com.spring.soap.platform.adapter.persistence.mapper;

import com.spring.soap.domain.model.Account;
import com.spring.soap.platform.adapter.persistence.entity.AccountEntity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AccountMapperTest {

    private final AccountMapper mapper = new AccountMapper();

    @Test
    void toDomainCopiesFieldsAndLeavesCurrencyAndStatusNull() {
        AccountEntity entity = new AccountEntity();
        entity.setId(7L);
        entity.setAccountNumber("ACC-1");
        entity.setName("Alaa");
        entity.setBalance(new BigDecimal("100.50"));

        Account account = mapper.toDomain(entity);

        assertEquals(7L, account.id());
        assertEquals("ACC-1", account.accountNumber());
        assertEquals("Alaa", account.name());
        assertEquals(new BigDecimal("100.50"), account.balance());
        assertNull(account.currency());
        assertNull(account.status());
    }

    @Test
    void toEntityCopiesFields() {
        Account account = new Account(3L, "ACC-2", "Sara", new BigDecimal("42"), "JOD", "ACTIVE");

        AccountEntity entity = mapper.toEntity(account);

        assertEquals(3L, entity.getId());
        assertEquals("ACC-2", entity.getAccountNumber());
        assertEquals("Sara", entity.getName());
        assertEquals(new BigDecimal("42"), entity.getBalance());
    }

    @Test
    void roundTripPreservesPersistedFields() {
        AccountEntity entity = new AccountEntity();
        entity.setId(1L);
        entity.setAccountNumber("ACC-3");
        entity.setName("Omar");
        entity.setBalance(BigDecimal.TEN);

        AccountEntity result = mapper.toEntity(mapper.toDomain(entity));

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getAccountNumber(), result.getAccountNumber());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getBalance(), result.getBalance());
    }
}
