package com.spring.soap.domain.usecase;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class OffusPayment {

    private static final Logger LOG = System.getLogger(OffusPayment.class.getName());

    public PaymentResult invoke(PaymentCommand command) {
        LOG.log(Level.INFO, "received offus payment request");
        return PaymentResult.SUCCESS;
    }
}
