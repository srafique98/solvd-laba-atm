package com.solvd.laba.persistence.interfaces;

import com.solvd.laba.domain.Account;

public interface AccountRepository {
    void create(Account account, Long userID);

    Account findById(Long id);

    void updateById(Account account);
}
