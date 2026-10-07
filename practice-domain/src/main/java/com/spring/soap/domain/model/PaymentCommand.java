package com.spring.soap.domain.model;

import java.math.BigDecimal;

public record PaymentCommand(String payerAccount,
                             String beneficiaryAccount,
                             BigDecimal amount,
                             String bankShortName) {
}
