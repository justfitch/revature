package dev.fitch.daotests;


import dev.fitch.data.*;
import dev.fitch.entities.Account;
import dev.fitch.entities.Client;
import dev.fitch.entities.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// to run tests in order

public class TransactionDaoTests {

    static ClientDAO clientDAO = new ClientDAOPostgresImpl();

    static AccountDAO accountDAO = new AccountDAOPostgresImpl();

    static TransactionDAO transactionDAO = new TransactionDAOPostgresImpl();
    static Transaction testTransaction = null;

    @Test
    void create_new_transaction(){
        Client tm = new Client("TransactionMan", "Transaction", "Man", "transman", "123 Main St.", "trans@gmail.com");
        clientDAO.createClient(tm);

        Account tmacct = new Account( "Checking", "TransactionMan", 1000);
        accountDAO.createAccount(tmacct);

        Transaction testTransaction = new Transaction(0, tmacct.getAccountNumber(), System.currentTimeMillis(), "Deposit", 1000.53);
        Transaction savedTransaction = transactionDAO.newTransaction(testTransaction);
        TransactionDaoTests.testTransaction = savedTransaction;
        Assertions.assertNotEquals(0, savedTransaction.getTransactionNumber()); //Once again...the Assertion doesn't work, but the transaction is in the DB correctly...
    }
}
