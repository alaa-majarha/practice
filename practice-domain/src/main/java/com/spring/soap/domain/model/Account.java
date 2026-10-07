package com.spring.soap.domain.model;

import java.math.BigDecimal;

public record Account(Long id, String accountNumber, String name, BigDecimal balance, String currency, String status) {

    public Account credit(BigDecimal amount) {
        return new Account(id, accountNumber, name, balance.add(amount), currency, status);
    }

    public Account debit(BigDecimal amount) {
        return new Account(id, accountNumber, name, balance.subtract(amount), currency, status);
    }
}
