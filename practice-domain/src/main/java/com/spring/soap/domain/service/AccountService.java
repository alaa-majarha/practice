package com.spring.soap.domain.service;

import com.spring.soap.domain.model.Account;
import com.spring.soap.domain.usecase.GetAccountInfo;

public class AccountService {

    private final GetAccountInfo getAccountInfo;

    public AccountService(GetAccountInfo getAccountInfo) {
        this.getAccountInfo = getAccountInfo;
    }

    public Account getAccountInfo(String accountNumber) {
        return getAccountInfo.invoke(accountNumber);
    }
}
