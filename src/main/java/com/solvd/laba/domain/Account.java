package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Account {
    private Long id;
    private double balance;
    private String type;
    private double interestRate;
    private User user;
}
