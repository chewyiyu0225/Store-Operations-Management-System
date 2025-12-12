import java.util.List;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService auth = new AuthService();
        AttendanceService attendance = new AttendanceService();
        StockManager stock = new StockManager();

        System.out.println("Welcome to Store Operations Management System");

        while (true) {
            if (!performLogin(scanner, auth)) {
                break;
            }

            boolean sessionActive = true;
            while (sessionActive) {
                Employee user = auth.getCurrentUser();
                System.out.println("\nLogged in as: " + user.getId() + " (" + user.getRole() + ")");
                System.out.println("===== Main Menu =====");
                System.out.println("1. View All Stock (Sorted)");
                System.out.println("2. Search Stock by Model");
                System.out.println("3. Update Stock Quantity");
                System.out.println("4. Register Employee (Manager)");
                System.out.println("5. Clock IN");
                System.out.println("6. Clock OUT");
                System.out.println("7. View Attendance (Manager)");
                System.out.println("8. Logout");
                System.out.println("0. Exit Application");
                System.out.print("Enter choice: ");

                int choice = readInt(scanner);
                switch (choice) {
                    case 1:
                        stock.displayAllStock();
                        break;
                    case 2:
                        stock.searchStock(scanner);
                        break;
                    case 3:
                        stock.updateStock(scanner);
                        break;
                    case 4:
                        registerEmployee(scanner, auth);
                        break;
                    case 5:
                        attendance.mark(user, "IN", auth);
                        break;
                    case 6:
                        attendance.mark(user, "OUT", auth);
                        break;
                    case 7:
                        viewAttendance(attendance, user);
                        break;
                    case 8:
                        attendance.mark(user, "OUT", auth);
                        auth.logout();
                        System.out.println("Logged out. Returning to login screen.");
                        sessionActive = false;
                        break;
                    case 0:
                        attendance.mark(user, "OUT", auth);
                        System.out.println("Goodbye.");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Try again.");
                        break;
                }
            }
        }
        scanner.close();
    }

    private static boolean performLogin(Scanner scanner, AuthService auth) {
        while (true) {
            System.out.println("\n=== Employee Login ===");
            System.out.println();
            System.out.print("Enter User ID: ");
            String id = scanner.nextLine().trim();
            if (id.equalsIgnoreCase("q")) {
                return false;
            }
            System.out.print("\nEnter Password: ");
            String pw = scanner.nextLine().trim();

            if (auth.login(id, pw)) {
                Employee user = auth.getCurrentUser();
                System.out.println();
                System.out.println("Login Successful!");
                System.out.println();
                System.out.println("Welcome, " + user.getName() + " (" + user.getOutletCode() + ")");
                return true;
            }
            System.out.println();
            System.out.println("Login Failed: Invalid User ID or Password.");
        }
    }

    private static void registerEmployee(Scanner scanner, AuthService auth) {
        Employee current = auth.getCurrentUser();
        if (current == null || !"Manager".equalsIgnoreCase(current.getRole())) {
            System.out.println("Only Manager can register employees.");
            return;
        }

        System.out.println("\n=== Register New Employee ===");
        System.out.println();
        System.out.print("Enter Employee Name: ");
        String name = scanner.nextLine().trim();
        System.out.println();
        System.out.print("Enter Employee ID: ");
        String id = scanner.nextLine().trim();
        System.out.println();
        System.out.print("Set Password: ");
        String pw = scanner.nextLine().trim();
        System.out.println();
        System.out.print("Set Role: ");
        String role = scanner.nextLine().trim();
        System.out.println();
        
        // Default outlet assignment (outlet management handled by other team member)
        String outlet = "C60";

        boolean ok = auth.register(id, name, role, pw, outlet);
        if (ok) {
            System.out.println("\nEmployee successfully registered!");
        } else {
            System.out.println("\nRegistration failed. ID may exist or fields are empty.");
        }
    }

    private static void viewAttendance(AttendanceService attendance, Employee user) {
        if (!"Manager".equalsIgnoreCase(user.getRole())) {
            System.out.println("Only Manager can view attendance logs.");
            return;
        }
        List<AttendanceEntry> entries = attendance.readAll();
        if (entries.isEmpty()) {
            System.out.println("No attendance records yet.");
            return;
        }
        System.out.println("EmployeeID | Date | Time | Status");
        for (AttendanceEntry e : entries) {
            System.out.println(e.getEmployeeId() + " | " + e.getDate() + " | " + e.getTime() + " | " + e.getStatus());
        }
    }

    private static int readInt(Scanner scanner) {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}