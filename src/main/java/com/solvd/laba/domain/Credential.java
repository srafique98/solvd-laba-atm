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

    public  boolean isValidPin(String pin) {
        return pin != null && isNumeric(pin) && pin.length() == 4;
    }

    public boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && isNumeric(accountNumber) && accountNumber.length() == 10;
    }

    public  boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
