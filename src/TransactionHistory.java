import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {
    private List<Transaction> history;

    private static int accountTransactionCount = 0;
    private static int securityTransactionCount = 0;
    public TransactionHistory() {
        history = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        history.add(transaction);
    }

    public List<Transaction> getHistory() {
        return new ArrayList<>(history);
    }

    public void printHistory() {
        if (history.isEmpty()) {
            System.out.println("No transactions available.");
        } else {
            for (Transaction transaction : history) {
                System.out.println(transaction);
            }
        }
    }

    public static class Transaction {
        private String transactionId;
        private String type;
        private double amount;
        private String detail;
        private String accountName;

        public Transaction(String type, double amount, String detail, String accountName) {
            this.transactionId = GenerateTransactionId(type);
            this.type = type;
            this.amount = amount;
            this.detail = detail;
            this.accountName = accountName;
        }

        private String GenerateTransactionId(String txType) {
            return switch (txType) {
                case "Withdrawal", "Deposit", "Transfer" -> "A" + String.format("%04d", accountTransactionCount++);
                default -> "S" + String.format("%04d", securityTransactionCount++);
            };
        }

        @Override
        public String toString() {
            return String.format("%s: %s", transactionId, detail);
        }
    }
}
