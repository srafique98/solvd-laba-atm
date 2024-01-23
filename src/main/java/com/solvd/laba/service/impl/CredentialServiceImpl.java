package com.solvd.laba.service.impl;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.persistence.CredentialRepository;
import com.solvd.laba.persistence.impl.CredentialDAO;
import com.solvd.laba.service.CredentialService;

public class CredentialServiceImpl implements CredentialService {
    private final CredentialRepository credentialRepository;

    public CredentialServiceImpl( ) {
        this.credentialRepository = new CredentialDAO();
    }

    @Override
    public void create(Credential credential) {
        credentialRepository.create(credential);
    }

    @Override
    public Credential findById(Long id) {
        return null;
    }

    @Override
    public void updateById(Credential credential) {

    }
}
