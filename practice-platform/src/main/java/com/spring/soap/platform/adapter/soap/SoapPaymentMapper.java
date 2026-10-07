package com.spring.soap.platform.adapter.soap;

import com.spring.soap.domain.model.PaymentCommand;
import com.spring.soap.domain.model.PaymentResult;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentRequest;
import com.spring.soap.platform.adapter.soap.payment.MakePaymentResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;

@Component
public class SoapPaymentMapper {

    public PaymentCommand toCommand(MakePaymentRequest request) {
        // amount is xs:long on the wire; the domain works in BigDecimal.
        return new PaymentCommand(
                request.getPayerAccount(),
                request.getBeneficiaryAccount(),
                BigDecimal.valueOf(request.getAmount()),
                request.getBankShortName());
    }

    public MakePaymentResponse toResponse(PaymentResult result) {
        MakePaymentResponse response = new MakePaymentResponse();
        response.setStatus(result.name().toLowerCase(Locale.ROOT));
        return response;
    }
}
