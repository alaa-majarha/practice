package com.spring.soap.platform.adapter.soap;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentRequest;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentResponse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SoapPaymentMapperTest {

    private final SoapPaymentMapper mapper = new SoapPaymentMapper();

    @Test
    void toCommandMapsRequestFieldsAndConvertsAmountToBigDecimal() {
        MakePaymentRequest request = new MakePaymentRequest();
        request.setPayerAccount("PAYER");
        request.setBeneficiaryAccount("BENEF");
        request.setAmount(250L);
        request.setBankShortName("BMCT");

        PaymentCommand command = mapper.toCommand(request);

        assertEquals("PAYER", command.payerAccount());
        assertEquals("BENEF", command.beneficiaryAccount());
        assertEquals(BigDecimal.valueOf(250L), command.amount());
        assertEquals("BMCT", command.bankShortName());
    }

    @Test
    void toResponseLowercasesSuccess() {
        MakePaymentResponse response = mapper.toResponse(PaymentResult.SUCCESS);

        assertEquals("success", response.getStatus());
    }

    @Test
    void toResponseLowercasesFailed() {
        MakePaymentResponse response = mapper.toResponse(PaymentResult.FAILED);

        assertEquals("failed", response.getStatus());
    }
}
