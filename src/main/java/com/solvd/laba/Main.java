package com.solvd.laba;

import com.solvd.laba.domain.Account;
import com.solvd.laba.domain.Credential;
import com.solvd.laba.domain.Transaction;
import com.solvd.laba.domain.User;
import com.solvd.laba.service.impl.*;
import com.solvd.laba.service.interfaces.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        CredentialService credentialService = new CredentialServiceImpl();
        AccountService accountService = new AccountServiceImpl();
        TransactionService transactionService = new TransactionServiceImpl();
        List<Account> accountList = new ArrayList<>();
        List<Transaction> transactionList = new ArrayList<>();

        System.out.println("Welcome to ATM");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your account number: ");
        String accountNumber = scanner.nextLine();

        Credential credential = credentialService.findByAccountNumber(accountNumber);
        if (credential == null) {
            System.out.println("Account not recognized. Do you want to create an account? (yes/no)");

            if (yesOrNo(scanner)) {
                System.out.print("Enter your name: ");
                String name = scanner.nextLine();

                accountNumber = credentialService.generateUniqueAccountNumber();
                System.out.println("Your new account number is: " + accountNumber);
                String pin = getUserPin(scanner);
                credential = createNewCredential(pin, accountNumber);
                credentialService.create(credential);

                User user = createNewUser(name, null, accountList, transactionList);
                userService.create(user, 1L, credential.getId());
                System.out.print("Creating a checking account with starting value of $25 dollars");

                Account account = createNewAccount(25.0, "Checking", 0);
                accountService.create(account, user.getId());
                System.out.println("Account created successfully.");
            } else {
                System.out.println("Exiting the program.");
                return;
            }
        }
        User foundUser = userService.findByCredentialID(credential.getId());
        System.out.println(foundUser);
        System.out.println("Welcome, " + foundUser.getName() + "!");
        System.out.print("Enter your PIN to perform a transaction: ");
        if (enterPinWithAttempts(scanner, foundUser)) {
            System.out.println("PIN entered correctly. You can now perform a transaction.");
        } else {
            System.out.println("Exiting the program due to multiple incorrect PIN attempts.");
            return;
        }

        while (true) {
            System.out.println("Choose an operation:");
            System.out.println("1. Withdrawal");
            System.out.println("2. Deposit");
            System.out.println("3. Transfer funds");
            System.out.println("4. Check Balance");
            System.out.println("5. Change PIN");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    double withDrawalAmount = appropriateWithdrawalAmount(scanner,foundUser);
                    double newBalance = foundUser.getAccounts().get(0).getBalance() - withDrawalAmount;
                    foundUser.getAccounts().get(0).setBalance(newBalance);
                    accountService.updateById(foundUser.getAccounts().get(0));
                    Transaction t1 = createNewTransaction(LocalDate.now(),withDrawalAmount,"WithDrawal");
                    transactionService.create(t1,foundUser.getId());
                    transactionList.add(t1);
                    foundUser.setTransactions(transactionList);
                    break;
                case 2:
                    userService.deposit(foundUser);
                    break;
                case 3:
                    userService.transferFunds(foundUser);
                    break;
                case 4:
                    foundUser = userService.findById(foundUser.getId());
                    System.out.println("Current balance: " + foundUser.getAccounts().get(0).getBalance());
                    userService.checkBalance(foundUser);
                    break;
                case 5:
//                    userService.changePin(foundUser);
                    credential.setPin(getUserPin(scanner));
                    credentialService.updateById(credential);
                    break;
                case 6:
                    System.out.println("Exiting the program.");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose a valid operation.");
            }
        }

    }

    private static double appropriateWithdrawalAmount(Scanner scanner, User foundUser) {
        double amount;
        double currentBalance = foundUser.getAccounts().get(0).getBalance();
        do {
            System.out.print("Enter an appropriate amount to withdrawal: ");
            amount = scanner.nextDouble();

            if (amount <= 0) {
                System.out.println("Cannot input a negative number or 0.");
            } else if (amount > foundUser.getAccounts().get(0).getBalance()) {
                System.out.println("Withdrawal amount exceeds account balance: " + currentBalance);
            }
        } while (amount <= 0 || amount > currentBalance);

        return amount;
    }


    private static boolean enterPinWithAttempts(Scanner scanner, User user) {
        int maxAttempts = 3;
        int attempts = 0;

        while (attempts < maxAttempts) {
            System.out.print("Enter your PIN: ");
            String enteredPin = scanner.nextLine();

            if (Objects.equals(user.getCredential().getPin(), enteredPin)) {
                return true;
            } else {
                attempts++;
                System.out.println("Incorrect PIN. Attempts remaining: " + (maxAttempts - attempts));
            }
        }
        return false;
    }

    private static String getUserPin(Scanner scanner) {
        String pin;
        Credential credential = new Credential();
        do {
            System.out.print("Choose a PIN: (4 digits) ");
            pin = scanner.nextLine();
        } while (!credential.isValidPin(pin));
        return pin;
    }

    private static boolean yesOrNo(Scanner scanner) {
        System.out.println("1. Yes");
        System.out.println("2. No");
        System.out.print("Enter your choice: ");
        int choice;
        while (true) {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                if (choice == 1 || choice == 2) {
                    break;
                } else {
                    System.out.print("Invalid choice. Enter 1 for Yes or 2 for No: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number: ");
            }
        }
        return choice == 1;
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

