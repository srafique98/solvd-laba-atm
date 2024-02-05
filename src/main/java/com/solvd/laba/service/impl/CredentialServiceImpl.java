package com.solvd.laba.service.impl;

import com.solvd.laba.domain.Credential;
import com.solvd.laba.persistence.interfaces.CredentialRepository;
import com.solvd.laba.persistence.impl.CredentialDAO;
import com.solvd.laba.service.interfaces.CredentialService;

import java.util.UUID;

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
        return credentialRepository.findById(id);
    }

    @Override
    public void updateById(Credential credential) {
        credentialRepository.updateById(credential);

    }

    @Override
    public Credential findByAccountNumber(String accountNumber) {
        return credentialRepository.findByAccountNumber(accountNumber);
    }

    @Override
    public String generateUniqueAccountNumber(){
        String uniqueAccountNumber;
        do {
            uniqueAccountNumber = generateRandomNumber();
        } while (credentialRepository.findByAccountNumber(uniqueAccountNumber) != null);
        return uniqueAccountNumber;
    }

    private String generateRandomNumber() {
        UUID uuid = UUID.randomUUID();
        long longValue = Math.abs(uuid.getLeastSignificantBits());
        return String.format("%010d", longValue).substring(0, 10);
    }
}
