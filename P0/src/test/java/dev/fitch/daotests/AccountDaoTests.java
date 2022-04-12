package dev.fitch.daotests;

import dev.fitch.data.AccountDAO;
import dev.fitch.data.AccountDAOPostgresImpl;
import dev.fitch.data.ClientDAO;
import dev.fitch.data.ClientDAOPostgresImpl;
import dev.fitch.entities.Account;
import dev.fitch.entities.Client;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)// to run tests in order

public class AccountDaoTests {

    static ClientDAO clientDAO = new ClientDAOPostgresImpl();
    static AccountDAO accountDAO = new AccountDAOPostgresImpl();
    static Account testAccount = null;

    @Test
    @Order(1)
    void create_account_with_initial_balance_test(){
        Client js = new Client("jsmith", "John", "Smith", "jsmith1", "123 Main Street", "jsmith@jsmith.com");
        clientDAO.createClient(js);
        Account jsAccount = new Account( "Checking", "jsmith", 1000);
        Account savedAccount = accountDAO.createAccount(jsAccount);
        AccountDaoTests.testAccount = savedAccount;
        Assertions.assertNotEquals(0, savedAccount.getAccountNumber()); //If this account is set up, it will be assigned an account number (and will no longer be 0)
    }


    @Test
    @Order(2)
    void create_account_with_zero_balance_test(){
        Client sd = new Client("ruhroh83", "Scooby", "Doo", "ScoobySnacks!", "1 Crystal Cove", "scoobs@scared.com");
        clientDAO.createClient(sd);
        Account sdAccount = new Account("Savings", "ruhroh83");
        Account savedAccount = accountDAO.createAccount(sdAccount);
        AccountDaoTests.testAccount = savedAccount;;
        Assertions.assertNotEquals(0, savedAccount.getAccountNumber()); //If this account is set up, it will be assigned an account number (and will no longer be 0)
        //System.out.println(testAccount);
        //System.out.println(AccountDaoTests.testAccount.getAccountNumber());
    }

    @Test
    @Order(3)
    void get_account_details_test(){
        testAccount = accountDAO.getAccountDetails(AccountDaoTests.testAccount.getAccountNumber());// Why does "AccountDaoTests.testAccount.getAccountNumber());" not work (but "4" does...)?
        Assertions.assertEquals("ruhroh83", testAccount.getOwnerOne());
    }


    @Test
    @Order(4)
    void update_account_test(){
        testAccount = accountDAO.getAccountDetails(AccountDaoTests.testAccount.getAccountNumber());// Why does "AccountDaoTests.testAccount.getAccountNumber());" not work (but "4" does...)?
        AccountDaoTests.testAccount.setOwnerTwo("jsmith");
        accountDAO.updateAccount(testAccount);
        Account retrievedAccount = accountDAO.getAccountDetails(testAccount.getAccountNumber());
        Assertions.assertEquals("jsmith", retrievedAccount.getOwnerTwo());
    }

    @Test
    @Order(5)
    void delete_account_test(){
        boolean result = accountDAO.closeAccount(AccountDaoTests.testAccount.getAccountNumber()); // true if successful----still have to use the actual account numbers...
        Assertions.assertTrue(result);

    }






}
