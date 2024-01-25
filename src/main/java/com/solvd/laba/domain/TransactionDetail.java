package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionDetail {
    private Long id;
    private double amount;
    private double preBalance;
    private double postBalance;
    private String transferTo;
    private String type;

}
