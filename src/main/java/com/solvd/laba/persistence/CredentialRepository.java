package com.solvd.laba.persistence;

import com.solvd.laba.domain.Credential;

public interface CredentialRepository {
    void create(Credential credential);

    Credential findById(Long id);

    void updateById(Credential credential);
}
