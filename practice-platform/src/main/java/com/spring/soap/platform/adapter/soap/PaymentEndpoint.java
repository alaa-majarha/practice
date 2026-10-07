package com.spring.soap.platform.adapter.soap;

import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentRequest;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentResponse;
import org.apache.camel.ProducerTemplate;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class PaymentEndpoint {

    private static final String NAMESPACE = "http://bmct.com/makePayment";

    private final ProducerTemplate producerTemplate;
    private final SoapPaymentMapper mapper;

    public PaymentEndpoint(ProducerTemplate producerTemplate, SoapPaymentMapper mapper) {
        this.producerTemplate = producerTemplate;
        this.mapper = mapper;
    }

    @PayloadRoot(
            namespace = NAMESPACE,
            localPart = "MakePaymentRequest")
    @ResponsePayload
    public MakePaymentResponse makePayment(
            @RequestPayload MakePaymentRequest request) {

        PaymentResult result = producerTemplate.requestBody(
                "direct:payment", mapper.toCommand(request), PaymentResult.class);

        return mapper.toResponse(result);
    }
}
