package com.solvd.laba.service.interfaces;

import com.solvd.laba.domain.Account;

public interface AccountService {
    void create(Account account, Long userID);

    Account findById(Long id);

    void updateById(Account account);
}
