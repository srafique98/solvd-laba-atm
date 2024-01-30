package com.solvd.laba.service.interfaces;

import com.solvd.laba.domain.User;

public interface UserService {
    void create(User user, Long atmID, Long credentialID);

    User findById(Long id);

    void updateById(User user);

    void withdrawal(User user);
    void deposit(User user);
    void transferFunds(User transferTo);
    void checkBalance(User user);
    void changePin(User user);
    User findByCredentialID(Long credentialID);
}
