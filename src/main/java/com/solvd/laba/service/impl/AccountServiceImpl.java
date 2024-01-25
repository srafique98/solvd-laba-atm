package com.solvd.laba.service.impl;

import com.solvd.laba.domain.Account;
import com.solvd.laba.persistence.impl.AccountDAO;
import com.solvd.laba.persistence.interfaces.AccountRepository;
import com.solvd.laba.service.interfaces.AccountService;

public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl() {
        this.accountRepository = new AccountDAO();
    }

    @Override
    public void create(Account account, Long userID) {
        accountRepository.create(account, userID);

    }

    @Override
    public Account findById(Long id) {
        return accountRepository.findById(id);
    }

    @Override
    public void updateById(Account account) {
        accountRepository.updateById(account);

    }
}
