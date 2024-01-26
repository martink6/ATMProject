import java.util.Scanner;

public class ATM {
    private Customer customer;
    private Account savingsAccount;
    private Account checkingAccount;
    private TransactionHistory transactionHistory;
    private Scanner scanner;

    public ATM() {
        transactionHistory = new TransactionHistory();
        scanner = new Scanner(System.in);
    }

    public void start() {
        createCustomer();
        Utils.sleep(1500);
        Utils.clearScreen();

        boolean sessionActive = true;
        while (sessionActive) {
            sessionActive = showMainMenu();
        }
        System.out.println("Thank you for using the ATM. Goodbye!");
    }

    private void pinCheckLoop() {
        Utils.clearScreen();

        boolean pinValid = false;
        while (!pinValid) {
            System.out.print("Enter your PIN:");
            String enteredPin = scanner.nextLine();
            if (!enteredPin.equals(customer.getPin())) {
                System.out.println("Invalid PIN. Try again.");
                continue;
            }
            pinValid = true;
        }

        System.out.println("PIN accepted.");
        Utils.sleep(1500);
    }

    private void createCustomer() {
        Utils.clearScreen();
        System.out.print("Welcome to the ATM. Please enter your name:");
        String name = scanner.nextLine();
        System.out.print("Choose a PIN:");
        String pin = scanner.nextLine();
        customer = new Customer(name, pin);
        savingsAccount = new Account("Savings Account", customer);
        checkingAccount = new Account("Checking Account", customer);
        System.out.println("Account created successfully.");
    }

    private boolean continueSession() {
        System.out.print("Would you like to keep using the ATM? (y/n):");
        String choice = scanner.nextLine();
        return choice.equalsIgnoreCase("y");
    }

    private boolean showMainMenu() {
        try {
            pinCheckLoop();

            Utils.clearScreen();
            System.out.println("\nMain Menu:");
            System.out.println("1. Withdraw money");
            System.out.println("2. Deposit money");
            System.out.println("3. Transfer money");
            System.out.println("4. Get account balances");
            System.out.println("5. Get transaction history");
            System.out.println("6. Change PIN");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    handleWithdraw();
                    break;
                case 2:
                    handleDeposit();
                    break;
                case 3:
                    handleTransfer();
                    break;
                case 4:
                    displayBalances();
                    break;
                case 5:
                    displayTransactionHistory();
                    break;
                case 6:
                    changePIN();
                    break;
                case 7:
                    System.out.println("Thank you for using the ATM. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        } finally {
            if (continueSession()) {
                return true;
            }
        }
        return false;
    }
    private void handleWithdraw() {
        Utils.clearScreen();

        Account selectedAccount = selectAccount();
        if (selectedAccount == null) return;

        int fivers = 0;
        int twenties = 0;

        while (true) {
            System.out.print("Enter an amount to withdraw:");
            double amount = scanner.nextDouble();
            scanner.nextLine();
            if (amount <= 0) {
                System.out.println("Invalid withdrawal amount. Amount must be greater than 0.");
                continue;
            }

            if (amount % 5 != 0) {
                System.out.println("Invalid withdrawal amount. Amount must be in the denominations of $5 and $20.");
                continue;
            }

            System.out.println("Enter the number of $5 bills:");
            fivers = scanner.nextInt();
            scanner.nextLine();

            if (fivers * 5 > amount) {
                System.out.println("Invalid withdrawal amount. Amount of $5 bills must be less than or equal to the withdrawal amount.");
                continue;
            }

            System.out.println("Enter the number of $20 bills:");
            twenties = scanner.nextInt();
            scanner.nextLine();

            if (twenties * 20 > amount) {
                System.out.println("Invalid withdrawal amount. Amount of $20 bills must be less than or equal to the withdrawal amount.");
                continue;
            }

            if (fivers * 5 + twenties * 20 != amount) {
                System.out.println("Invalid withdrawal amount. Number of $5 and $20 bills must equal the withdrawal amount.");
                continue;
            }

            break;
        }

        if (selectedAccount.withdraw(fivers, twenties)) {
            double amount = fivers * 5 + twenties * 20;
            transactionHistory.addTransaction(new TransactionHistory.Transaction("Withdrawal", amount, "Withdrawn $" + amount + " from " + selectedAccount.getAccountName() + ".", selectedAccount.getAccountName()));
        }
    }

    private void handleDeposit() {
        Utils.clearScreen();

        Account selectedAccount = selectAccount();
        if (selectedAccount == null) return;

        System.out.print("Enter amount to deposit:");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (selectedAccount.deposit(amount)) {
            transactionHistory.addTransaction(new TransactionHistory.Transaction( "Deposit", amount, "Deposited $" + amount + " into " + selectedAccount.getAccountName() + ".", selectedAccount.getAccountName()));
        }

        Utils.sleep(1500);
    }

    private void handleTransfer() {
        Utils.clearScreen();

        System.out.println("Select the account to transfer FROM:");
        Account fromAccount = selectAccount();
        if (fromAccount == null) return;

        Utils.clearScreen();
        System.out.println("Select the account to transfer TO:");
        Account toAccount = selectAccount();
        if (toAccount == null || toAccount == fromAccount) {
            System.out.println("Invalid account selection for transfer.");
            return;
        }

        Utils.clearScreen();
        System.out.printf("Transfer from %s to %s\n", fromAccount.getAccountName(), toAccount.getAccountName());
        System.out.print("Enter amount to transfer:");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        if (fromAccount.transfer(toAccount, amount)) {
            transactionHistory.addTransaction(new TransactionHistory.Transaction( "Transfer", amount, "Transferred $" + amount + " from " + fromAccount.getAccountName() + " to " + toAccount.getAccountName() + ".", fromAccount.getAccountName()));
        }

        Utils.sleep(1500);
    }

    private void displayBalances() {
        Utils.clearScreen();

        System.out.println("Savings Account Balance: $" + savingsAccount.getBalance());
        System.out.println("Checking Account Balance: $" + checkingAccount.getBalance());

        transactionHistory.addTransaction(new TransactionHistory.Transaction( "Balance", savingsAccount.getBalance(), "Checked balance of " + savingsAccount.getAccountName() + ".", savingsAccount.getAccountName()));
    }

    private void displayTransactionHistory() {
        Utils.clearScreen();

        System.out.println("Transaction History:");
        for (TransactionHistory.Transaction transaction : transactionHistory.getHistory()) {
            System.out.println(transaction.toString());
        }

        transactionHistory.addTransaction(new TransactionHistory.Transaction( "History", 0, "Checked transaction history of " + savingsAccount.getAccountName() + ".", savingsAccount.getAccountName()));
    }

    private void changePIN() {
        Utils.clearScreen();
        System.out.print("Enter new PIN:");
        String newPin = scanner.nextLine();
        customer.setPin(newPin);

        transactionHistory.addTransaction(new TransactionHistory.Transaction( "PIN", 0, "Changed PIN of " + savingsAccount.getAccountName() + ".", savingsAccount.getAccountName()));

        System.out.println("PIN changed successfully.");
        Utils.sleep(1500);
    }

    private Account selectAccount() {
        System.out.print("\n1. Savings Account\n2. Checking Account\n3. Cancel\nSelect an account:");
        int accountChoice = scanner.nextInt();
        scanner.nextLine();

        return switch (accountChoice) {
            case 1 -> savingsAccount;
            case 2 -> checkingAccount;
            case 3 -> null;
            default -> {
                System.out.println("Invalid account selection.");
                yield null;
            }
        };
    }
}
