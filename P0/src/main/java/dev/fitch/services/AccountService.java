package dev.fitch.services;

import dev.fitch.entities.Account;
import dev.fitch.utilities.List;

public interface AccountService {

    Account getAccountDetails(int accountNumber);

    double checkBalance(int username);

    double updateBalance(int accountNumber, double amount);

    Account updateAccount(Account account);

    Account createAccount(String accountType, String primaryUser, String secondUser);

    List<Account> findAccounts(String username);

    Account verifyAccount(int accountNumber);

}
