package com.solvd.laba.persistence.interfaces;


import com.solvd.laba.domain.Transaction;

public interface TransactionRepository {
    void create(Transaction transaction, Long userID);

    Transaction findById(Long id);

    void updateById(Transaction transaction);
}
