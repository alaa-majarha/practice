package com.spring.soap.platform.adapter.persistence.mapper;

import com.spring.soap.domain.model.Account;
import com.spring.soap.platform.adapter.persistence.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toDomain(AccountEntity entity) {
        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getName(),
                entity.getBalance(),
                null,
                null);
    }

    public AccountEntity toEntity(Account account) {
        AccountEntity entity = new AccountEntity();
        entity.setId(account.id());
        entity.setAccountNumber(account.accountNumber());
        entity.setName(account.name());
        entity.setBalance(account.balance());
        return entity;
    }
}
