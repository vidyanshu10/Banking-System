public class AccountManager {

    private AccountDAO accountDAO;

    public AccountManager() {
        this.accountDAO = new AccountDAO();
    }

    public double checkBalance(String email) {
        return accountDAO.checkBalance(email);
    }

    public boolean deposit(String email, String pin, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive");
            return false;
        }
        return accountDAO.deposit(email, pin, amount);
    }

    public boolean withdraw(String email, String pin, double amount) {
        if (amount <= 0) {
            System.out.println("❌ Amount must be positive");
            return false;
        }
        return accountDAO.withdraw(email, pin, amount);
    }
}
