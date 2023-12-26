package Atm_Code;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ATMdb {

    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/ATMdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "caching_sha2_password";
    private static String accountName;
    private static String accountNumber;
    private static String pin ;
    private static String description;
    public static Date LastWithdrawalDate;
    public static Date LastDepositDate;


    // JDBC variables for opening, closing and managing connection
    private static Connection connection;

    public void ATMdb()
    {
        this.accountName = "0";
        this.accountNumber = "0";
        this.pin = "0000";
        this.description = "";
    }    

    public static void createAccount(Account currAccount)
    {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String insertQuery = "INSERT INTO Accounts (AccountNumber, AccountName, PIN, Balance, CreatedAt) VALUES (?, ?, ?, ?, NOW())";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            accountName = currAccount.getAccountName();
            accountNumber = currAccount.getAccountNumber();
            pin = currAccount.getPin();
            double balance = 100;

            
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setString(2, accountName);
            preparedStatement.setString(3, pin);
            preparedStatement.setDouble(4, balance);

            // Execute the query
            preparedStatement.executeUpdate();

            System.out.println("Account created successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        // finally {
        //     // Close the connection in the finally block to ensure it's always closed
        //     if (connection != null) {
        //         try {
        //             connection.close();
        //         } catch (SQLException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }
    }


      public static void createTransaction(String accountNumber, Date date, String description, double amount, double balance)
    {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String insertQuery = "INSERT INTO Transactions (AccountNumber, TransactionDate, Description, Amount, BalanceAfter) VALUES (?, NOW(), ?, ?, ?)";

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            String currAccountNumber = accountNumber;
            String currDescription = description;
            double currAmount = amount;
            double balanceAfter = balance;

            preparedStatement.setString(1, currAccountNumber);
            preparedStatement.setString(2, currDescription);
            preparedStatement.setDouble(3, currAmount);
            preparedStatement.setDouble(4, balanceAfter);

            // Execute the query
            preparedStatement.executeUpdate();

            System.out.println("Transaction recorded successfully!");

    } catch (SQLException e) {
        e.printStackTrace();
    }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        // finally {
        //     // Close the connection in the finally block to ensure it's always closed
        //     if (connection != null) {
        //         try {
        //             connection.close();
        //         } catch (SQLException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }
    }

    

public static void blockAccount(String accountNumber, Date blockedUntil) {
try {
    // Open a connection
    connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

    Account curAccount = retrieveAccount(accountNumber);
    curAccount.setBlockedUntil(new Date(System.currentTimeMillis() + 86400000));


    String insertQuery = "INSERT INTO BlockedAccounts (AccountNumber, BlockedUntil) VALUES (?, ?)";

    try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
        preparedStatement.setString(1, accountNumber);
        preparedStatement.setTimestamp(2, new java.sql.Timestamp(blockedUntil.getTime()));

        // Execute the query
        preparedStatement.executeUpdate();

        System.out.println("Account blocked successfully!");

    } catch (SQLException e) {
        e.printStackTrace();
    }
     } catch (SQLException e) {
            e.printStackTrace();
    } 

}

public static boolean isAccountBlocked(String accountNumber)
{
    try {
    // Open a connection
    connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    String selectBlockedAccountQuery = "SELECT * FROM BlockedAccounts WHERE AccountNumber = ?";
    
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectBlockedAccountQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has data
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

public static Timestamp blockedUntil(String accountNumber)
{
    try {
    // Open a connection
    connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

    String blockedUntil = "SELECT BlockedUntil FROM BlockedAccounts WHERE AccountNumber = ?";

    try (PreparedStatement preparedStatement = connection.prepareStatement(blockedUntil)) {
            
            preparedStatement.setString(1, accountNumber);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has data
            if (resultSet.next()) {
                // Timestamp blockedTime = resultSet.getTimestamp("BlockedUntil");
                // Date blockedDate = resultSet.getDate("BlockedUntil");
                // String timeDate = blockedDate + " " + blockedTime;
                // return timeDate;

                return resultSet.getTimestamp("BlockedUntil");
            }

        } 
        
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    //RETRIEVE ACCOUNT

      public static Account retrieveAccount(String accountNumber)
    {
        try {
            // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        String selectQuery = "SELECT * FROM Accounts WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has data
            if (resultSet.next()) {
                String accountName = resultSet.getString("AccountName");
                String pin = resultSet.getString("PIN");
                double balance = resultSet.getDouble("Balance");
                Date createdAt = resultSet.getTimestamp("CreatedAt");

                return new Account(accountName, accountNumber, pin, balance, createdAt);
            }

        } 
        
        catch (SQLException e) {
        return null;
    }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        // finally {
        //     // Close the connection in the finally block to ensure it's always closed
        //     if (connection != null) {
        //         try {
        //             connection.close();
        //         } catch (SQLException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }

        return null;
    }

    //GET PIN

      public static String getPin(String accountNumber)
    {
        try {
            // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        String selectQuery = "SELECT * FROM Accounts WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has data
            if (resultSet.next()) {
                String pin = resultSet.getString("PIN");
                return pin;
            }

        } 
        
        catch (SQLException e) {
        return null;
    }

        } catch (SQLException e) {
            e.printStackTrace();
        } 
        
        // finally {
        //     // Close the connection in the finally block to ensure it's always closed
        //     if (connection != null) {
        //         try {
        //             connection.close();
        //         } catch (SQLException e) {
        //             e.printStackTrace();
        //         }
        //     }
        // }
        
        return null;
    }

     public static String changePin(String newPin, String accountNumber)
    {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String updateQuery = "UPDATE Accounts SET PIN = ? WHERE AccountNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, newPin);
            preparedStatement.setString(2, accountNumber);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("PIN updated successfully!");
            } else {
                System.out.println("Account not found or PIN not updated.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        } catch (SQLException e) {
            e.printStackTrace();
        } 

        return null;
    }

    //UPDATE BALANCE

     public static String updateBalance(String accountNumber, double newBalance)
    {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String updateQuery = "UPDATE Accounts SET Balance = ? WHERE AccountNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, accountNumber);

            // Execute the update query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Balance Updated Successfully");
            } else {
                System.out.println("Unable to update balance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
       
    }
    catch (SQLException e) {
            e.printStackTrace();
        }
         return null;
    }

    //DELETE ACCOUNT

    public static void deleteAccount(String accountNumber) {
        try {

        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        // Delete transactions associated with the account
        deleteTransactions(accountNumber);

        // Delete blocked account information associated with the account
        deleteBlockedAccount(accountNumber);

        String deleteQuery = "DELETE FROM Accounts WHERE AccountNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the delete query
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("Account not found or not deleted.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
          } catch (SQLException e) {
            e.printStackTrace();
        } 
    }

    private static void deleteTransactions(String accountNumber) throws SQLException {
        String deleteTransactionsQuery = "DELETE FROM Transactions WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteTransactionsQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the delete query for transactions
            preparedStatement.executeUpdate();
        }
    }

    private static void deleteBlockedAccount(String accountNumber) throws SQLException {
        String deleteBlockedAccountQuery = "DELETE FROM BlockedAccounts WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteBlockedAccountQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the delete query for blocked account information
            preparedStatement.executeUpdate();
        }
    }

    //TRANSFER FUNDS

    public static void transferFunds(String sourceAccountNumber, String destinationAccountNumber, double amount)
    {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            Account sourceAccount = retrieveAccount(sourceAccountNumber);
            Account destinationAccount = retrieveAccount(destinationAccountNumber);

            sourceAccount.withdraw(amount);

            destinationAccount.deposit(amount);

            double sourceBalance = getAccountBalance(sourceAccountNumber);
            double destinationBalance = getAccountBalance(destinationAccountNumber);

            // updateBalance(sourceAccountNumber, sourceBalance);
            // updateBalance(destinationAccountNumber, destinationBalance);

            createTransaction(sourceAccountNumber,new Date(), "Transfer to " + destinationAccount.getAccountName(), amount, sourceBalance);
            createTransaction(destinationAccountNumber,new Date(), "Transfer from " + sourceAccount.getAccountName(), amount, destinationBalance);

        } catch (SQLException e) {
            e.printStackTrace();
        } 

    }

     // Method to retrieve the balance for an account number
     public static double getAccountBalance(String accountNumber) {
        String selectQuery = "SELECT Balance FROM Accounts WHERE AccountNumber = ?";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {

            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getDouble("Balance");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0; // Default balance if not found or an error occurs
    }

    //ACCOUNT EXISTS

    public static boolean isAccountExists(String accountNumber) {
        try {
            // Open a connection
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        String selectAccountQuery = "SELECT AccountNumber FROM Accounts WHERE AccountNumber = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAccountQuery)) {
            preparedStatement.setString(1, accountNumber);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set has data
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();}
        
    }
    catch (SQLException e) {
            e.printStackTrace();
        }
            return false;
}

    //USER TRANSACTIONS

        public static StringBuilder getUserTransaction(String accountNumber) {
        
        try {
        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        StringBuilder userTransactions = new StringBuilder();

        String selectTransactionsQuery = "SELECT * FROM Transactions WHERE AccountNumber = ? ORDER BY TransactionDate DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTransactionsQuery)) {

            preparedStatement.setString(1, accountNumber);

            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Append transaction details to the StringBuilder
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            while (resultSet.next()) {

                String transactionDate = dateFormat.format(resultSet.getTimestamp("TransactionDate"));

                String transactionType = resultSet.getString("Description");

                double amount = resultSet.getDouble("Amount");

                double balance = resultSet.getDouble("BalanceAfter");

                if(transactionType.charAt(0) == 'W')
                {
                userTransactions.append(String.format("%s     -     %s       -$%.2f,     Balance: $%.2f%n", transactionDate, transactionType, amount, balance));
                }
                else if(transactionType.charAt(0) == 'D')
                {
                 userTransactions.append(String.format("%s     -     %s      +$%.2f,      Balance: $%.2f%n", transactionDate, transactionType, amount, balance));
                }
                else if(transactionType.charAt(9) == 't')
                {
                    userTransactions.append(String.format("%s     -     %s         -$%.2f,        Balance: $%.2f%n", transactionDate, transactionType, amount, balance));
                }
                else
                {
                    userTransactions.append(String.format("%s     -     %s       +$%.2f,      Balance: $%.2f%n", transactionDate, transactionType, amount, balance));
                }

            }

            return userTransactions;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static StringBuilder getAllAccountsInfo() {
          
        try {
        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

        StringBuilder allAccountdDetails = new StringBuilder();

        String selectAllAccountsQuery = "SELECT * FROM Accounts";

        try (PreparedStatement preparedStatement = connection.prepareStatement(selectAllAccountsQuery)) {
            
            // Execute the select query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Process the result set
            while (resultSet.next()) {

                String accountName = resultSet.getString("AccountName");
                String accountNumber = resultSet.getString("AccountNumber");
                String pin = resultSet.getString("PIN");
                double balance = resultSet.getDouble("Balance");
                Date creationDate = resultSet.getDate("CreatedAt");

                allAccountdDetails.append("\n");
                allAccountdDetails.append("Account Name: ").append(accountName).append("\n");
                allAccountdDetails.append("Account Number: ").append(accountNumber).append("\n");
                allAccountdDetails.append("PIN: ").append(pin).append("\n");
                allAccountdDetails.append("Balance: ").append(balance).append("\n");
                allAccountdDetails.append("Creation Date: ").append(creationDate).append("\n");
            }

            return allAccountdDetails;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
        
         } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void recordWithdrawal(String accountNumber) {
        try {
        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String updateQuery = "UPDATE DailyLimits SET LastWithdrawalDate = CURRENT_DATE WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, accountNumber);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        
         } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void recordDeposit(String accountNumber) {
        try {
        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        String updateQuery = "UPDATE DailyLimits SET LastDepositDate = CURRENT_DATE WHERE AccountNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, accountNumber);

            preparedStatement.executeUpdate();
       } catch (SQLException e) {
            e.printStackTrace();
        }

        
         } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
        //CLOSE CONNECTION
    public static void closeConnection()
    {
    if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
    }


    // Method to insert or update deposit and withdrawal limits for an account
    public static void updateDeposit(String AccountNumber, double DailyDepositLimit) {
        String updateQuery = "INSERT INTO DailyLimits (AccountNumber, DailyDepositLimit) " +
                             "VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE DailyDepositLimit = VALUES(DailyDepositLimit)";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, AccountNumber);
            preparedStatement.setDouble(2, DailyDepositLimit);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void updateWithdrawal(String AccountNumber, double currWithdrawalLimit) {

        double DailyWithdrawalLimit = currWithdrawalLimit;

        String updateQuery = "INSERT INTO DailyLimits (AccountNumber, DailyWithdrawalLimit) " + "VALUES (?, ?) " + "ON DUPLICATE KEY UPDATE DailyWithdrawalLimit = VALUES(DailyWithdrawalLimit)";


        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {

            preparedStatement.setString(1, AccountNumber);
            preparedStatement.setDouble(2, DailyWithdrawalLimit);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve deposit and withdrawal limits for an account
    public static double retrieveDeposit(String accountNumber) {
        String retrieveQuery = "SELECT DailyDepositLimit FROM DailyLimits WHERE AccountNumber = ?";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(retrieveQuery)) {

            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                                
                double DailyDepositLimit = resultSet.getDouble("DailyDepositLimit");

                System.out.println("Account Number: " + accountNumber);
                System.out.println("Inside Retrieve Deposit Limit: $" + DailyDepositLimit);

                return DailyDepositLimit;



            } else {
                System.out.println("Limits not found for account: " + accountNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Method to retrieve deposit and withdrawal limits for an account
    public static double retrieveWithdrawal(String accountNumber) {
        String retrieveQuery = "SELECT DailyWithdrawalLimit FROM DailyLimits WHERE AccountNumber = ?";

        try (Connection connection = getDatabaseConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(retrieveQuery)) {

            preparedStatement.setString(1, accountNumber);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                
                double DailyWithdrawalLimit = resultSet.getDouble("DailyWithdrawalLimit");
                
                System.out.println("Account Number: " + accountNumber);
                System.out.println("In retrieve Withdrawal Limit: $" + DailyWithdrawalLimit);

                return DailyWithdrawalLimit;


            } else {
                System.out.println("In retreive : Limits not found for account: " + accountNumber);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static Connection getDatabaseConnection()
    {
        try {
        // Open a connection
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        return connection;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}


