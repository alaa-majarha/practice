package com.spring.soap.platform.config;

import com.spring.soap.domain.port.AccountPort;
import com.spring.soap.domain.service.AccountService;
import com.spring.soap.domain.service.PaymentService;
import com.spring.soap.domain.usecase.GetAccountInfo;
import com.spring.soap.domain.usecase.OffusPayment;
import com.spring.soap.domain.usecase.OnusPayment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The domain module carries no Spring annotations, so its services are declared as beans here.
 */
@Configuration
public class DomainConfig {

    @Bean
    public OnusPayment onusPayment(AccountPort accountPort) {
        return new OnusPayment(accountPort);
    }

    @Bean
    public OffusPayment offusPayment() {
        return new OffusPayment();
    }

    @Bean
    public PaymentService paymentService(OnusPayment onusPayment, OffusPayment offusPayment) {
        return new PaymentService(onusPayment, offusPayment);
    }

    @Bean
    public GetAccountInfo getAccountInfo() {
        return new GetAccountInfo();
    }

    @Bean
    public AccountService accountService(GetAccountInfo getAccountInfo) {
        return new AccountService(getAccountInfo);
    }
}
