import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

// User class represents a user with a unique ID, PIN, and account balance.
class User {
    private String userID;
    private String userPIN;
    private double accountBalance;

    // Constructor to initialize a User object with provided values.
    public User(String userID, String userPIN, double accountBalance) {
        this.userID = userID;
        this.userPIN = userPIN;
        this.accountBalance = accountBalance;
    }

    // Getter methods to retrieve user information.
    public String getUserID() {
        return userID;
    }

    public String getUserPIN() {
        return userPIN;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    // Setter method to update the account balance.
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}

// ATM class encapsulates the ATM functionalities and user interactions.
class ATM {
    private Map<String, User> userDatabase;
    private User currentUser;
    private final int maxAttempts = 3;  // Maximum number of login attempts

    // Constructor initializes the user database with sample users.
    public ATM() {
        userDatabase = new HashMap<>();
        userDatabase.put("1234", new User("1234", "5678", 1000.0));
        userDatabase.put("5678", new User("5678", "1234", 500.0));
    }

    // Method to start the ATM, handling user authentication and interaction.
    public void startATM() {
        Scanner scanner = new Scanner(System.in);

        int attempts = 0;
        boolean authenticated = false;

        // User Authentication with limited attempts
        while (attempts < maxAttempts && !authenticated) {
            System.out.print("Enter UserID: ");
            String userID = scanner.nextLine();

            try {
                if (isUserValid(userID)) {
                    System.out.print("Enter PIN: ");
                    String userPIN = scanner.nextLine();

                    if (authenticateUser(userID, userPIN)) {
                        authenticated = true;
                        System.out.println("Authentication successful!");
                        displayMenu();
                        int choice = scanner.nextInt();
                        performOperation(choice);
                    } else {
                        System.out.println("Authentication failed. Please try again.");
                        attempts++;
                    }
                } else {
                    System.out.println("Invalid UserID. Please try again.");
                    attempts++;
                }
            } catch (Exception e) {
                System.out.println("Error during authentication: " + e.getMessage());
            }
        }

        if (!authenticated) {
            System.out.println("Exceeded maximum login attempts. Exiting...");
        }

        // Close the scanner to prevent resource leak
        scanner.close();
    }

    // Method to check if the entered user ID is valid.
    private boolean isUserValid(String userID) {
        return userDatabase.containsKey(userID);
    }

    // Method to authenticate a user based on provided userID and PIN.
    private boolean authenticateUser(String userID, String userPIN) {
        User user = userDatabase.get(userID);
        if (user != null && user.getUserPIN().equals(userPIN)) {
            currentUser = user; // Set the currentUser upon successful authentication
            return true;
        }
        return false;
    }

    // Method to display the ATM menu to the user.
    private void displayMenu() {
        System.out.println("1. Check Balance");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Deposit Money");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    // Method to perform the selected operation based on user input.
    private void performOperation(int choice) {
        Scanner scanner = new Scanner(System.in);

        try {
            switch (choice) {
                case 1:
                    System.out.println("Balance: Rs " + currentUser.getAccountBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to withdraw: Rs ");
                    double withdrawAmount = scanner.nextDouble();
                    if (isValidWithdrawal(withdrawAmount)) {
                        currentUser.setAccountBalance(currentUser.getAccountBalance() - withdrawAmount);
                        System.out.println("Withdrawal successful. Remaining balance: Rs " + currentUser.getAccountBalance());
                    } else {
                        System.out.println("Invalid withdrawal amount or insufficient funds.");
                    }
                    break;
                case 3:
                    System.out.print("Enter amount to deposit: Rs ");
                    double depositAmount = scanner.nextDouble();
                    if (isValidDeposit(depositAmount)) {
                        currentUser.setAccountBalance(currentUser.getAccountBalance() + depositAmount);
                        System.out.println("Deposit successful. New balance: Rs " + currentUser.getAccountBalance());
                    } else {
                        System.out.println("Invalid deposit amount.");
                    }
                    break;
                case 4:
                    System.out.println("Exiting ATM. Thank you!");
                    break;
                default:
                    System.out.println("Invalid choice. Exiting...");
            }
        } catch (Exception e) {
            System.out.println("Error during operation: " + e.getMessage());
        } finally {
            // Close the scanner to prevent resource leak
            scanner.close();
        }
    }

    // Method to validate withdrawal amount.
    private boolean isValidWithdrawal(double amount) {
        return amount > 0 && amount <= currentUser.getAccountBalance();
    }

    // Method to validate deposit amount.
    private boolean isValidDeposit(double amount) {
        return amount > 0;
    }
}

// Main class to execute the ATM program.
public class Main {
    public static void main(String[] args) {
        ATM atm = new ATM();
        atm.startATM();
    }
}
