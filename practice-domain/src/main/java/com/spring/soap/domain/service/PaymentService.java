package com.spring.soap.domain.service;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.domain.usecase.OffusPayment;
import com.spring.soap.domain.usecase.OnusPayment;

import java.util.Objects;

import static com.spring.soap.domain.BankConstants.BANK_SHORT_NAME;

public class PaymentService {

    private final OnusPayment onusPayment;
    private final OffusPayment offusPayment;

    public PaymentService(OnusPayment onusPayment, OffusPayment offusPayment) {
        this.onusPayment = onusPayment;
        this.offusPayment = offusPayment;
    }

    public PaymentResult makePayment(PaymentCommand command) {
        if (Objects.equals(command.bankShortName(), BANK_SHORT_NAME)) {
            return onusPayment.invoke(command);
        } else {
            return offusPayment.invoke(command);
        }
    }
}
