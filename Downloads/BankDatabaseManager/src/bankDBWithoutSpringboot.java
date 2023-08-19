import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

public class bankDBWithoutSpringboot {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/bankSystemDb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "orsac@123";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    public static void createAccountsTable() throws SQLException {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS accounts (" +
                "account_number VARCHAR(50) PRIMARY KEY," +
                "balance DOUBLE PRECISION" +
                ")";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(createTableQuery);
        }
    }

    public static void createAccount(String accountNumber, double initialBalance) throws SQLException {
        String insertQuery = "INSERT INTO accounts (account_number, balance) VALUES (?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, accountNumber);
            preparedStatement.setDouble(2, initialBalance);
            preparedStatement.executeUpdate();
        }
    }

    public static void updateBalance(String accountNumber, double newBalance) throws SQLException {
        String updateQuery = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, accountNumber);
            preparedStatement.executeUpdate();
        }
    }

    public static double getBalance(String accountNumber) throws SQLException {
        String selectQuery = "SELECT balance FROM accounts WHERE account_number = ?";
        double balance = 0.0;

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setString(1, accountNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    balance = resultSet.getDouble("balance");
                }
            }
        }

        return balance;
    }

    public static void main(String[] args) {
        try {
            createAccountsTable(); // Create the accounts table if it doesn't exist

            Scanner scanner = new Scanner(System.in);
            int choice;

            do {
                System.out.println("Banking System Menu");
                System.out.println("1. Create Account");
                System.out.println("2. Deposit");
                System.out.println("3. Withdraw");
                System.out.println("4. Check Balance");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        createAccount();
                        break;
                    case 2:
                        performTransaction(TransactionType.DEPOSIT);
                        break;
                    case 3:
                        performTransaction(TransactionType.WITHDRAW);
                        break;
                    case 4:
                        checkBalance();
                        break;
                    case 5:
                        System.out.println("Exiting the system...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                System.out.println();
            } while (choice != 5);

            scanner.close();
        } catch (IOException | SQLException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void createAccount() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();

        createAccount(accountNumber, initialBalance);

        System.out.println("Account created successfully.");
    }

    private static void performTransaction(TransactionType type) throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        double balance = getBalance(accountNumber);

        if ((type == TransactionType.WITHDRAW) && (amount > balance)) {
            System.out.println("Insufficient balance.");
            return;
        }

        if (type == TransactionType.DEPOSIT) {
            balance += amount;
        } else if (type == TransactionType.WITHDRAW) {
            balance -= amount;
        }

        updateBalance(accountNumber, balance);

        System.out.println("Transaction performed successfully.");
    }

    private static void checkBalance() throws IOException, SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        double balance = getBalance(accountNumber);

        if (balance >= 0) {
            System.out.println("Account balance: " + balance);
        } else {
            System.out.println("Account does not exist.");
        }
    }

    private enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }
}
