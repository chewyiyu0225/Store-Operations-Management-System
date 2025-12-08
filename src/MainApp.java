/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yiren
 */
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) {
        StockManager manager = new StockManager();
        Scanner scanner = new Scanner(System.in);
        int choice = -1;

        while (choice != 0) {
            System.out.println("\n===== Stock Management Menu =====");
            System.out.println("1. View All Stock (Sorted)");
            System.out.println("2. Search Stock by Model");
            System.out.println("3. Update Stock Quantity");
            System.out.println("0. Exit Stock Management");
            System.out.println("===============================");
            System.out.print("Enter your choice: ");
            
            try {
                String input = scanner.nextLine();
                choice = Integer.parseInt(input);

                switch (choice) {
                    case 1 -> manager.displayAllStock();
                    case 2 -> manager.searchStock(scanner);
                    case 3 -> manager.updateStock(scanner);
                    case 0 -> System.out.println("Exiting Stock Management. Goodbye!");
                    default -> System.out.println("Invalid choice. Please enter 0, 1, 2, or 3.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid input. Please enter a number.");
                choice = -1; // Reset choice to keep the loop going
            }
        }
        
        scanner.close();
    }
}