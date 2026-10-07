package com.spring.soap.platform.adapter.camel;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.platform.adapter.camel.handler.PaymentQueueHandler;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FetchWaitingPaymentQueue extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("jms:queue:WaitingPayment")
                .routeId("payment-consume")
                .unmarshal().json(PaymentCommand.class)
                .bean(PaymentQueueHandler.class, "handle");
    }
}
