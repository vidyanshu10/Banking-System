import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

public class AccountDAO {

    public boolean accountExists(String email) {

        String query = "SELECT account_number FROM accounts WHERE emain = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean openAccount(String name, String email, String pin) {

        long accountNumber = generateAccountNumber();

        String query =
            "INSERT INTO accounts (account_number, full_name, emain, balance, security_pin) " +
            "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setLong(1, accountNumber);
            ps.setString(2, name);
            ps.setString(3, email);
            ps.setDouble(4, 0.0);
            ps.setString(5, pin);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public double checkBalance(String email) {

        String query = "SELECT balance FROM accounts WHERE emain = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getDouble("balance");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean withdraw(String email, String pin, double amount) {

    String checkQuery =
        "SELECT balance FROM accounts WHERE emain = ? AND security_pin = ?";

    String updateQuery =
        "UPDATE accounts SET balance = balance - ? WHERE emain = ?";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement checkPs = con.prepareStatement(checkQuery)) {

        checkPs.setString(1, email);
        checkPs.setString(2, pin);

        ResultSet rs = checkPs.executeQuery();

        // invalid PIN
        if (!rs.next()) {
            return false;
        }

        double currentBalance = rs.getDouble("balance");

        // insufficient balance
        if (currentBalance < amount) {
            System.out.println("❌ Insufficient balance");
            return false;
        }

        try (PreparedStatement updatePs =
                 con.prepareStatement(updateQuery)) {

            updatePs.setDouble(1, amount);
            updatePs.setString(2, email);

            return updatePs.executeUpdate() > 0;
        }

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}





    public boolean deposit(String email, String pin, double amount) {

        String checkPin =
            "SELECT * FROM accounts WHERE emain = ? AND security_pin = ?";
        String update =
            "UPDATE accounts SET balance = balance + ? WHERE emain = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(checkPin)) {

            ps.setString(1, email);
            ps.setString(2, pin);

            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return false;

            try (PreparedStatement ups = con.prepareStatement(update)) {
                ups.setDouble(1, amount);
                ups.setString(2, email);
                return ups.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private long generateAccountNumber() {
        return 1000000000L + new Random().nextInt(900000000);
    }
}
