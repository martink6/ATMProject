public class Account {
    private String accountName;
    private double balance;
    private Customer customer;

    public Account(String accountName, Customer customer) {
        this.accountName = accountName;
        this.customer = customer;
        this.balance = 0.0;
    }

    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("Invalid deposit amount. Amount must be greater than 0.");
            return false;
        }
        balance += amount;
        System.out.printf("$%.2f deposited to %s. New balance: $%.2f\n", amount, accountName, balance);
        return true;
    }

    public boolean withdraw(int fivers, int twenties) {
        if ((fivers < 0 || twenties < 0) || (fivers == 0 && twenties == 0)) {
            System.out.println("Invalid withdrawal amount. Amount must be greater than 0.");
            return false;
        }

        double amount = fivers * 5 + twenties * 20;
        if (amount > balance) {
            System.out.println("Insufficient funds for withdrawal.");
            return false;
        }

        System.out.printf("$%.2f withdrawn from %s. Remaining balance: $%.2f\n", amount, accountName, balance);
        return true;
    }

    public boolean transfer(Account toAccount, double amount) {
        if (amount <= 0) {
            System.out.println("Invalid transfer amount. Amount must be greater than 0.");
            return false;
        }
        if (amount > this.balance) {
            System.out.println("Insufficient funds for transfer.");
            return false;
        }
        this.balance -= amount;
        toAccount.balance += amount;
        System.out.printf("$%.2f transferred from %s to %s. New balance: $%.2f\n", amount, this.accountName, toAccount.getAccountName(), this.balance);
        return true;
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountName() {
        return accountName;
    }
}
