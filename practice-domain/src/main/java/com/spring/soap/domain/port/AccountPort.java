package com.spring.soap.domain.port;

import com.spring.soap.domain.model.Account;

import java.util.Optional;

public interface AccountPort {

    Optional<Account> findByAccountNumber(String accountNumber);

    Account save(Account account);
}
