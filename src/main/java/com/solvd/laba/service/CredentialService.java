package com.solvd.laba.service;

import com.solvd.laba.domain.Credential;

public interface CredentialService {
    void create(Credential credential);

    Credential findById(Long id);

    void updateById(Credential credential);
}
