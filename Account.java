package Atm_Code;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Date;

public class Account {


    public double remainingDepositLimit;
    private Date dateCreated;


    private String accountName;
    public int loginAttemptsRemaining = 3;
    public double currAmount = 0;
    ATMdb myDatabase;


    private String accountNumber;
    private String pin;
    private double balance;
    private Date blockedUntil;


    public Account( String accountName, String accountNumber, String pin, double balance, Date dateCreated) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
        this.blockedUntil = null;
        this.accountName = accountName;
        this.dateCreated = dateCreated;
    }


    public void invalitAttempt()
    {
        loginAttemptsRemaining--;
        return;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountName()
    {   
        return accountName;
    }

    public void setAccountName(String accountName)
    {
            this.accountName = accountName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setPin(String pin) {
        this.pin = pin;
        return;
    }


    public int deposit(double amount, double currDepositLimit) {

        double balance = myDatabase.getAccountBalance(accountNumber);
        System.out.println("reamininf limit " +currDepositLimit);


        if (amount > currDepositLimit) {
            
            System.out.println("Daily Deposit Limit for your account is $10,000");
            return 1; // Exceeding deposit limit
        }

        if (amount <= 0) {
            return 2; // Invalid deposit amount
        }        

        balance += amount;

        double remainingDepositLimit = currDepositLimit - amount;
        myDatabase.updateBalance(accountNumber, balance);
        myDatabase.updateDeposit(accountNumber, remainingDepositLimit);
        myDatabase.recordDeposit(accountNumber);

        return 0;
    }

    public int withdraw(double amount, double currWithDrawalLimit) {

        double balance = myDatabase.getAccountBalance(accountNumber);

        if (amount > currWithDrawalLimit) {
            System.out.println("Daily withdrawal Limit for your account is $2000");
            return 1; // Exceeding withdrawal limit
        }
        if (amount <= 0 || amount > balance) {
            return 2; // Invalid withdrawal amount or insufficient funds
        }

        balance -= amount;
        System.out.println("Updating Balance Sender : " + balance);

        double remainingWithdrawalLimit = currWithDrawalLimit - amount;
        
        myDatabase.updateBalance(accountNumber, balance);
        myDatabase.updateWithdrawal(accountNumber, remainingWithdrawalLimit);
        myDatabase.recordWithdrawal(accountNumber);

        return 0;
    }

     public void withdraw(double amount)
     {
        balance -= amount;
        myDatabase.updateBalance(accountNumber, balance);
        myDatabase.recordWithdrawal(accountNumber);
     } 

      public void deposit(double amount)
     {
        balance += amount;
        myDatabase.updateBalance(accountNumber, balance);
        myDatabase.recordDeposit(accountNumber);
     } 

    public double getAmount()
    {
        return currAmount;
    }
    


    public boolean isBlocked() {
        return blockedUntil != null && blockedUntil.after(new Date());
    }

    public Date getBlockedUntil()
 
{
        return blockedUntil;
    }

    public void setBlockedUntil(Date blockedUntil)
 
    {
        this.blockedUntil = blockedUntil;
    }

}