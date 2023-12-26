package Atm_Code;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

public class AccountMenuGUI extends JFrame {
    private JTextArea resultArea;
    private JTextArea accountsArea;
    private JTextArea textAreaScrollable;
    private JScrollPane scrollPane;
    private String accountNumber;
    private String accountName;
    private String pin;
    private static ATMdb myDatabase;
    Account currAccount;

     public AccountMenuGUI(String accountNumber, String accountName, String Pin, Account currAccount, ATMdb myDatabase) {
        this.accountName = accountName;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.currAccount = myDatabase.retrieveAccount(accountNumber);
        this.myDatabase = myDatabase;
        this.resultArea = new JTextArea();
        textAreaScrollable = new JTextArea();
        textAreaScrollable.setEditable(false);
        scrollPane = new JScrollPane(textAreaScrollable);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        initialize();
    }

    private void initialize() {

        if(!accountName.equals("Admin"))
        {
            setTitle("Account Menu");
            setSize(700, 300);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           
        }
        else
        {
            setTitle("Account Menu");
            setSize(700, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        }

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setVisible(true);
                
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome, to your banking portal " + "" + accountName);
        welcomeLabel.setBounds(200, 20, 300, 40);
        panel.add(welcomeLabel);

        if(!accountName.equals("Admin")){
        resultArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBounds(20, 150, 650, 100);
        panel.add(scrollPane);

//DEPOSIT
        JButton depositButton = new JButton("Deposit");
        depositButton.setBounds(10, 70, 150, 25);

        depositButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deposit();
            }
        });
        panel.add(depositButton);

//CHANGE PIN

        JButton changePin = new JButton("Change Pin");
        changePin.setBounds(10, 110, 150, 25);
        changePin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changePin();
            }
        });
        panel.add(changePin);

//WITHDRAW

        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.setBounds(180, 70, 150, 25);
        withdrawButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                withdraw();
            }
        });
        panel.add(withdrawButton);

//CHECK BALANCE

        JButton checkBalanceButton = new JButton("Check Balance");
        checkBalanceButton.setBounds(350, 70, 150, 25);
        checkBalanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBalance();
            }
        });
        panel.add(checkBalanceButton);

//TRANSFER FUNDS

        JButton transferFunds = new JButton("Transfer Funds");
        transferFunds.setBounds(520, 70, 150, 25);
        transferFunds.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                transferFunds();
            }
        });
        panel.add(transferFunds);

//LOGOUT

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(350, 110, 150, 25);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        panel.add(logoutButton);

//PRINT STATEMENT

        JButton printStatement = new JButton("Print Statement");
        printStatement.setBounds(180, 110, 150, 25);
        printStatement.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printStatement();
            }
        });
        panel.add(printStatement);
    }
    else
    {
        //FOR ADMIN DISPLAY DIALOG BOX
        accountsArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(accountsArea);
        scrollPane.setBounds(20, 150, 650, 400);
        panel.add(scrollPane);

        JButton adminAccess = new JButton("Access all accounts info");
        adminAccess.setBounds(175, 80, 300, 25);
        adminAccess.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printAllAccountDetails();
            }
        });
        panel.add(adminAccess);

        //LOGOUT

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(320, 110, 150, 25);
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });
        panel.add(logoutButton);

        //DELETE ACCOUNT
        JButton deleteAccount = new JButton("Delete a Account");
        deleteAccount.setBounds(175, 110, 150, 25);
        deleteAccount.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteAccount();
            }
        });
        panel.add(deleteAccount);

    }
        
    }

    //-------------------------------------------------------------//
    //DEPOSIT METHOD

    private void deposit() {

        String takeInputAmount = JOptionPane.showInputDialog("Enter Amount to be Deposited");
        double depositAmount = Double.parseDouble(takeInputAmount);

        double currDepositLimit = myDatabase.retrieveDeposit(accountNumber);
        System.out.println("In DESPOTI : " + currDepositLimit);

        int result = currAccount.deposit(depositAmount, currDepositLimit);
        
        currDepositLimit = myDatabase.retrieveDeposit(accountNumber);   
        
        if(result == 1)
        {
            String message = "Above per day Deposit Limit of $10,000 " + "Remaining Limit for Today : $" + currDepositLimit;
            JOptionPane.showMessageDialog(null, message, "Limit Reached", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        else if(result == 2)
        {
            String message = "Invalid deposit amount";
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        double accountBalance = myDatabase.getAccountBalance(accountNumber);

        myDatabase.createTransaction(accountNumber,new Date(), "    Deposit        ",depositAmount, accountBalance);
        
        //myDatabase.updateDailyLimits(accountNumber, currAccount.remainingWithdrawalLimit, currAccount.remainingDepositLimit);
        myDatabase.recordDeposit(accountNumber);

        String message = "Deposit successful!\nYour new balance is: $" + accountBalance;
        JOptionPane.showMessageDialog(null, message, "Deposit Successful", JOptionPane.INFORMATION_MESSAGE);
            
    }

    //-------------------------------------------------------------//
    //WITHDRAW METHOD

    private void withdraw() {

        String takeInputAmount = JOptionPane.showInputDialog("Enter Amount to be Withdrawn");
        double withdrawAmount = Double.parseDouble(takeInputAmount);
        String currPin = JOptionPane.showInputDialog("Enter Account Pin");

        if(myDatabase.getPin(accountNumber).equals(currPin))
        {
            double currWithDrawalLimit = myDatabase.retrieveWithdrawal(accountNumber);
            int result = currAccount.withdraw(withdrawAmount, currWithDrawalLimit);

            if(result == 0)
            {
                double accountBalance = myDatabase.getAccountBalance(accountNumber);

                myDatabase.createTransaction(accountNumber,new Date(), "Withdrawal      ",withdrawAmount, accountBalance);

                myDatabase.recordWithdrawal(accountNumber);

                String message = "Withdraw successful!\nYour new balance is: $" + accountBalance;
                JOptionPane.showMessageDialog(null, message, "Withdrawal Successful", JOptionPane.INFORMATION_MESSAGE);
            }
            else if(result == 1)
            {
                double remainingWithdrawalLimit = myDatabase.retrieveWithdrawal(accountNumber);
                String message = "Daily withdrawal Limit for your account is $2000. Remaining Withdrwal Limit for today : $" + remainingWithdrawalLimit;
                JOptionPane.showMessageDialog(null, message, "Withdrawal Limit Reached", JOptionPane.INFORMATION_MESSAGE);
            }

            else
            {
                double accountBalance = currAccount.getBalance();
                String message = "Low Balance $" + accountBalance;
                JOptionPane.showMessageDialog(null, message, "Not Sufficient Balance", JOptionPane.INFORMATION_MESSAGE);
            }
            
        }
        else if(currAccount.loginAttemptsRemaining == 1)
        {
            String message = "Account blocked for 24 hours due to multiple incorrect login attempts.";
            JOptionPane.showMessageDialog(null, message, "Account Blocked", JOptionPane.INFORMATION_MESSAGE);
            
            myDatabase.blockAccount(accountNumber, new Date(System.currentTimeMillis() + 86400000));
            myDatabase.retrieveAccount(accountNumber).setBlockedUntil(new Date(System.currentTimeMillis() + 86400000));
            logout();
            return;
        }
        else
        {
            currAccount.loginAttemptsRemaining--;
            String message = "Wrong Pin Try Again. You have " + currAccount.loginAttemptsRemaining + " attempts remaining";
            JOptionPane.showMessageDialog(null, message, "Wrong Pin", JOptionPane.INFORMATION_MESSAGE);
        }

        
    }


     //-------------------------------------------------------------//
    //CHECK BALANCE METHOD

    private void checkBalance() {
        
        Double balance = myDatabase.getAccountBalance(accountNumber);
        resultArea.setText("Your Current Account Balance is : " +balance);
    }

    //-------------------------------------------------------------//
    //CHANGE PIN METHOD

    private void changePin() {

        String oldPin = JOptionPane.showInputDialog("Enter Your Previous Pin");

        if (currAccount.getPin().equals(oldPin)) {
            String newPin = JOptionPane.showInputDialog("Enter Your New Pin");
            
            myDatabase.changePin(newPin, accountNumber);
        
            String message = "PIN changed successfully! ";
            JOptionPane.showMessageDialog(null, message, "Pin Changed", JOptionPane.INFORMATION_MESSAGE);
            logout();
        }
    
        else if(currAccount.loginAttemptsRemaining == 1)
        {
            String message = "Account blocked for 24 hours due to multiple incorrect login attempts.";
            JOptionPane.showMessageDialog(null, message, "Account Blocked", JOptionPane.INFORMATION_MESSAGE);
            myDatabase.blockAccount(accountNumber, new Date(System.currentTimeMillis() + 86400000));
            myDatabase.retrieveAccount(accountNumber).setBlockedUntil(new Date(System.currentTimeMillis() + 86400000));
            logout();
            return;
        }
        else
        {
            currAccount.loginAttemptsRemaining--;
            String message = "Wrong Pin Try Again. You have " + currAccount.loginAttemptsRemaining + " attempts remaining";
            JOptionPane.showMessageDialog(null, message, "Wrong Pin", JOptionPane.INFORMATION_MESSAGE);
            throw new IllegalArgumentException("Wrong Pin");
        }
    }

    //-------------------------------------------------------------//
    //TRANSFER FUNDS

    private void transferFunds() {

        String recipientAccountNumber = JOptionPane.showInputDialog("Enter recipient account number: ");
        String confirmAccountNumber = JOptionPane.showInputDialog("Re-enter recipient account number to confirm: ");

        if(!recipientAccountNumber.equals(confirmAccountNumber))
        {
            String message = "Account Number do not match. Try Again. Be careful funds once transferred can't be undone";
            JOptionPane.showMessageDialog(null, message, "Account number don't match", JOptionPane.INFORMATION_MESSAGE);
        }
        
        String transferAmount = JOptionPane.showInputDialog("Enter transfer amount: ");
        double amount = Double.parseDouble(transferAmount);

        if (amount <= 0) {
            String message = "Invalid Amount";
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if(amount > currAccount.getBalance())
        {
            String message = "Transfer failed. Account Balance is low. Please try again.";
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        Account destinationAccount = myDatabase.retrieveAccount(recipientAccountNumber);
        
        if (destinationAccount == null) {

            String message = "Transfer failed. Account number does not exist and Please try again.";
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
            
        String accPin = JOptionPane.showInputDialog("Enter pin to confirm Transaction : ");
        
        if(currAccount.getPin().equals(accPin))
        {
            myDatabase.transferFunds(accountNumber, recipientAccountNumber, amount);

            resultArea.setText("Transfer Successfull. Remaining Balance : " + myDatabase.getAccountBalance(accountNumber));
        }

         else if(currAccount.loginAttemptsRemaining == 1)
        {
            String message = "Account blocked for 24 hours due to multiple incorrect login attempts.";
            JOptionPane.showMessageDialog(null, message, "Account Blocked", JOptionPane.INFORMATION_MESSAGE);
            
            myDatabase.blockAccount(confirmAccountNumber, new Date(System.currentTimeMillis() + 86400000));
            logout();
            return;
        }
        else
        {
            currAccount.loginAttemptsRemaining--;
            String message = "Wrong Pin Try Again. You have " + currAccount.loginAttemptsRemaining + " attempts remaining";
            JOptionPane.showMessageDialog(null, message, "Wrong Pin", JOptionPane.INFORMATION_MESSAGE);
            throw new IllegalArgumentException("Wrong Pin");
        }

       
    }

    //-------------------------------------------------------------//
    //PRINT STATEMENT

    private void printStatement() {

    StringBuilder userBankStatement = myDatabase.getUserTransaction(accountNumber);

    resultArea.setText(userBankStatement.toString());
    }

    //-------------------------------------------------------------//
    //LOGOUT METHOD
    private void logout() {
        dispose();
        myDatabase.closeConnection();
        //new ATMGUI(currBank); // Close the current window
    }

    //-------------------------------------------------------------//
    //PRINT ACCOUNT DETAILS (ADMIN)

    public void printAllAccountDetails() {
        
        StringBuilder allAccountDetails = myDatabase.getAllAccountsInfo();

        accountsArea.setText(allAccountDetails.toString());
    }

      public void deleteAccount()
      {
        String accountNumberToDelete = JOptionPane.showInputDialog("Enter recipient account number: ");

        String accPin = JOptionPane.showInputDialog("Enter pin to confirm deletion : ");

        if(currAccount.getPin().equals(accPin))
        {
            myDatabase.deleteAccount(accountNumberToDelete);
            String message = "Account deleted successfully";
            JOptionPane.showMessageDialog(null, message, "Account deleted", JOptionPane.INFORMATION_MESSAGE);
        }
         else if(currAccount.loginAttemptsRemaining == 1)
        {
            String message = "Account blocked for 24 hours due to multiple incorrect login attempts.";
            JOptionPane.showMessageDialog(null, message, "Account Blocked", JOptionPane.INFORMATION_MESSAGE);
            
            myDatabase.blockAccount(accountNumber, new Date(System.currentTimeMillis() + 86400000));

            logout();
            return;
        }
        else
        {
            currAccount.loginAttemptsRemaining--;
            String message = "Wrong Pin Try Again. You have " + currAccount.loginAttemptsRemaining + " attempts remaining";
            JOptionPane.showMessageDialog(null, message, "Wrong Pin", JOptionPane.INFORMATION_MESSAGE);
            throw new IllegalArgumentException("Wrong Pin");
        }

       
        
      }


}
