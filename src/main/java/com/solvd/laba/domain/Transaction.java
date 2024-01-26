package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;


@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {
    private Long id;
    private LocalDate date;
    private double amount;
    private String type;
}
