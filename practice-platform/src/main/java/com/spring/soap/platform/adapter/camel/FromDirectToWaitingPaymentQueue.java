package com.spring.soap.platform.adapter.camel;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FromDirectToWaitingPaymentQueue extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:payment")
                .routeId("payment-dispatch")
                .log("Received payment from direct")
                .marshal().json()
                .to("jms:queue:WaitingPayment");
    }
}
