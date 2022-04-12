package dev.fitch.entities;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.abs;

public class Transaction {

    public static final DecimalFormat df = new DecimalFormat("$###,###,###.00");

    int transactionNumber;
    int accountNumber;
    long date;
    String transactionType;
    double amount;
    double newBalance;

    public Transaction(int transactionNumber, int accountNumber, long date, String transactionType, double amount, double newBalance) {
        this.transactionNumber = transactionNumber;
        this.accountNumber = accountNumber;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
        this.newBalance = newBalance;
    }

    public Transaction(int transactionNumber, int accountNumber, long date, String transactionType, double amount) {
        this.transactionNumber = transactionNumber;
        this.accountNumber = accountNumber;
        this.date = date;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public Transaction() {

    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    @Override
    public String toString() {
        if (transactionType.equals("Withdrawal")) {
            return "Transaction ID: " + transactionNumber +
                    "\nDate: " +  new Date(date).toString() +
                    "\nTransaction Type: " + transactionType +
                    "\nAmount Withdrawn: " + df.format(abs(amount)) +
                    "\nNew Balance: " + df.format(newBalance) +
                    "\n\n";
        }else if (transactionType.equals("Deposit")){
            return "Transaction ID: " + transactionNumber +
                    "\nDate: " + new Date(date).toString() +
                    "\nTransaction Type: " + transactionType +
                    "\nAmount Deposited: " + df.format(abs(amount)) +
                    "\nNew Balance: " + df.format(newBalance) +
                    "\n\n";
        } else if (transactionType.equals("Outgoing Transfer")){
            return "Transaction ID: " + transactionNumber +
                    "\nDate: " + new Date(date).toString() +
                    "\nTransaction Type: " + transactionType +
                    "\nAmount Sent: " + df.format(abs(amount)) +
                    "\nNew Balance: " + df.format(newBalance) +
                    "\n\n";
        }else{
            return "Transaction ID: " + transactionNumber +
                    "\nDate: " + new Date(date).toString() +
                    "\nTransaction Type: " + transactionType +
                    "\nAmount Received: " + df.format(abs(amount)) +
                    "\nNew Balance: " + df.format(newBalance) +
                    "\n\n";
        }
    }
}
