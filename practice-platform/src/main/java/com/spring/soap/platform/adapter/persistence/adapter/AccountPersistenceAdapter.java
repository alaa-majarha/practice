package com.spring.soap.platform.adapter.persistence.adapter;

import com.spring.soap.domain.model.Account;
import com.spring.soap.domain.port.AccountPort;
import com.spring.soap.platform.adapter.persistence.AccountJpaRepository;
import com.spring.soap.platform.adapter.persistence.mapper.AccountMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountPersistenceAdapter implements AccountPort {

    private final AccountJpaRepository repository;
    private final AccountMapper mapper;

    public AccountPersistenceAdapter(AccountJpaRepository repository, AccountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return repository.findByAccountNumber(accountNumber).map(mapper::toDomain);
    }

    @Override
    public Account save(Account account) {
        return mapper.toDomain(repository.save(mapper.toEntity(account)));
    }
}
