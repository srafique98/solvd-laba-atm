package com.solvd.laba.persistence.interfaces;

import com.solvd.laba.domain.TransactionDetail;

public interface TransactionDetailRespository {

    void create(TransactionDetail transactionDetail);

    TransactionDetail findById(Long id);

    void updateById(TransactionDetail transactionDetail);
}
