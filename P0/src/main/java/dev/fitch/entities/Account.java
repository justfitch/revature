package dev.fitch.entities;

public class Account {

    int accountNumber;
    String accountType;
    String ownerOne;
    String ownerTwo;
    double balance;

    public Account() {}

    public Account(String accountType, String ownerOne) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownerOne = ownerOne;
        this.balance = 0;
    }

    public Account(String accountType, String ownerOne, String ownerTwo) {
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ownerOne = ownerOne;
        this.ownerTwo = ownerTwo;
        this.balance = 0;
    }

    public Account(String accountType, String ownerOne, double balance) {
        this.accountType = accountType;
        this.ownerOne = ownerOne;
        this.balance = balance;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getOwnerOne() {
        return ownerOne;
    }

    public void setOwnerOne(String ownerOne) {
        this.ownerOne = ownerOne;
    }

    public String getOwnerTwo() {
        return ownerTwo;
    }

    public void setOwnerTwo(String ownerTwo) {
        this.ownerTwo = ownerTwo;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }


    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", accountType='" + accountType + '\'' +
                ", balance=" + balance +
                '}';
    }
}
