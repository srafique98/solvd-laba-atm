package com.solvd.laba.persistence.interfaces;

import com.solvd.laba.domain.Credential;

public interface CredentialRepository {
    void create(Credential credential);

    Credential findById(Long id);

    void updateById(Credential credential);
    Credential findByAccountNumber(String accountNumber);
}
