package com.spring.soap.platform.adapter.camel.handler;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.domain.service.PaymentService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Transaction boundary for payments consumed off the queue: the domain is framework-free, so
 * the debit and the credit are held in one transaction here rather than on the use case.
 */
@Component
public class PaymentQueueHandler {

    private final PaymentService paymentService;

    public PaymentQueueHandler(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Transactional
    public PaymentResult handle(PaymentCommand command) {
        return paymentService.makePayment(command);
    }
}
