package dev.fitch.servicetests;

import dev.fitch.api.App;
import dev.fitch.data.*;
import dev.fitch.entities.Account;
import dev.fitch.entities.Client;
import dev.fitch.entities.Transaction;
import dev.fitch.services.*;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class UtilityServiceTests {

    static ClientDAO clientDAO = new ClientDAOPostgresImpl();
    static ClientService clientService = new ClientServiceImpl(clientDAO);
    static Client testClient = null;

    static AccountDAO accountDAO = new AccountDAOPostgresImpl();
    static AccountService accountService = new AccountServiceImpl(accountDAO);
    public static Account testAccountOut;
    public static Account testAccountIn;

    static TransactionDAO transactionDAO = new TransactionDAOPostgresImpl();
    static TransactionService transactionService = new TransactionServiceImpl(transactionDAO);
    static Transaction testTransactionOut = null;
    static Transaction testTransactionIn = null;

    public int accountNumberOut;
    public double currentBalanceOut;
    public int accountNumberIn;
    public double currentBalanceIn;

    @Test
    @Order(1)
    void create_client() {
        Client user = new Client("User", "First", "Last", "password", "address", "email");
        Client savedClient = clientService.registerClient(user);

        testClient = clientService.getClientInfo(savedClient.getUsername());
        Assertions.assertNotNull(testClient);
    }

    @Test
    @Order(2)
    void create_accounts_and_transactions() {
        Account out = new Account("Test", "User",1000.00);
        Account in = new Account("Test","User",100.00);

        Account savedAccountOut = accountService.createAccount(out.getAccountType(), out.getOwnerOne(), out.getOwnerTwo());
        Account savedAccountIn  = accountService.createAccount(in.getAccountType(), in.getOwnerOne(), in.getOwnerTwo());

        testAccountOut = accountService.getAccountDetails(savedAccountOut.getAccountNumber());
        testAccountIn = accountService.getAccountDetails((savedAccountIn.getAccountNumber()));

        accountNumberOut = testAccountOut.getAccountNumber();
        currentBalanceOut = testAccountOut.getBalance();

        accountNumberIn = testAccountIn.getAccountNumber();
        currentBalanceIn = testAccountIn.getBalance();

        System.out.println(accountNumberOut);
        System.out.println(currentBalanceOut);

        System.out.println(accountNumberIn);
        System.out.println(currentBalanceIn);

        Assertions.assertNotNull(testAccountOut);
        Assertions.assertNotNull(testAccountIn);

        double amount = 500.00;

        //CREATE TRANSACTION RECORD FOR OUT ACCOUNT IN TRANSACTION TABLE
        testTransactionOut =  transactionService.transferOut(accountNumberOut, amount, (currentBalanceOut - amount));

        //CREATE TRANSACTION RECORD FOR ACCOUNT TO WHICH TRANSFER IS GOING IN TRANSACTION TABLE
        testTransactionIn = transactionService.transferIn(accountNumberIn, amount, (currentBalanceIn + amount));

        Assertions.assertEquals((currentBalanceOut - amount), testTransactionOut.getNewBalance());
        Assertions.assertEquals((currentBalanceOut + amount), testTransactionIn.getNewBalance());
    }
}
