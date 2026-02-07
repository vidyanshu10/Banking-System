import java.util.Scanner;

public class Banking_System {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        AccountDAO accountDAO = new AccountDAO();   // ✅ REQUIRED
        AccountManager manager = new AccountManager();

        // 🔁 MAIN MENU LOOP
        while (true) {
            System.out.println("\n===== BANKING SYSTEM =====");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            // 🔹 REGISTER
            if (choice == 1) {

                System.out.print("Enter Full Name: ");
                String name = sc.nextLine();

                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                if (userDAO.register(name, email, password)) {
                    System.out.println("✅ Registration Successful");
                } else {
                    System.out.println("❌ User already exists");
                }

            }
            // 🔹 LOGIN
            else if (choice == 2) {

                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                System.out.print("Enter Password: ");
                String password = sc.nextLine();

                if (!userDAO.login(email, password)) {
                    System.out.println("❌ Invalid email or password");
                    continue;
                }

                System.out.println("✅ Login Successful");

                // create account if not exists
                if (!accountDAO.accountExists(email)) {
                    String name = userDAO.getUserName(email);

                    System.out.print("Enter 4-digit Security PIN: ");
                    String pin = sc.nextLine();

                    accountDAO.openAccount(name, email, pin);
                    System.out.println("🏦 Account Created Successfully");
                }

                // 🔁 ACCOUNT MENU LOOP
                while (true) {
                    System.out.println("\n===== ACCOUNT MENU =====");
                    System.out.println("1. Check Balance");
                    System.out.println("2. Deposit");
                    System.out.println("3. Withdraw");
                    System.out.println("4. Logout");
                    System.out.print("Choose option: ");

                    int accChoice = sc.nextInt();
                    sc.nextLine();

                    if (accChoice == 1) {
                        double balance = manager.checkBalance(email);
                        System.out.println("Current Balance: Rs. " + balance);
                    }
                    else if (accChoice == 2) {

                        System.out.print("Enter PIN: ");
                        String pin = sc.nextLine();

                        System.out.print("Enter Deposit Amount: ");
                        double amt = sc.nextDouble();
                        sc.nextLine();

                        if (manager.deposit(email, pin, amt)) {
                            System.out.println("✅ Deposit Successful");
                        } else {
                            System.out.println("❌ Deposit Failed");
                        }
                    }
                    else if (accChoice == 3) {

                        System.out.print("Enter PIN: ");
                        String pin = sc.nextLine();

                        System.out.print("Enter Withdraw Amount: ");
                        double amt = sc.nextDouble();
                        sc.nextLine();

                        if (manager.withdraw(email, pin, amt)) {
                            System.out.println("✅ Withdrawal Successful");
                        } else {
                            System.out.println("❌ Withdrawal Failed");
                        }
                    }
                    else {
                        System.out.println("Logged out successfully");
                        break;
                    }
                }
            }
            else {
                System.out.println("Thank you for using Banking System");
                break;
            }
        }

        sc.close();
    }
}
