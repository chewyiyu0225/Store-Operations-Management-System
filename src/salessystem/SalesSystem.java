package salessystem;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SalesSystem {

    // --- COLORS FOR TEXT
    static final String RESET = "\u001B[0m";
    static final String GREEN = "\u001B[32m";
    static final String CYAN = "\u001B[36m";
    static final String RED = "\u001B[31m";

    // --- GLOBAL VARIABLES ---
    static ArrayList<Watch> inventory = new ArrayList<>();
    static ArrayList<Employee> staffList = new ArrayList<>(); // <--- NEW: List of employees
    static Employee currentUser = null;                       // <--- NEW: Stores who is logged in
    static Scanner input = new Scanner(System.in);
    
    // Outlet Names (C60-C69)
    static String[] outlets = {
        "KLCC", "MidValley", "Sunway Velocity", "IOI City Mall", "Lalaport", 
        "KL East", "Nu Sentral", "Pavillion KL", "1 Utama", "MyTown"
    };

    // --- MAIN METHOD ---
    public static void main(String[] args) {
        createDatabaseFile();   // Auto-create watches.txt
        createEmployeeFile();   // <--- NEW: Auto-create employees.txt if missing
        loadWatches();          // Load watch data
        loadEmployees();        // <--- NEW: Load employee data

        performLogin();         // <--- NEW: Force login before menu

        while (true) {
            // Display User Info in Menu
            System.out.println("\n=== GOLDEN HOUR MENU ===");
            System.out.println("User: " + CYAN + currentUser.name + RESET + " (" + currentUser.role + ")");
            System.out.println("1. Sales System");
            System.out.println("2. Search Information");
            System.out.println("3. Exit");
            System.out.print("Select option: ");
            
            try {
                int choice = input.nextInt(); 
                input.nextLine(); // Fix "Enter" key bug

                if (choice == 1) runSalesSystem();
                else if (choice == 2) runSearchInfo();
                else if (choice == 3) break;
                else System.out.println("Invalid option.");
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number.");
                input.nextLine();
            }
        }
    }

    // ==========================================
    // MODULE 1: SALES SYSTEM
    // ==========================================
    public static void runSalesSystem() {
        System.out.println("\n=== Record New Sale ===");
        
        // 1. Date & Time Format (e.g. 02:50 p.m.)
        String date = LocalDate.now().toString();
        String timeRaw = LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm a"));
        String time = timeRaw.toLowerCase().replace("am", "a.m.").replace("pm", "p.m.");
        
        System.out.println("Date: " + date);
        System.out.println("Time: " + time);

        // 2. Customer
        System.out.print("Customer Name: "); 
        String customer = input.nextLine();
        
        System.out.println("Item(s) Purchased:");
        
        double subtotal = 0;
        StringBuilder itemsLog = new StringBuilder();
        boolean firstItem = true;
        
        // 3. Item Loop
        while (true) {
            System.out.print("Enter Model: ");
            String name = input.nextLine();
            Watch w = findWatch(name);

            if (w != null) {
                System.out.print("Enter Quantity: ");
                try {
                    int qty = input.nextInt(); input.nextLine();
                    int currentStock = w.stock[0]; // Assuming outlet 0 (KLCC)

                    if (qty <= currentStock && qty > 0) {
                        System.out.println("Unit Price: RM" + (int)w.price);
                        
                        // Update Logic
                        w.stock[0] -= qty;
                        double lineTotal = w.price * qty;
                        subtotal += lineTotal;
                        
                        // Format for log
                        if (!firstItem) itemsLog.append(", ");
                        itemsLog.append(w.name).append(" Quantity: ").append(qty);
                        firstItem = false;

                    } else {
                        System.out.println("Error: Insufficient stock (Current: " + currentStock + ")");
                    }
                } catch (Exception e) {
                    System.out.println("Invalid quantity."); input.nextLine();
                }
            } else {
                System.out.println("Error: Model not found.");
            }

            System.out.print("Are there more items purchased? (Y/N): ");
            String reply = input.nextLine();
            if (reply.equalsIgnoreCase("N")) break; 
        }

        // 4. Payment & Completion
        if (subtotal > 0) {
            System.out.println(); // Blank line
            System.out.print("Enter transaction method: "); 
            String method = input.nextLine(); 
            
            System.out.println("Subtotal: RM" + (int)subtotal);
            System.out.println(); // Blank line

            // Success Messages (GREEN COLOR to match screenshot)
            System.out.println("Transaction " + GREEN + "successful." + RESET);
            System.out.println("Sale recorded " + GREEN + "successfully." + RESET);
            System.out.println("Model quantities updated " + GREEN + "successfully." + RESET);
            System.out.println("Receipt generated: sales_" + date + ".txt");
            
            // <--- CHANGED: Now passing currentUser.name to the save function
            saveTransaction(date, time, customer, itemsLog.toString(), subtotal, method, currentUser.name);
        }
    }

    // ==========================================
    // MODULE 2: SEARCH INFORMATION
    // ==========================================
    static void runSearchInfo() {
        System.out.println("\n=== Search Stock Information ==="); 
        System.out.println("1. Stock Information");
        System.out.println("2. Sales Information");
        System.out.print("Select: ");
        int type = input.nextInt(); input.nextLine();

        if (type == 1) {
            // --- SEARCH STOCK ---
            System.out.println("\n=== Search Stock Information ===");
            System.out.print("Search Model Name: ");
            String name = input.nextLine();
            
            // CYAN COLOR for "Searching..."
            System.out.println(CYAN + "Searching..." + RESET + "\n");

            Watch w = findWatch(name);
            if (w != null) {
                System.out.println("Model: " + w.name);
                System.out.println("Unit Price: RM" + (int)w.price);
                System.out.println("Stock by Outlet:");
                
                // Formatted Output (4 items per row to match screenshot grid)
                int count = 0;
                for (int i = 0; i < outlets.length; i++) {
                    // %-15s creates padding for alignment
                    System.out.printf("%-15s: %-3d  ", outlets[i], w.stock[i]);
                    count++;
                    if (count % 4 == 0) System.out.println(); 
                }
                System.out.println(); 
            } else System.out.println("Model not found.");

        } else if (type == 2) {
            // --- SEARCH SALES ---
            System.out.println("\n=== Search Sales Information ===");
            System.out.print("Search keyword: ");
            String key = input.nextLine();
            System.out.println(CYAN + "Searching..." + RESET + "\n");

            boolean found = false;
            try {
                Scanner file = new Scanner(new File("sales_db.txt"));
                while (file.hasNextLine()) {
                    String line = file.nextLine();
                    // Database format: Date|Time|Customer|Items|Total|Method|EmployeeName
                    if (line.toLowerCase().contains(key.toLowerCase())) {
                        String[] parts = line.split("\\|"); 
                        if (parts.length >= 6) {
                            System.out.println("Sales Record Found:");
                            System.out.println("Date: " + parts[0].trim() + "\t\tTime: " + parts[1].trim());
                            System.out.println("Customer: " + parts[2].trim());
                            
                            String itemsRaw = parts[3].trim(); 
                            System.out.println("Item(s): " + itemsRaw); 
                            
                            System.out.println("Total: " + parts[4].trim());
                            System.out.println("Transaction Method: " + parts[5].trim());
                            
                            // <--- CHANGED: Read employee name from file if it exists
                            String emp = (parts.length > 6) ? parts[6].trim() : "Unknown";
                            System.out.println("Employee: " + emp); 
                            
                            System.out.println("Status: Transaction verified.");
                            System.out.println("------------------------------------------------");
                            found = true;
                        }
                    }
                }
                if (!found) System.out.println("No records found.");
            } catch (Exception e) { System.out.println("No sales history found."); }
        }
    }

    // ==========================================
    // HELPER METHODS
    // ==========================================

    // <--- CHANGED: Added staffName parameter
    static void saveTransaction(String date, String time, String cust, String items, double total, String method, String staffName) {
        try {
            FileWriter db = new FileWriter("sales_db.txt", true);
            // Save with delimiters | for easy reading. ADDED staffName at the end.
            db.write(date + "|" + time + "|" + cust + "|" + items + "|RM" + (int)total + "|" + method + "|" + staffName + "\n");
            db.close();
            
            FileWriter receipt = new FileWriter("sales_" + date + ".txt", true);
            receipt.write("=== Receipt ===\n");
            receipt.write("Date: " + date + "  Time: " + time + "\n");
            receipt.write("Customer: " + cust + "\n");
            receipt.write("Items: " + items + "\n");
            receipt.write("Total: RM" + (int)total + "\n");
            receipt.write("Method: " + method + "\n");
            receipt.write("Served by: " + staffName + "\n"); // <--- ADDED to receipt
            receipt.write("Status: Paid\n");
            receipt.write("--------------------------------\n\n");
            receipt.close();
        } catch (IOException e) {
            System.out.println("Error saving files.");
        }
    }
    
    // --- NEW: LOGIN METHOD ---
    static void performLogin() {
        while (true) {
            System.out.println("\n=== SYSTEM LOGIN ===");
            System.out.print("Enter Employee ID: ");
            String id = input.nextLine();
            System.out.print("Enter Password: ");
            String pass = input.nextLine();

            boolean found = false;
            for (Employee e : staffList) {
                if (e.id.equalsIgnoreCase(id) && e.password.equals(pass)) {
                    currentUser = e; // Set global user
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println(GREEN + "Login Successful! Welcome, " + currentUser.name + RESET);
                break;
            } else {
                System.out.println(RED + "Invalid ID or Password. Please try again." + RESET);
            }
        }
    }

    // --- NEW: LOAD EMPLOYEES ---
    static void loadEmployees() {
        try {
            Scanner file = new Scanner(new File("employees.txt"));
            while (file.hasNextLine()) {
                String line = file.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                // Expecting: ID, Name, Role, Password
                if (parts.length >= 4) {
                    staffList.add(new Employee(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Error: employees.txt not found.");
        }
    }
    
    // --- NEW: AUTO-CREATE EMPLOYEE FILE (For testing) ---
    static void createEmployeeFile() {
        File f = new File("employees.txt");
        if (!f.exists()) {
            try {
                FileWriter fw = new FileWriter(f);
                fw.write("C6001,Tan Guan Han,Manager,a2b1c0\n");
                fw.write("C6002,Adam bin Abu,Full-time,d3e4f5\n");
                fw.write("C6003,Jinu Saja,Part-time,g6h7i8\n");
                fw.write("C6004,Wong Benedict,Part-time,j9k0l1\n");
                fw.write("C6005,Dhaarane,Full-time,m2n3o4\n");
                fw.write("C6006,Adriana Nur Zahra binti Helmi,Part-time,p5q6r7\n");
                fw.write("C6007,Kang Rumi,Full-time,s8t9u0\n");
                fw.write("C6008,Li Xinyi,Full-time,v1w2x3\n");
                fw.write("C6009,Megan Mei,Part-time,y4z5a6\n");
                fw.write("C6010,Aminah binti Said,Full-time,b7c8d9\n");
                fw.write("C6011,Jarjit Singh,Part-time,e0f1g2\n");
                fw.write("C6012,Khairul Ariffin bin Muhammad,Part-time,h3i4j5\n");
                fw.close();
            } catch (IOException e) { }
        }
    }

    static void createDatabaseFile() {
        File f = new File("watches.txt");
        if (!f.exists()) {
            try {
                FileWriter fw = new FileWriter(f);
                fw.write("Model,Price,C60,C61,C62,C63,C64,C65,C66,C67,C68,C69\n");
                fw.write("DW2300-4,349,1,1,0,0,3,1,2,2,0,1\n");
                fw.write("SW2500-1,845,4,3,4,2,2,3,2,5,1,1\n");
                fw.close();
            } catch (IOException e) {}
        }
    }

    static void loadWatches() {
        try {
            Scanner file = new Scanner(new File("watches.txt"));
            if (file.hasNextLine()) file.nextLine(); 
            while (file.hasNextLine()) {
                String line = file.nextLine();
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",");
                String name = parts[0];
                double price = Double.parseDouble(parts[1]);
                int[] stock = new int[10];
                for (int i = 0; i < 10; i++) stock[i] = Integer.parseInt(parts[i + 2]);
                inventory.add(new Watch(name, price, stock));
            }
        } catch (Exception e) {}
    }

    static Watch findWatch(String name) {
        for (Watch w : inventory) if (w.name.equalsIgnoreCase(name)) return w;
        return null;
    }

    // --- CLASSES ---
    static class Watch {
        String name; double price; int[] stock;
        public Watch(String n, double p, int[] s) { name = n; price = p; stock = s; }
    }
    
    // --- NEW CLASS: EMPLOYEE ---
    static class Employee {
        String id, name, role, password;
        public Employee(String id, String name, String role, String pass) {
            this.id = id; this.name = name; this.role = role; this.password = pass;
        }
    }
}