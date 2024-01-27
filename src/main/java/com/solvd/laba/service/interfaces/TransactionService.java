package com.solvd.laba.service.interfaces;

import com.solvd.laba.domain.Transaction;

public interface TransactionService {
    void create(Transaction transaction, Long userID);

    Transaction findById(Long id);

    void updateById(Transaction transaction);
}
