package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private Credential credential;

    private List<Account> accounts;
    private List<Transaction> transactions;

}
