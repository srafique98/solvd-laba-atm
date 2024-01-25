package com.solvd.laba;

import com.solvd.laba.domain.*;
import com.solvd.laba.service.impl.AccountServiceImpl;
import com.solvd.laba.service.impl.TransactionServiceImpl;
import com.solvd.laba.service.impl.UserServiceImpl;
import com.solvd.laba.service.interfaces.AccountService;
import com.solvd.laba.service.interfaces.CredentialService;
import com.solvd.laba.service.impl.CredentialServiceImpl;
import com.solvd.laba.service.interfaces.TransactionService;
import com.solvd.laba.service.interfaces.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        CredentialService credentialService = new CredentialServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        UserService userService = new UserServiceImpl();

        TransactionService transactionService = new TransactionServiceImpl();

        //Create New Credential
//        Credential c1  = createNewCredential("3829","8493846329");
//        credentialService.create(c1);
//
//        Credential c2  = createNewCredential("8765","3939234323");
//        c2.setId(2L);
//        credentialService.updateById(c2);
//
//        System.out.println(credentialService.findById(5L));

        // Account
//        Account a1 = createNewAccount(99.0, "Saving", .2 );
//        accountService.create(a1,3L);
//        Account a2 = createNewAccount(500, "Checking", .7 );
//        a2.setId(2L);
//        accountService.updateById(a2);

//        System.out.println(accountService.findById(5L));

        //User
//        Credential c2  = createNewCredential("3932","8493846329");
//        Account a3 = createNewAccount(88.0, "Saving", 0 );
//        List<Account> accounts = new ArrayList<>();

//        User a1 = createNewUser("User6", c2, .2 );


        //Transaction
        TransactionDetail td1 = createNewTransactionDetail(50.0,20,70,null,"Deposit");
        Transaction t1 = createNewTransaction(LocalDate.now(),td1);

        transactionService.create(t1,1L);



    }

    private static TransactionDetail createNewTransactionDetail(double amount, double preBalance, double postBalance, String transferTo, String type) {
        if (!isValidTransactionType(type)) {
            throw new IllegalArgumentException("Invalid transaction type. Allowed types are: WithDrawal, Deposit, Transfer");
        }
        if (type.equals("Transfer") && (transferTo == null || transferTo.isEmpty())) {
            throw new IllegalArgumentException("TransferTo must be specified for Transfer transactions.");
        }
        TransactionDetail td = new TransactionDetail();
        td.setAmount(amount);
        td.setPreBalance(preBalance);
        td.setPostBalance(postBalance);
        if (type.equals("Transfer")) {
            td.setTransferTo(transferTo);
        }
        td.setType(type);
        return td;
    }
    private static boolean isValidTransactionType(String type) {
        return type != null && (type.equals("WithDrawal") || type.equals("Deposit") || type.equals("Transfer"));
    }

    private static Transaction createNewTransaction(LocalDate date, TransactionDetail transactionDetail) {
        Transaction t1 = new Transaction();
        t1.setDate(date);
        t1.setTransactionDetail(transactionDetail);
        return t1;
    }

    private static User createNewUser(String name, Credential credential, List<Account> accounts, List<Transaction> transactions) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setCredential(credential);
        newUser.getAccounts().addAll(accounts);
        newUser.getTransactions().addAll(transactions);
        return newUser;

    }

    public static Account createNewAccount(double balance, String type, double interestRate ) {
        if (balance <= 0) {
            throw new IllegalArgumentException("Balance must be positive.");
        }
        if (!type.equals("Checking") && !type.equals("Saving")) {
            throw new IllegalArgumentException("Type must be 'Checking' or 'Savings'.");
        }
        Account newAccount = new Account();
        newAccount.setBalance(balance);
        newAccount.setType(type);
        newAccount.setInterestRate(interestRate);
        return  newAccount;
    }

    public static Credential createNewCredential(String pin, String accountNumber) {
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

    private static boolean isValidPin(String pin) {
        return pin != null && isNumeric(pin) && pin.length() == 4;
    }

    private static boolean isValidAccountNumber(String accountNumber) {
        return accountNumber != null && isNumeric(accountNumber) && accountNumber.length() == 10;
    }

    private static boolean isNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}