package com.solvd.laba.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Credential {
    private Long id;
    private String pin;
    private String accountNumber;
}
