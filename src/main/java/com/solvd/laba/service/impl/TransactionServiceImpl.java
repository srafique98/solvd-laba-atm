package com.solvd.laba.service.impl;

import com.solvd.laba.domain.Transaction;
import com.solvd.laba.persistence.impl.TransactionDAO;
import com.solvd.laba.persistence.interfaces.TransactionRepository;
import com.solvd.laba.service.interfaces.TransactionService;

public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl() {
        this.transactionRepository = new TransactionDAO();
    }

    @Override
    public void create(Transaction transaction, Long userID) {
        this.transactionRepository.create(transaction,userID);

    }
    @Override
    public Transaction findById(Long id) {
        return this.transactionRepository.findById(id);
    }

    @Override
    public void updateById(Transaction transaction) {
        this.transactionRepository.updateById(transaction);

    }
}
