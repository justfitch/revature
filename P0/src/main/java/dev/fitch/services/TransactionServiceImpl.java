package dev.fitch.services;

import dev.fitch.data.TransactionDAO;
import dev.fitch.entities.Transaction;
import dev.fitch.utilities.ArrayList;
import dev.fitch.utilities.List;

public class TransactionServiceImpl implements TransactionService {

    private TransactionDAO transactionDAO;

    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    @Override
    public Transaction withdrawal(int account, double amount, double newBalance){  //Take in amount to withdraw
        Transaction transaction = new Transaction(0, account, System.currentTimeMillis(), "Withdrawal", -amount, newBalance);
        return transactionDAO.newTransaction(transaction);
    }

    @Override
    public Transaction deposit(int account, double amount, double newBalance) {
        Transaction transaction = new Transaction(0, account, System.currentTimeMillis(), "Deposit", amount, newBalance);
        return transactionDAO.newTransaction(transaction);
    }

    @Override
    public List<Transaction> transactionHistory(int account) {
        List<Transaction> transactions = new ArrayList();
        transactions = transactionDAO.listTransactions(account);
        return transactions;
    }

    @Override
    public Transaction transferOut(int account, double amount, double newBalance) {
        Transaction transaction = new Transaction(0, account, System.currentTimeMillis(), "Outgoing Transfer", -amount, newBalance);
        return transactionDAO.newTransaction(transaction);
    }

    @Override
    public Transaction transferIn(int account, double amount, double newBalance) {
        Transaction transaction = new Transaction(0, account, System.currentTimeMillis(), "Incoming Transfer", amount, newBalance);
        return transactionDAO.newTransaction(transaction);
    }
}