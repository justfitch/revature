package dev.fitch.services;

import dev.fitch.data.AccountDAO;
import dev.fitch.entities.Account;
import dev.fitch.utilities.List;

public class AccountServiceImpl implements AccountService{

    private AccountDAO accountDAO;

    public AccountServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public Account createAccount(String accountType, String primaryUser, String secondUser) {
        Account account = new Account(accountType, primaryUser, secondUser);
        return this.accountDAO.createAccount(account);
    }

    @Override
    public Account getAccountDetails(int accountNumber) {
        return this.accountDAO.getAccountDetails(accountNumber);
    }

    @Override
    public double checkBalance(int accountNumber) {
       Account account = accountDAO.getAccountDetails(accountNumber);
       return account.getBalance();
    }

    @Override
    public Account updateAccount(Account account) {
        return accountDAO.updateAccount(account);
    }

    @Override
    public double updateBalance(int accountNumber, double amount) {
        Account account = accountDAO.getAccountDetails(accountNumber);
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        accountDAO.updateAccount(account);
        return newBalance;
    }

    @Override
    public List<Account> findAccounts(String username) {
        return accountDAO.findAccounts(username);
    }

    @Override
    public Account verifyAccount(int accountNumber) {
        Account account = accountDAO.verifyAccount(accountNumber);
        return account;
    }
}
