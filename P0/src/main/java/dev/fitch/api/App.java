package dev.fitch.api;

import dev.fitch.data.*;
import dev.fitch.entities.Account;
import dev.fitch.entities.Client;
import dev.fitch.entities.Transaction;
import dev.fitch.services.*;
import dev.fitch.utilities.List;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Scanner;

public class App {

    public static ClientService clientService = new ClientServiceImpl(new ClientDAOPostgresImpl());
    public static TransactionService transactionService = new TransactionServiceImpl(new TransactionDAOPostgresImpl());
    public static AccountService accountService = new AccountServiceImpl(new AccountDAOPostgresImpl());

    public static final DecimalFormat df = new DecimalFormat("$###,###,###.00");

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        String username = null;
        int accountNumber = 0;
        int choice = 0;

        //SELECT USER
        while (username == null) {
            System.out.println("Welcome to Twelfth Sixth Bank Self-Service Terminal.\n");
            System.out.println("Please select and option from the list below:");
            System.out.println("1. Login as an existing user\n2. Register a new user");

            choice = scanner.nextInt();

            switch (choice) {
                case 1: {       //Login as existing user
                    username = logIn();
                    break;
                }

                case 2: {       //Register new user
                    username = registerClient();
                    break;
                }
            }
        }

        //SELECT ACCOUNT - ONLY THOSE REGISTERED TO SELECTED USER ARE AVAILABLE
        System.out.println("Welcome valued customer! Please select from the following choices:");
        System.out.println("1. Access existing account");
        System.out.println("2. Register a new account");

        choice = scanner.nextInt();

        switch (choice) {
            case 1: {        //Make transactions on an account that already exists in teh database
                accountNumber = selectAccount(username);
                break;
            }

            case 2: {        //Create a new account, then use that account for transactions
                accountNumber = registerNewAccount(username);
                break;
            }
        }

        //TRANSACTIONS ON SELECTED USER/ACCOUNT
        Boolean isExit = false;   //IS SET TO TRUE WHEN USER CHOOSES TO QUIT (NO MORE TRANSACTIONS)
        while (isExit == false) {  //LOOP TO DETERMINE WHEN TO END PROGRAM
            System.out.println("\n----------Account " + accountNumber + "----------\nPlease select from the following:");
            System.out.println("1. Make a withdrawal\n2. Make a deposit\n3. View account balance" +
                    "\n4. View transaction history\n5. Transfer funds\n6. Edit existing user/account information" +
                    "\n7. Change Accounts\n8. Quit");

            choice = scanner.nextInt();

            switch (choice) {
                case 1: {   //MAKE A WITHDRAWAL FROM CURRENT ACCOUNT
                    makeWithdrawal(accountNumber);
                    isExit = isExit();
                    break;
                }

                case 2: {   //MAKE A DEPOSIT INTO CURRENT ACCOUNT
                    makeDeposit(accountNumber);
                    isExit = isExit();
                    break;
                }

                case 3: {   //VIEW BALANCE OF CURRENT ACCOUNT
                    System.out.println("Your current account balance is: " + df.format(accountService.checkBalance(accountNumber)));
                    break;
                }

                case 4: {   //VIEW TRANSACTION HISTORY OF CURRENT ACCOUNT
                    viewTransactionHistory(accountNumber);
                    isExit = isExit();
                    break;
                }

                case 5: {    //TRANSFER FUNDS FROM CURRENT ACCOUNT TO ANOTHER EXISTING ACCOUNT
                    transferFunds(username, accountNumber);
                    isExit = isExit();
                    break;
                }

                case 6: {   //EDIT CURRENT USER OR ACCOUNT INFORMATION
                    System.out.println("Please chose from the following options:");
                    System.out.println("1. Update user information\n2. Add or Remove Second User on Account " + accountNumber);
                    choice = scanner.nextInt();

                    if (choice == 1) {      //UPDATE USER INFORMATION
                        updateClientInformation(username);
                    } else if (choice == 2) {   //UPDATED ACCOUNT INFORMATION (ONLY THING TO UPDATE IS 2ND USER)
                        updateSecondClient(accountNumber);
                    }
                    isExit = isExit();
                    break;
                }

                case 7: { //SWITCH TO A DIFFERENT ACCOUNT
                    accountNumber = selectAccount(username);
                    break;
                }

                case 8: {   //QUIT
                    isExit = true;
                    System.out.println("Thank you for being a valued customer! Have a wonderful day!");
                }
            }
        }
    }

    //LOG IN AS A REGISTERED USER
    private static String logIn() {
        Scanner scanner = new Scanner(System.in);
        String username = null;
        boolean isValid = false;
        //scanner.nextLine();
        while (isValid == false) {   //LOOP TO REPEAT INPUT UNTIL VALID USERNAME/PASSWORD ARE ENTERED
            System.out.println("Please enter username: ");
            username = scanner.nextLine();

            //Go back
            if (username.equals("back")){
                return null;
            }


            if (App.clientService.checkUsername(username)) {   //VERIFY THAT THIS IS A REGISTERED USERNAME
                isValid = true;
                System.out.println("Please enter password: ");
                String password = scanner.nextLine();

                boolean isExit = false;
                while (isExit == false) {     //LOOP TO INPUT PASSWORD AGAIN IF ATTEMPT IS INVALID
                    if (App.clientService.checkPassword(username, password) == true) {  //CHECK PASSWORD TO USERNAME
                        System.out.println("Login successful!\n");        //LOG IN USER IF PASSWORD IS CORRECT
                        isExit = true;
                    } else {                                           //PASSWORD IS INCORRECT- LOOP BACK TO TRY AGAIN
                        System.out.println("Invalid password. Please try again. Enter 'back' to go back.");
                        password = scanner.nextLine();

                        //Go Back
                        if (password.equals("back")){
                            return null;
                        }
                    }
                }
            } else {                                                   //USERNAME IS NOT REGISTERED
                System.out.println("Username is not registered. Please try again. Enter 'back' to go back.");
            }
        }
        return username;
    }

    //REGISTER A NEW USER
    private static String registerClient() {
        Scanner scanner = new Scanner(System.in);
        String username;
        System.out.println("Input Username:");
        username = scanner.nextLine();

        int nameTaken;
        do {    //RUN THIS LOOP UNTIL AN ACCEPTABLE (NOT ALREADY USED) USERNAME IS SELECTED
            if (App.clientService.checkUsername(username)) {   //IF USERNAME ALREADY EXISTS, PROMPT FOR DIFFERENT USERNAME
                System.out.println("That username is already taken. Please try a different username:");
                username = scanner.nextLine();
                break;
            } else {       //USERNAME IS AVAILABLE - MOVE ON TO COLLECT USER DATA
                break;
            }
        } while (App.clientService.checkUsername(username));

        //INPUT USER DATA
        System.out.println("Input first name:");
        //scanner.nextLine();
        String firstName = scanner.nextLine();

        System.out.println("Input last name:");
        //scanner.nextLine();
        String lastName = scanner.nextLine();

        System.out.println("Choose a password:");
        //scanner.nextLine();
        String password = scanner.nextLine();

        System.out.println("Input mailing address:");
        //scanner.nextLine();
        String address = scanner.nextLine();

        System.out.println("Input email address:");
        //scanner.nextLine();
        String email = scanner.nextLine();

        //CREATE NEW CLIENT
        Client client = new Client(username, firstName, lastName, password, address, email);
        App.clientService.registerClient(client);
        System.out.println("New user registered! Welcome to the Twelfth Sixth family!\n");
        return username;  //RETURN NEW USERNAME TO PROCEED TO ACCOUNT CREATION
    }

    //SELECT AND EXISTING ACCOUNT ASSOCIATED CURRENT USER
    private static int selectAccount(String username) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        int accountNumber = 0;
        List<Account> accounts = accountService.findAccounts(username);  //GET ACCOUNTS ASSOCIATED WITH USER FROM DB
        int numberOfAccounts = accounts.size();
        if (numberOfAccounts == 1) {    //IF THEY ONLY HAVE ONE ACCOUNT, AUTO-SELECT THAT ACCOUNT
            System.out.println("Accessing account # " + accounts.get(0) + "\n");
            accountNumber = accounts.get(0).getAccountNumber();
        } else if (numberOfAccounts == 0) {   //IF THEY HAVE NO ACCOUNT, TELL THEM AND THEN SEND TO ACCOUNT CREATION
            System.out.println("No account registered for this user. Please register an account.\n");
            accountNumber = registerNewAccount(username);
        } else if (numberOfAccounts > 1) {  //IF USER HAS MULTIPLE ACCOUNTS, LIST  AND LET THEM SELECT WHICH TO ACCESS
            boolean isAccountSelected = false;
            while (isAccountSelected == false) {  //LOOP TO VALIDATE ACCOUNT SELECTION
                System.out.println("***Please select an account***");
                for (int i = 0; i < numberOfAccounts; i++) {   //LIST ACCOUNTS ASSOCIATED WITH USERNAME
                    System.out.println((i + 1) + ". " + accounts.get(i).getAccountType().substring(0, 1).toUpperCase() + accounts.get(i).getAccountType().substring(1) + " Account No. " + accounts.get(i).getAccountNumber());
                }
                if (scanner.hasNextInt()) {  //USER SELECTS ACCOUNT
                    choice = scanner.nextInt();
                    if (choice <= numberOfAccounts) {
                        accountNumber = accounts.get(choice - 1).getAccountNumber();
                        isAccountSelected = true;
                    } else {
                        System.out.println("Invalid input. Please try again.");
                    }
                } else {
                    System.out.println("Invalid input. Please select the number for the account you would like to access");
                }
            }
        }
        return accountNumber;  //RETURN ACCOUNT NUMBER TO PERFORM TRANSACTIONS ON
    }

    //CREATE A NEW ACCOUNT
    private static int registerNewAccount(String username) {
        Scanner scanner = new Scanner(System.in);
        int accountNumber;
        int choice;
        String accountType = null;
        String secondUser = null;
        boolean isValid = false;
        while (isValid == false) {   //VALIDATE USER ENTRY IS 1 OR 2
            System.out.println("What type of account would you like to create?");
            System.out.println("1. Savings\n2. Checking");
            choice = scanner.nextInt();

            if (choice == 1) {
                accountType = "Savings";
                isValid = true;
            } else if (choice == 2) {
                accountType = "Checking";
                isValid = true;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }

        boolean isExit = false;
        while (isExit == false) { //LOOP BACK TO HERE IF USER SELECTS 1, BUT DOESN'T KNOW SECOND USERNAME
            System.out.println("Would you like to add a second registered user to this account?\n1. Yes\n2. No");
            choice = scanner.nextInt();

            isValid = false;
            while (isValid == false) {  //VALIDATE USER INPUT - IS SECOND USER A REGISTERED CLIENT?
                if (choice == 1) {
                    boolean validUser = false;
                    scanner.nextLine();
                    while (validUser == false) {
                        System.out.println("Please enter registered username: ");
                        secondUser = scanner.nextLine();

                        if (secondUser.equals("back")){
                            validUser = true;
                        } else if (App.clientService.checkUsername(secondUser)) {   //IF USERNAME EXISTS, ADD AS USER2
                            System.out.println("User " + secondUser + " registered as second user on account");
                            validUser = true;
                            isExit = true;
                        } else {        //USERNAME ENTERED IS NOT A REGISTERED USER. PROMPT TO TRY AGAIN.
                            System.out.println("User not found. Please try again. Enter 'back' to go back.");
                        }
                    }
                    isValid = true;
                } else if (choice == 2) {
                    secondUser = null;
                    isValid = true;
                    isExit = true;
                } else {
                    System.out.println("Invalid input. Please try again. Enter 'back' to go back.");
                }
            }
        }

        Account newAccount = App.accountService.createAccount(accountType, username, secondUser); //CREATE ACCOUNT
        accountNumber = newAccount.getAccountNumber();
        System.out.println("New " + accountType + " account created: " + accountNumber);
        return accountNumber;
    }

    //WITHDRAW FUNDS FROM CURRENT ACCOUNT
    private static void makeWithdrawal(int accountNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How much would you like to withdraw?: ");
        double amount = scanner.nextDouble();

        double currentBalance = App.accountService.checkBalance(accountNumber);  //VERIFY SUFFICIENT FUNDS
        if (currentBalance >= amount) {             //FUNDS SUFFICIENT - PROCEED WITH WITHDRAWAL
            App.transactionService.withdrawal(accountNumber, amount, (currentBalance - amount)); //ADD WITHDRAWAL TO TRANSACTIONS TABLE
            System.out.println("Withdrawal from account " + accountNumber + " of " + df.format(amount) + " successful.");
            System.out.println("New balance: " + df.format(App.accountService.updateBalance(accountNumber, -amount))); //UPDATE BALANCE IN ACCOUNTS TABLE
        } else {   //INSUFFICIENT FUNDS
            System.out.println("Sorry. Insufficient funds. Current balance: " + df.format(currentBalance));
        }
    }

    //DEPOSIT FUNDS INTO CURRENT ACCOUNT
    private static void makeDeposit(int accountNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("How much would you like to deposit?: ");
        double amount = scanner.nextDouble();
        System.out.println("Please insert your cash or check");
        System.out.println("Submission detected. Please wait");

        double currentBalance = App.accountService.checkBalance(accountNumber);
        App.transactionService.deposit(accountNumber, amount, (currentBalance + amount));   //ADD DEPOSIT TO TRANSACTION TABLE
        System.out.println("Deposit to account " + accountNumber + " of " + df.format(amount) + " successful.");
        System.out.println("New balance: " + df.format(App.accountService.updateBalance(accountNumber, amount))); //UPDATE BALANCE IN ACCOUNT TABLE
    }

    //PRINT OUT A LIST OF PAST TRANSACTIONS ON THE ACCOUNT - NUMBER TO SHOW DETERMINED BY USER
    private static void viewTransactionHistory(int accountNumber) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter the maximum number of transactions to display: ");
        int count = scanner.nextInt();          //USER SELECTS NUMBER OF TRANSACTIONS TO DISPLAY

        List<Transaction> transactions = transactionService.transactionHistory(accountNumber); //GET TRANSACTIONS FROM DB
        int length = transactions.size();
        if (length == 0){
            System.out.println("There are no transactions to display");
        }
        for (int i = length - 1; (i >= length - count) && (i >= 0); i--) {  //PRINT THE SELECTED # OF TRANSACTIONS
            System.out.println(transactions.get(i));
        }
    }

    //TRANSFER FUNDS TO ANOTHER REGISTERED ACCOUNT
    private static void transferFunds(String username, int accountNumber) {
        Scanner scanner = new Scanner(System.in);
        boolean youSure = false;
        int toAccount = 0;
        while (youSure == false) {  //LOOP TO GIVE USER SECOND CHANCE TO VERIFY ACCOUNT TO WHICH THEY WANT TO TRANSFER
            System.out.println("Please Enter an account number to which you would like to transfer funds.");
            toAccount = scanner.nextInt();
            Account account = accountService.verifyAccount(toAccount);  //VERIFY THAT ACCOUNT NUMBER EXISTS

            if (account == null) {  //ACCOUNT NUMBER DOES NOT EXIST
                System.out.println("Invalid account. Would you like to try again?\n1. Yes\n2. No");
                int choice = scanner.nextInt();
                if (choice == 2){return;} //EXIT METHOD

            //ACCOUNT DOES EXIST, BUT IS NOT REGISTERED TO THE CURRENT USER- WARN THAT THEY ARE SENDING $ ELSEWHERE
            } else if (!Objects.equals(account.getOwnerOne(), username) && !Objects.equals(account.getOwnerTwo(), username)) {
                System.out.println("WARNING: Your username IS NOT associated with this account.");
                System.out.println("Would you like to proceed or re-enter the account number?");
                System.out.println("1. Proceed\n2. Re-enter account number\n3. Exit");
                int isProceed = scanner.nextInt();
                if (isProceed == 1) {
                    youSure = true;
                } else if (isProceed == 2) {
                    System.out.println("Please try again.");
                } else if (isProceed == 3) {
                    return;
                } else {
                    System.out.println("Invalid input. Please try again.");
                }
            } else {
                System.out.println("Account found. Transfer funds to account " + toAccount);
                youSure = true;
            }
        }
        System.out.println("Please enter amount to transfer: $");
        double amount = scanner.nextDouble();

        double currentBalance = App.accountService.checkBalance(accountNumber); //VERIFY SUFFIICENT FUNDS TO TRANSFER OUT
        if (currentBalance >= amount) {
            //CREATE TRANSACTION RECORD FOR CURRENT ACCOUNT IN TRANSACTION TABLE
            App.transactionService.transferOut(accountNumber, amount, (currentBalance - amount));
            //CREATE TRANSACTION RECORD FOR ACCOUNT TO WHICH TRANSFER IS GOING IN TRANSACTION TABLE
            App.transactionService.transferIn(toAccount, amount, (currentBalance + amount));
            System.out.println("Success! Transferred " + df.format(amount) + " from account " + accountNumber + " to account " + toAccount + ".");
            //UPDATE BALANCE OF CURRENT ACCOUNT IN ACCOUNT TABLE
            System.out.println("New balance: " + df.format(App.accountService.updateBalance(accountNumber, -amount)));
            //UPDATE BALANCE OF ACCOUNT TO WHICH TRANSFER WAS DEPOSITED IN ACCOUNT TABLE
            App.accountService.updateBalance(toAccount, amount);
        } else {
            System.out.println("Sorry. Insufficient funds. Current balance: " + df.format(currentBalance));
        }
    }

    //UPDATE CLIENT INFORMATION (NAME, EMAIL, ADDRESS, PASSWORD)
    private static void updateClientInformation(String username) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        Client client = clientService.getClientInfo(username); //CREATE CLIENT OBJECT, SET TO CURRENT USER INFO
        boolean isFinished = false;
        while (isFinished == false) {  //LOOP TO ALLOW USER TO CHANGE AS MANY FIELDS AS DESIRED
            System.out.println("Select an item to update:");
            System.out.println("1. First Name\n2. Last Name\n3. Password\n4. Mailing Address\n5. Email Address");
            choice = scanner.nextInt();
            switch (choice) {
                case 1: {   //UPDATE FIRST NAME IN LOCAL OBJECT
                    System.out.println("Please enter new first name: ");
                    scanner.nextLine();
                    String firstName = scanner.nextLine();
                    client.setFirstName(firstName);
                    break;
                }
                case 2: {   //UPDATE LAST NAME IN LOCAL OBJECT
                    System.out.println("Please enter new last name: ");
                    String lastName = null;
                    scanner.nextLine();
                    lastName = scanner.nextLine();
                    client.setLastName(lastName);
                    break;
                }
                case 3: {   //UPDATE PASSWORD IN LOCAL OBJECT
                    boolean isValid = false;
                    scanner.nextLine();
                    while (isValid == false) {  //LOOP TO ALLOW TRY AGAIN ON EXISTING PASSWORD
                        System.out.println("Please enter current password: ");
                        String curPassword = null;
                        curPassword = scanner.nextLine();
                        //CHECK CURRENT PASSWORD AGAINST USER IN DATABASE
                        if (clientService.checkPassword(username, curPassword)) {
                            System.out.println("Please enter new password: ");
                            String password = null;
                            password = scanner.nextLine();
                            client.setPassword(password);
                            isValid = true;
                        } else {
                            System.out.println("Invalid password. Please try again.");
                        }
                    }
                    break;
                }
                case 4: {   //UPDATE MAILING ADDRESS IN LOCAL OBJECT
                    System.out.println("Please enter new mailing address: ");
                    String address = null;
                    scanner.nextLine();
                    address = scanner.nextLine();
                    client.setAddress(address);
                    break;
                }
                case 5: {   //UPDATE EMAIL ADDRESS IN LOCAL OBJECT
                    System.out.println("Please enter new email address: ");
                    String email = null;
                    scanner.nextLine();
                    email = scanner.nextLine();
                    client.setEmail(email);
                    break;
                }
            }
            boolean isValid = false;
            while (isValid == false) {  //INPUT VALIDATION LOOP
                System.out.println("Would you like to update any other information?\n1. Yes\n2. No");
                choice = scanner.nextInt();
                if (choice == 1) {
                    isValid = true;
                } else if (choice == 2) {
                    isValid = true;
                    isFinished = true;
                } else if (choice != 1) {
                    System.out.println("Invalid input. Please try again");
                }
            }
        }
        try {
            clientService.updateClient(client); //PASS UPDATED VALUES IN LOCAL CLIENT OBJECT TO CLIENT RECORD IN DB
        } catch (Exception e) {
            System.out.println("That email address is already associated with another client");
        } finally {
            System.out.println("User information updated.");
        }
    }

    //ADD OR EDIT SECOND USERNAME ASSOCIATED WITH THE ACCOUNT
    private static void updateSecondClient(int accountNumber) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        //CREATE LOCAL ACCOUNT OBJECT FROM CLIENT RECORD
        Account account = accountService.getAccountDetails(accountNumber);
        //IF ACCOUNT CURRENTLY HAS NO SECOND USER (OWNERTWO IS NOT NULL) - PROMPT TO REMOVE
        if (account.getOwnerTwo() != null) {
            String ownerTwo = account.getOwnerTwo();
            System.out.println("User " + ownerTwo + " is listed as a second user on this account." +
                    "would you like to remove this user?\n1. Yes\n2. No");
            choice = scanner.nextInt();
            if (choice == 1) {
                account.setOwnerTwo(null); //REMOVE SECOND USER
                accountService.updateAccount(account); //UPDATE ACCOUNT RECORD IN DB WITH INFO FROM LOCAL OBJECT
                System.out.println("User " + ownerTwo + " has been removed from this account.");
                scanner.nextLine();
                System.out.println("Would you like to add a new second user?\n1. Yes\n2. No");
                choice = scanner.nextInt();
                //ADD NEW SECOND USER
                if (choice == 1) {
                    boolean isValid = false;
                    while (isValid == false) {  //VALIDATION LOOP
                        System.out.println("Please enter new second user: ");
                        String newUser2 = null;
                        scanner.nextLine();
                        newUser2 = scanner.nextLine();
                        if (clientService.checkUsername(newUser2)) { //VERIFY SECOND USER EXISTS
                            account.setOwnerTwo(newUser2);          //ADD SECOND USER TO LOCAL ACCOUNT OBJECT
                            accountService.updateAccount(account); //UPDATE ACCOUNT RECORD IN DB WITH INFO FROM LOCAL OBJECT
                            System.out.println("Additional user added.");
                            isValid = true;
                        } else {
                            System.out.println("There is no user registered by that username. Please Try again.");
                        }
                    }
                }
            }
        //IF ACCOUNT HAS NO SECOND USER - PROMPT TO ADD ONE
        } else {
            boolean isValid = false;
            while (isValid == false) {
                System.out.println("Please enter new second user: ");
                String newUser2 = scanner.nextLine();
                if (clientService.checkUsername(newUser2)) {    //VERIFY USERNAME EXISTS
                    account.setOwnerTwo(newUser2);              //ADD SECOND USER TO LOCAL ACCOUNT OBJECT
                    System.out.println("Additional user added.");
                    isValid = true;
                } else {                                        //USER DOES NOT EXIST IN DB
                    System.out.println("There is no user registered by that username. Please Try again.");
                }
            }
            accountService.updateAccount(account);
        }
    }

    //ASK CLIENT IF THEY WOULD LIKE TO MAKE ANOTHER TRANSACTION
    private static boolean isExit(){
        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        boolean isValid = false; //VALIDATE USER INPUT (1 OR 2)
        while (isValid == false){
            System.out.println("\nWould you like to make another transaction?\n1. Yes\n2. No");
            choice = scanner.nextInt();
            if (choice == 1) {
                isValid = true;
                return false;
            }else if (choice == 2){  //EXIT PROGRAM
                isValid = true;
                System.out.println("Thank you for being a valued customer! Have a wonderful day!");
                return true;
            }else{
                System.out.println("Invalid input. Please try again.");
            }
        }
        return false;
    }
}