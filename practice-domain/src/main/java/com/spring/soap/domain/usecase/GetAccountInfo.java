package com.spring.soap.domain.usecase;

import com.spring.soap.domain.model.Account;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigDecimal;

/**
 * Still a stub: returns a fixed account regardless of the number asked for. The persistence
 * port exists and is wired for payments, but this read has never been connected to it.
 */
public class GetAccountInfo {

    private static final Logger LOG = System.getLogger(GetAccountInfo.class.getName());

    public Account invoke(String accountNumber) {
        LOG.log(Level.INFO, "Account number: " + accountNumber);

        return new Account(null, accountNumber, "John Smith", new BigDecimal("2500"), "USD", "ACTIVE");
    }
}
