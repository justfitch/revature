package dev.fitch.data;

import dev.fitch.entities.Account;
import dev.fitch.entities.Transaction;
import dev.fitch.utilities.List;

public interface TransactionDAO {

    //Create
        Transaction newTransaction(Transaction transaction);

    //Read
        List<Transaction> listTransactions(int account);

    //Update
    //I don't think transactions should be "updated"...

    //Delete
    //We will never delete transactions --- refunds, for example, will be a credit

}
