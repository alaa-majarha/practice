package com.spring.soap.domain.usecase;

import com.spring.soap.domain.model.Account;
import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.domain.port.AccountPort;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Optional;

public class OnusPayment {

    private static final Logger LOG = System.getLogger(OnusPayment.class.getName());

    private final AccountPort accountPort;

    public OnusPayment(AccountPort accountPort) {
        this.accountPort = accountPort;
    }

    public PaymentResult invoke(PaymentCommand command) {
        Optional<Account> payerAccount = accountPort.findByAccountNumber(command.payerAccount());
        Optional<Account> beneficiaryAccount = accountPort.findByAccountNumber(command.beneficiaryAccount());

        Account beneficiary = beneficiaryAccount.get().credit(command.amount());
        Account payer = payerAccount.get().debit(command.amount());

        accountPort.save(payer);
        accountPort.save(beneficiary);

        LOG.log(Level.INFO, "received onus payment request");
        return PaymentResult.SUCCESS;
    }
}
