package com.solvd.laba;

import com.solvd.laba.domain.*;
import com.solvd.laba.service.impl.*;
import com.solvd.laba.service.interfaces.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        AtmService atmService = new AtmServiceImpl();
        UserService userService = new UserServiceImpl();
        CredentialService credentialService = new CredentialServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl();

//        testTransactionDao(transactionService);
//        testAccountDAO(accountService);
//        testCredentialDAO(credentialService);
//        tesUserDAO(userService);
        testAtmDao(atmService);

    }

    private static void testAtmDao(AtmService atmService) {
        List<Account> accountList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();
        List<User> userList = new ArrayList<>();

        Credential credential = createNewCredential("4532","8473620491");
        Account account = createNewAccount(332,"Checking",0);
        accountList.add(account);
        Transaction transaction = createNewTransaction(LocalDate.now().minusDays(2),400, "WithDrawal");
        transactionList.add(transaction);
        User user = createNewUser("User8",credential,accountList,transactionList);
        userList.add(user);

        Atm atm = new Atm();
        atm.setCity("Updated city1");
        atm.setUsers(userList);

//        atmService.create(atm,2L);

        Atm retrievedAtm = atmService.findById(1L);
        System.out.println("Retrieved Atm:\n" + retrievedAtm);

//        atm.setId(9L);
//        atmService.updateById(atm);

    }

    private static void tesUserDAO(UserService userService) {
        List<Account> accountList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

        Credential credential = createNewCredential("3842","3948393224");
        Account account = createNewAccount(7000,"Checking",0);
        accountList.add(account);
        Transaction transaction = createNewTransaction(LocalDate.now().minusDays(1),200, "WithDrawal");
        transactionList.add(transaction);

        User user = createNewUser("User7",credential,accountList,transactionList);
//        userService.create(user,1L, 1L);
        System.out.println(userService.findById(2L));
//        user.setName("Real name");
//        user.setId(1L);
//        userService.updateById(user);
    }

    private static void testCredentialDAO(CredentialService credentialService) {
        Credential credential = createNewCredential("3721","3729383271");
        credentialService.create(credential);
        credential.setPin("9876");
        credentialService.updateById(credential);
        System.out.println(credentialService.findById(9L));
    }

    private static void testAccountDAO(AccountService accountService) {
        Account account = createNewAccount(500.00,"Checking",0);
//        accountService.create(account,1L);

//        account.setId(1L);
//        accountService.updateById(account);
        System.out.println(accountService.findById(9L));
    }

    private static void testTransactionDao(TransactionService transactionService) {
        Transaction transaction = createNewTransaction(LocalDate.now(),100.0,"Deposit");
//        transactionService.create(transaction, 1L);
//        System.out.println("Transaction created:\n" + transaction);

        Transaction retrievedTransaction = transactionService.findById(2L);
        System.out.println("Retrieved Transaction:\n" + retrievedTransaction);
//
//        transaction.setType("Withdrawal");
//        transactionService.updateById(transaction);
//        System.out.println("Transaction updated:\n" + transaction);
    }

    private static User createNewUser(String name, Credential credential, List<Account> accounts, List<Transaction> transactions) {
        User newUser = new User();
        newUser.setName(name);
        newUser.setCredential(credential);
        newUser.setAccounts(accounts);
        newUser.setTransactions(transactions);
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
        if (type.equals("Checking")){
            newAccount.setInterestRate(0); }
        else {
            newAccount.setInterestRate(interestRate); }
        return  newAccount;
    }

    public static Credential createNewCredential(String pin, String accountNumber) {
        Credential credential = new Credential();
        if (credential.isValidPin(pin)) {
            credential.setPin(pin);
        } else {
            throw new IllegalArgumentException("Invalid pin");
        }

        if (credential.isValidAccountNumber(accountNumber)) {
            credential.setAccountNumber(accountNumber);
        } else {
            throw new IllegalArgumentException("Invalid account number");
        }
        return credential;
    }

    private static Transaction createNewTransaction(LocalDate date, double amount, String type) {
        if (!type.equals("WithDrawal") && !type.equals("Deposit") && !type.equals("Transfer")) {
            throw new IllegalArgumentException("Type must be 'WithDrawal', 'Deposit' or 'Transfer'.");
        }
        Transaction transaction = new Transaction();
        transaction.setDate(date);
        transaction.setAmount(amount);
        transaction.setType(type);
        return transaction;
    }


}