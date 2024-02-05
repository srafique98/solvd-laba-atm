package com.solvd.laba.service.impl;

import com.solvd.laba.domain.User;
import com.solvd.laba.persistence.impl.UserDAO;
import com.solvd.laba.persistence.interfaces.UserRepository;
import com.solvd.laba.service.interfaces.AccountService;
import com.solvd.laba.service.interfaces.CredentialService;
import com.solvd.laba.service.interfaces.TransactionService;
import com.solvd.laba.service.interfaces.UserService;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final CredentialService credentialService;
    private final TransactionService transactionService;

    public UserServiceImpl() {
        this.userRepository = new UserDAO();
        this.accountService = new AccountServiceImpl();
        this.credentialService = new CredentialServiceImpl();
        this.transactionService = new TransactionServiceImpl();

    }

    @Override
    public void create(User user, Long atmID, Long credentialID) {
        userRepository.create(user,atmID,credentialID);

        if (user.getCredential() != null) {
            credentialService.create(user.getCredential());
        }

        if (user.getAccounts() != null && !user.getAccounts().isEmpty()) {
            user.getAccounts().forEach(account -> {
                accountService.create(account,user.getId());
            });
        }

        if (user.getTransactions() != null && !user.getTransactions().isEmpty()) {
            user.getTransactions().forEach(transaction -> {
                transactionService.create(transaction,user.getId());
            });
        }

    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateById(User user) {
        userRepository.updateById(user);
    }

    public  User findByCredentialID(Long credentialID){
        return userRepository.findByCredentialID(credentialID);
    }


    @Override
    public void withdrawal(User user) {
        // need AccountService for withdrawal logic
//        accountService.withdrawal(user);
    }

    @Override
    public void deposit(User user) {
        // need AccountService for deposit logic
//        accountService.deposit(user);
    }

    @Override
    public void transferFunds(User transferTo) {
        // need AccountService for transfer logic
//        accountService.transferFunds(user);
    }

    @Override
    public void checkBalance(User user) {
        // need AccountService for balance checking logic
//        accountService.checkBalance(user);
    }

}
