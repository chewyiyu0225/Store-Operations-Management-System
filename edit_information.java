package fop;

import java.util.List;
import java.util.Scanner;

class edit_information {
    
    // Memory Lists
    public static List<WatchModel> stockList;
    public static List<SaleRecord> salesList;
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 1. DATA LOAD STATE 
        stockList = data.loadModels();
        salesList = data.loadSales();

        boolean a = true;
        while (a) {
            System.out.println("\n--- EDIT INFORMATION MENU ---");
            System.out.println("1. Edit Stock Quantity/Price");
            System.out.println("2. Edit Sales Transaction");
            System.out.println("3. Exit");
            System.out.print("Select: ");
            if(sc.hasNextInt()){
                int choice = sc.nextInt();
                sc.nextLine();
            
                switch (choice) {
                    case 1:
                      editStock();
                      break;
                    case 2:
                      editSalesInformation();
                      break;
                    case 3:
                      System.out.println("Exiting Program. Goodbye!");
                      a = false;
                      break;
                    default:
                      System.out.println("Invalid option. Try again.");
                      break;
                }
            }
            else{
                String badInput = sc.next(); 
                System.out.println("Invalid input: " + badInput + ". Please enter a number.");
                sc.nextLine();       
            }
        }
    }

    // --- EDIT INFORMATION IMPLEMENTATION ---

    // Requirement: Edit Stock-related data 
    public static void editStock() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter Model Name to Edit: ");
        String name = sc.nextLine();
        System.out.println("=== Edit Stock Information ===");
        System.out.print("Enter Model Name: ");
        System.out.println();
        String name = sc.nextLine();
        
        // Search Mechanism
        for (WatchModel m : stockList) {
            if (m.getModelName().equalsIgnoreCase(name)) {
                System.out.println("Current Stock: " + m);
                System.out.print("Enter New Stock Value: ");
                int newQty = sc.nextInt();
                
                // Modify Object
                m.setQuantity(newQty);

                System.out.print("Stock information updated successfully.");
                
                // STORAGE SYSTEM CALL 
                data.saveModels(stockList); 
                return;
            }
        }
        System.out.println("Model not found.");
    }
    
    public static void editSalesInformation() {
        Scanner sc = new Scanner(System.in);
        System.out.println("=== Edit Sales Information ===");

        // 1. INPUTS
        System.out.print("Enter Transaction Date: ");
        String searchDate = sc.nextLine().trim();

        System.out.print("Enter Customer Name: ");
        String searchName = sc.nextLine().trim();
        System.out.println();

        // 2. SEARCH MECHANISM
        SaleRecord foundRecord = null;
        for (SaleRecord sale : salesList) {
            // Check both Date and Name to be precise
            if (sale.getDate().equalsIgnoreCase(searchDate) && 
                sale.getCustomerName().equalsIgnoreCase(searchName)) {
                foundRecord = sale;
                break;
            }
        }

        // 3. DISPLAY & EDIT
        if (foundRecord != null) {
            System.out.println("Sales Record Found:");
            System.out.println();
            
            // Custom display format 
            System.out.println("Model: " + foundRecord.getModelName() + "   Quantity: " + foundRecord.getQuantity());
            System.out.println("Total: RM" + foundRecord.getTotalPrice());
            System.out.println("Transaction Method: " + foundRecord.getPaymentMethod());
            System.out.println();

            System.out.println("Select number to edit:");
            System.out.println();
            System.out.println("1. Name    2. Model    3. Quantity    4. Total");
            System.out.println("5. Transaction Method"); 
            System.out.print("> ");
            int choice = sc.nextInt();
            System.out.println();

            // Variables to hold potential new values
            String newValue = "";
            double newDouble = 0;
            int newInt = 0;

            // 4. HANDLING USER CHOICE
            switch (choice) {
                case 1:
                    System.out.print("Enter New Customer Name: ");
                    newValue = sc.nextLine();
                    break;
                case 2:
                    System.out.print("Enter New Model: ");
                    newValue = sc.nextLine();
                    break;
                case 3:
                    System.out.print("Enter New Quantity: ");
                    newInt = sc.nextInt();
                    sc.nextLine();
                    break;
                case 4:
                    System.out.print("Enter New Total: RM");
                    newDouble = sc.nextDouble();
                    sc.nextLine();
                    break;
                case 5:
                    System.out.print("Enter New Transaction Method: ");
                    newValue = sc.nextLine();
                    break;
                default:
                    System.out.println("Invalid option.");
                    return;
            }

            // 5. CONFIRMATION
            System.out.print("Confirm Update? (Y/N): ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("Y")) {
                switch (choice) {
                    case 1: 
                        foundRecord.setCustomerName(newValue); 
                        break;
                    case 2: 
                        foundRecord.setModelName(newValue); 
                        break;
                    case 3: 
                        foundRecord.setQuantity(newInt); 
                        break;
                    case 4: 
                        foundRecord.setTotalPrice(newDouble); 
                        break;
                    case 5: 
                        foundRecord.setPaymentMethod(newValue); 
                        break;
                }

                // 6. STORAGE SYSTEM SAVE
                data.saveSales(salesList);
                
                System.out.println();
                System.out.println("Sales information updated successfully.");
            } else {
                System.out.println("Update cancelled.");
            }

        } else {
            System.out.println("Sales Record Not Found.");
        }
    }

}
