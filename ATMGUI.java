package Atm_Code;
import javax.swing.*;
import java.sql.Timestamp;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

public class ATMGUI extends JFrame {
    
    private JTextField accountNumberField;
    private JPasswordField pinField;
    private JTextArea resultArea;
    private JTextField depositAmountField;
    private static ATMdb myDatabase;

     public ATMGUI() {
        this.resultArea = new JTextArea();
        initialize();
    }

    private void initialize() {
        setTitle("Welcome to the ATM. Please Input ID and Password");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeComponents(panel);
        add(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel accountLabel = new JLabel("Account Number:");
        accountLabel.setBounds(110, 80, 200, 25);
        panel.add(accountLabel);

        accountNumberField = new JTextField(20);
        accountNumberField.setBounds(230, 80, 300, 25);
        panel.add(accountNumberField);

        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setBounds(110, 120, 120, 25);
        panel.add(pinLabel);

        pinField = new JPasswordField(20);
        pinField.setBounds(230, 120, 300, 25);
        panel.add(pinField);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(250, 180, 100, 25);
        panel.add(loginButton);
        
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        JButton createAccountButton = new JButton("Create Account");
        createAccountButton.setBounds(350, 180, 150, 25);
        panel.add(createAccountButton);

        createAccountButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateAccountDialog();
            }
        });

    }

//CREATE ACCOUNT

    private void showCreateAccountDialog() {

        Random random = new Random();

        // Generate a 6-digit random number as a string
        String randomNumberString = String.format("%06d", random.nextInt(1000000));

        String accountName = JOptionPane.showInputDialog("Enter account Name:");
        String accountNumber = randomNumberString;
        String pin = JOptionPane.showInputDialog("Enter PIN:");

            int result = createAccount(accountName,accountNumber,pin);

            // if(result == 1)
            // resultArea.setText("Account Name already exist.");

            if(result == 2)
            resultArea.setText("Account number cannot be negative.");
            
            else if(result== 3)
            {
                String message = "Account Number already exist.";
                JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            }

            else if(result== 4)
            resultArea.setText( "Pin cannot be negative.");

            else
            {
            String message = "Account Created Succesfully.\nYour Account Number is : " + accountNumber + "\nYour Pin is : "+pin;
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
            }
            resultArea.setText( "Account Created Successfully");

        } 
        
    private void login() {

        String accountNumber = accountNumberField.getText();
        String pin = new String(pinField.getPassword());
        //String accountName = currBank.getAccountName(accountNumber);

        if(myDatabase.isAccountBlocked(accountNumber))
        {
            Timestamp blockedUntil = myDatabase.blockedUntil(accountNumber);
            String message = "Account is blocked. Will reactivate after " + blockedUntil;
            JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
        }
        else
        {
        Account currAccount = myDatabase.retrieveAccount(accountNumber);

        if(currAccount != null && currAccount.getPin().equals(pin))
        {
            String accountName = currAccount.getAccountName();

            double withdrawalLimit = myDatabase.retrieveWithdrawal(accountNumber);
            System.out.println(withdrawalLimit + "Withdraw after login : ");
            
            double getDepositLimit = myDatabase.retrieveDeposit(accountNumber);
            System.out.println(getDepositLimit + "Deposit after login : ");
            
            //currAccount.setLimits(withdrawalLimit, getDepositLimit);

            new AccountMenuGUI(accountNumber,accountName, pin, currAccount, myDatabase);
        }
        else{
        String message = "Invalid Credentials try Again";
        JOptionPane.showMessageDialog(null, message, "Try Again", JOptionPane.INFORMATION_MESSAGE);
        }
        }
       
    }

    public int createAccount(String accountName, String accountNumber, String pin) {

        int checkAccountNumber = Integer.parseInt(accountNumber);
        if (checkAccountNumber < 0) 
            return 2;

        if (myDatabase.isAccountExists(accountNumber))
            return 3;

        int checkPin = Integer.parseInt(pin);
        if (checkPin < 0)
            return 4;


        Account account = new Account(accountName, accountNumber, pin, 100, new Date());
        myDatabase.createAccount(account);

        myDatabase.updateDeposit(accountNumber, 10000);
        myDatabase.updateWithdrawal(accountNumber, 2000);

        return 0;  
    }


    //MAIN METHOD - DRIVER CODE

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
        public void run() {

            myDatabase = new ATMdb();
            new ATMGUI();
        
    }
        });
    }
}
