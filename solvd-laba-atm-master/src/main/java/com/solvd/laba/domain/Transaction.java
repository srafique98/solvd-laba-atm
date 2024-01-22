package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.security.Timestamp;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {
    private long id;
    private LocalDate date;
   private TransactionDetail transactionDetailId;

}
