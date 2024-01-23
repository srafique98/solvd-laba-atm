package com.solvd.laba.util;

import com.solvd.laba.domain.Credential;

public class CredentialUtil {

    public Credential createNewCredential(String pin, String accountNumber) {
        Credential credential = new Credential();
        if (isValidPin(pin)) {
            credential.setPin(pin);
        } else {
            throw new IllegalArgumentException("Invalid pin");
        }

        if (isValidAccountNumber(accountNumber)) {
            credential.setAccountNumber(accountNumber);
        } else {
            throw new IllegalArgumentException("Invalid account number");
        }
        return credential;
    }

    private boolean isValidPin(String pin) {
        return pin != null && isNumeric(pin) && pin.length() <= 4;
    }

    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && isNumeric(accountNumber) && accountNumber.length() <= 10;
    }

    private boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}
