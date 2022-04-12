package dev.fitch.data;

import dev.fitch.entities.Account;
import dev.fitch.utilities.List;

public interface AccountDAO {

    //create
    Account createAccount(Account account);

    //read
    Account getAccountDetails(int accountNumber);

    //update
    Account updateAccount(Account account);

    //delete
    boolean closeAccount (int accountNumber);

    List<Account> findAccounts(String username);

    Account verifyAccount(int accountNumber);
}
