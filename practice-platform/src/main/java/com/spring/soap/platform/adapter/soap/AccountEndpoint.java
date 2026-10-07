package com.spring.soap.platform.adapter.soap;

import com.spring.soap.domain.model.Account;
import com.spring.soap.domain.service.AccountService;
import com.spring.soap.platform.adapter.soap.account.GetAccountInfoRequest;
import com.spring.soap.platform.adapter.soap.account.GetAccountInfoResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class AccountEndpoint {

    private static final String NAMESPACE = "http://example.com/account";

    private final AccountService accountService;

    public AccountEndpoint(AccountService accountService) {
        this.accountService = accountService;
    }

    @PayloadRoot(
            namespace = NAMESPACE,
            localPart = "GetAccountInfoRequest"
    )
    @ResponsePayload
    public GetAccountInfoResponse getAccountInfo(
            @RequestPayload GetAccountInfoRequest request) {

        Account account = accountService.getAccountInfo(request.getAccountNumber());

        GetAccountInfoResponse response = new GetAccountInfoResponse();

        response.setCustomerName(account.name());
        response.setBalance(account.balance());
        response.setCurrency(account.currency());
        response.setStatus(account.status());

        return response;
    }
}
