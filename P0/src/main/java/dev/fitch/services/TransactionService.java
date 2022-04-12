package dev.fitch.services;

import dev.fitch.entities.Transaction;
import dev.fitch.utilities.List;

public interface TransactionService {

    Transaction withdrawal (int account, double amount, double new_balance);

    Transaction deposit (int account, double amount, double new_balance);

    List<Transaction> transactionHistory (int account);

    Transaction transferOut (int account, double amount, double new_balance);

    Transaction transferIn (int account, double amount, double new_balance);

}
