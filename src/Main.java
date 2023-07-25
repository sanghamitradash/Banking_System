import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

class BankSystem {
    private static final String ACCOUNTS_FILE = "accounts.txt";

    public static void main(String[] args) {
        try {
            createFileIfNotExists(ACCOUNTS_FILE);

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
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static void createAccount() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter initial balance: ");
        double initialBalance = scanner.nextDouble();

        FileWriter writer = new FileWriter(ACCOUNTS_FILE, true);
        writer.write(accountNumber + "," + initialBalance + System.lineSeparator());
        writer.close();

        System.out.println("Account created successfully.");
    }

    private static void performTransaction(TransactionType type) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();

        String fileContent = new String(Files.readAllBytes(Paths.get(ACCOUNTS_FILE)));
        String[] accounts = fileContent.split(System.lineSeparator());

        boolean accountExists = false;
        StringBuilder updatedContent = new StringBuilder();

        for (String account : accounts) {
            String[] accountInfo = account.split(",");
            if (accountInfo[0].equals(accountNumber)) {
                accountExists = true;
                double balance = Double.parseDouble(accountInfo[1]);
                if (type == TransactionType.WITHDRAW && amount > balance) {
                    System.out.println("Insufficient balance.");
                    return;
                } else if (type == TransactionType.DEPOSIT) {
                    balance += amount;
                } else if (type == TransactionType.WITHDRAW) {
                    balance -= amount;
                }
                updatedContent.append(accountInfo[0]).append(",").append(balance).append(System.lineSeparator());
            } else {
                updatedContent.append(account).append(System.lineSeparator());
            }
        }

        if (!accountExists) {
            System.out.println("Account does not exist.");
            return;
        }

        FileWriter writer = new FileWriter(ACCOUNTS_FILE);
        writer.write(updatedContent.toString());
        writer.close();

        System.out.println("Transaction performed successfully.");
    }

    private static void checkBalance() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine();

        String fileContent = new String(Files.readAllBytes(Paths.get(ACCOUNTS_FILE)));
        String[] accounts = fileContent.split(System.lineSeparator());

        for (String account : accounts) {
            String[] accountInfo = account.split(",");
            if (accountInfo[0].equals(accountNumber)) {
                double balance = Double.parseDouble(accountInfo[1]);
                System.out.println("Account balance: " + balance);
                return;
            }
        }

        System.out.println("Account does not exist.");
    }

    private static void createFileIfNotExists(String fileName) throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    private enum TransactionType {
        DEPOSIT,
        WITHDRAW
    }
}
