import java.util.*;
import java.util.stream.Collectors;

public class BankTester {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", "20.00$");
        Account a2 = new Account("Andrej", "20.00$");
        Account a3 = new Account("Andrej", "30.00$");
        Account a4 = new Account("Gajduk", "20.00$");
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1)&&!a1.equals(a2)&&!a2.equals(a1)&&!a3.equals(a1)
                && !a4.equals(a1)
                && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        FlatAmountProvisionTransaction fa1 = new FlatAmountProvisionTransaction(10, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa2 = new FlatAmountProvisionTransaction(20, 20, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa3 = new FlatAmountProvisionTransaction(20, 10, "20.00$", "10.00$");
        FlatAmountProvisionTransaction fa4 = new FlatAmountProvisionTransaction(10, 20, "50.00$", "50.00$");
        FlatAmountProvisionTransaction fa5 = new FlatAmountProvisionTransaction(30, 40, "20.00$", "10.00$");
        FlatPercentProvisionTransaction fp1 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp2 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 10);
        FlatPercentProvisionTransaction fp3 = new FlatPercentProvisionTransaction(10, 10, "20.00$", 10);
        FlatPercentProvisionTransaction fp4 = new FlatPercentProvisionTransaction(10, 20, "50.00$", 10);
        FlatPercentProvisionTransaction fp5 = new FlatPercentProvisionTransaction(10, 20, "20.00$", 30);
        FlatPercentProvisionTransaction fp6 = new FlatPercentProvisionTransaction(30, 40, "20.00$", 10);
        if (fa1.equals(fa1) &&
                !fa2.equals(null) &&
                fa2.equals(fa1) &&
                fa1.equals(fa2) &&
                fa1.equals(fa3) &&
                !fa1.equals(fa4) &&
                !fa1.equals(fa5) &&
                !fa1.equals(fp1) &&
                fp1.equals(fp1) &&
                !fp2.equals(null) &&
                fp2.equals(fp1) &&
                fp1.equals(fp2) &&
                fp1.equals(fp3) &&
                !fp1.equals(fp4) &&
                !fp1.equals(fp5) &&
                !fp1.equals(fp6)) {
            System.out.println("Your transactions equals methods do not work properly.");
            return;
        }
        Account accounts[] = new Account[]{a1, a2, a3, a4};
        Account accounts1[] = new Account[]{a2, a1, a3, a4};
        Account accounts2[] = new Account[]{a1, a2, a3};
        Account accounts3[] = new Account[]{a1, a2, a3, a4};

        Bank b1 = new Bank("Test", accounts);
        Bank b2 = new Bank("Test", accounts1);
        Bank b3 = new Bank("Test", accounts2);
        Bank b4 = new Bank("Sample", accounts);
        Bank b5 = new Bank("Test", accounts3);

        if (!(b1.equals(b1) &&
                !b1.equals(null) &&
                !b1.equals(b2) &&
                !b2.equals(b1) &&
                !b1.equals(b3) &&
                !b3.equals(b1) &&
                !b1.equals(b4) &&
                b1.equals(b5))) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        accounts[2] = a1;
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        long from_id = a2.getId();
        long to_id = a3.getId();
        Transaction t = new FlatAmountProvisionTransaction(from_id, to_id, "3.00$", "3.00$");
        b1.makeTransaction(t);
        if (b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        b5.makeTransaction(t);
        if (!b1.equals(b5)) {
            System.out.println("Your bank equals method do not work properly.");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), jin.nextLine());
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    String amount = jin.nextLine();
                    String parameter = jin.nextLine();
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + t.getAmount());
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + bank.totalProvision());
                    System.out.println("Total transfers: " + bank.totalTransfers());
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int from_idx, int to_idx, String amount, String o, Bank bank) {
        switch (description) {
            case "FlatAmount":
                return new FlatAmountProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, o);
            case "FlatPercent":
                return new FlatPercentProvisionTransaction(bank.getAccounts()[from_idx].getId(),
                        bank.getAccounts()[to_idx].getId(), amount, Integer.parseInt(o));
        }
        return null;
    }


}


/**
 * Bank
 */
class Bank {
    private String name;
    private Account[] accounts;

    private String totalTransers;
    private String totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = Arrays.copyOf(accounts, accounts.length);
        this.totalTransers = "0.00$";
        this.totalProvision = "0.00$";
    }

     public boolean makeTransaction(Transaction transaction) {
        Optional<Account> from = findAccount(transaction.getFromId());
        Optional<Account> to = findAccount(transaction.getToId());
        if (from.isPresent() && to.isPresent()) {
            Account fromAccount = from.get();
            Account toAccount = to.get();
            long fromBalance = toNumber(fromAccount.getBalance());
            long toBalance = toNumber(toAccount.getBalance());
            long amount = toNumber(transaction.getAmount());
            long provision = toNumber(transaction.getProvision());
            if (provision + amount <= fromBalance) {
                if (fromAccount.getId() == toAccount.getId()) {
                    fromBalance = toBalance = (fromBalance - provision);
                } else {
                    fromBalance -= provision + amount;
                    toBalance += amount;
                }
                fromAccount.setBalance(toString(fromBalance));
                toAccount.setBalance(toString(toBalance));
                updateTotals(amount, provision);
                return true;
            }
        }
        return false;
    }

    void updateTotals(long amount, long provision) {
        long totalTransfers = toNumber(this.totalTransers);
        long totalProvision = toNumber(this.totalProvision);

        totalTransfers += amount;
        totalProvision += provision;
        this.totalTransers = toString(totalTransfers);
        this.totalProvision = toString(totalProvision);
    }

    Optional<Account> findAccount(long id) {
        return Arrays.stream(accounts)
                .filter(each -> each.getId() == id)
                .findAny();
    }

    public static long toNumber(String amount) {
        String num = amount.replace(".", "").replace("$", "");
        return Long.parseLong(num);
    }

    public static String toString(long amount) {
        return String.format("%.2f$", amount / 100.);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Name: ");
        result.append(name);
        result.append("\n\n");
        Arrays.stream(accounts)
                .forEach(each -> result.append(each.toString()));
        return result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return Objects.equals(name, bank.name) &&
                Arrays.equals(accounts, bank.accounts) &&
                Objects.equals(totalTransers, bank.totalTransers) &&
                Objects.equals(totalProvision, bank.totalProvision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accounts, totalTransers, totalProvision);
    }

    public String totalProvision() {
        return totalProvision;
    }

    public String totalTransfers() {
        return totalTransers;
    }

    public Account[] getAccounts() {
        return accounts;
    }
}


/**
 * Account class
 */
class Account {
    private String name;
    private long id;
    private String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
        this.id = new Random().nextLong();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return String.format("Name: %s\nBalance: %s\n", name, balance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

/**
 * FlatAmountProvisionTransaction
 */
class FlatAmountProvisionTransaction extends Transaction {

    private final String flatAmount;

    public FlatAmountProvisionTransaction(long fromId, long toId, String amount, String flatAmount) {
        super(fromId, toId, amount, "FlatAmount");
        this.flatAmount = flatAmount;
    }

    @Override
    public String getProvision() {
        return flatAmount;
    }

    public String getFlatAmount() {
        return flatAmount;
    }
}

/**
 * FlatPercentProvisionTransaction
 */
class FlatPercentProvisionTransaction extends Transaction {
    private final int percent;

    public FlatPercentProvisionTransaction(long fromId, long toId, String amount, int percent) {
        super(fromId, toId, amount, "FlatPercent");
        this.percent = percent;
    }

    @Override
    public String getProvision() {
        long amount = Bank.toNumber(this.amount);
        amount = amount / 100;
        long provision = percent * amount;
        return Bank.toString(provision);
    }

    public int getPercent() {
        return percent;
    }
}

/**
 * Transaction
 */
abstract class Transaction {
    protected final long fromId;
    protected final long toId;
    protected final String amount;
    protected final String description;

    public Transaction(long fromId, long toId, String amount, String description) {
        this.fromId = fromId;
        this.toId = toId;
        this.amount = amount;
        this.description = description;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public abstract String getProvision();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(fromId, that.fromId) &&
                Objects.equals(toId, that.toId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromId, toId, amount, description);
    }
}



