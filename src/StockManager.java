/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yiren
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StockManager {
    private final List<WatchModel> inventory;
    // Define the path to your CSV file
    private final String FILE_PATH = "model.csv"; 

    public StockManager() {
        this.inventory = new ArrayList<>();
        loadInventoryFromCSV();
    }

    /**
     * Reads data from model.csv and populates the inventory list.
     */
    private void loadInventoryFromCSV() {
        inventory.clear();
        String line;
        
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            // Read and ignore the header line: Model,Price,C60,...
            br.readLine(); 
            
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                
                if (data.length < 12) {
                    System.err.println("Skipping malformed row (not enough columns): " + line);
                    continue; 
                }
                
                try {
                    String model = data[0].trim();
                    double price = Double.parseDouble(data[1].trim());
                    
                    // Extract 10 stock quantities (from index 2 to 11)
                    int[] quantities = new int[10]; 
                    for (int i = 0; i < 10; i++) {
                        quantities[i] = Integer.parseInt(data[i + 2].trim());
                    }
                    
                    inventory.add(new WatchModel(model, price, quantities));
                    
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing number (price or stock) in line: " + line);
                }
            }
            System.out.println("✅ Inventory loaded successfully: " + inventory.size() + " models.");

        } catch (IOException e) {
            System.err.println("❌ ERROR: Could not read CSV file. Check if 'model.csv' is in the project root.");
        }
    }

    /**
     * Displays all watch models, sorted by Total Stock (descending).
     */
    public void displayAllStock() {
        System.out.println("\n==================================");
        System.out.println("       GoldenHour Stock List      ");
        System.out.println("==================================");
        System.out.println("Model        | Price (RM) | Total Stock ");
        System.out.println("-------------|------------|--------------");
        
        if (inventory.isEmpty()) {
            System.out.println("No models in stock. Please check CSV file.");
            return;
        }

        // Sorting: Sort by Total Stock - Descending (highest stock first)
        // This fulfills the 'Sort Mechanisms' prerequisite.
        inventory.sort((m1, m2) -> Integer.compare(m2.getTotalStock(), m1.getTotalStock()));
        
        for (WatchModel model : inventory) {
            System.out.println(model);
        }
        System.out.println("==================================\n");
    }

    /**
     * Searches for a model based on user input (Linear Search).
     * @param scanner
     */
    public void searchStock(Scanner scanner) {
        System.out.print("Enter model name (or part of it) to search: ");
        String searchKey = scanner.nextLine().trim();
        
        WatchModel foundModel = null;
        // Linear Search (fulfilling 'Search Mechanisms' prerequisite)
        for (WatchModel model : inventory) {
            if (model.getModel().toLowerCase().contains(searchKey.toLowerCase())) {
                foundModel = model;
                break; // Found the first matching model
            }
        }
        
        if (foundModel != null) {
            System.out.println("\n✅ Model Found:");
            System.out.println("----------------------------------");
            System.out.println("Model: " + foundModel.getModel());
            System.out.println("Price: RM " + String.format("%.2f", foundModel.getPrice()));
            System.out.println("Total Stock: " + foundModel.getTotalStock());
            
            // Display stock breakdown
            System.out.print("Stock Breakdown: ");
            int[] quantities = foundModel.getStockQuantities();
            for (int i = 0; i < quantities.length; i++) {
                System.out.print("C" + (60 + i) + ": " + quantities[i] + (i < 9 ? " | " : ""));
            }
            System.out.println("\n----------------------------------");
            
        } else {
            System.out.println("❌ Model '" + searchKey + "' not found.");
        }
    }
    
    /**
     * Prompts the user to update the stock quantity for a specific model/location.
     * @param scanner
     */
    public void updateStock(Scanner scanner) {
        System.out.print("Enter model name to update stock: ");
        String searchKey = scanner.nextLine().trim();
        
        WatchModel foundModel = null;
        for (WatchModel model : inventory) {
            if (model.getModel().equalsIgnoreCase(searchKey)) {
                foundModel = model;
                break;
            }
        }

        if (foundModel == null) {
            System.out.println("❌ Model '" + searchKey + "' not found. Update failed.");
            return;
        }
        
        System.out.println("\n--- Current Stock for " + foundModel.getModel() + " (Total: " + foundModel.getTotalStock() + ") ---");
        int[] quantities = foundModel.getStockQuantities();
        for (int i = 0; i < quantities.length; i++) {
            // Display location names C60, C61, etc., mapped to menu numbers 1-10
            System.out.println((i+1) + ". C" + (60 + i) + ": " + quantities[i]);
        }
        
        try {
            System.out.print("Enter location number to update (1-10, e.g., 1 for C60): ");
            int locationChoice = Integer.parseInt(scanner.nextLine().trim());
            
            if (locationChoice < 1 || locationChoice > 10) {
                System.out.println("❌ Invalid location number. Must be between 1 and 10.");
                return;
            }
            
            System.out.print("Enter NEW quantity for C" + (59 + locationChoice) + ": ");
            int newQuantity = Integer.parseInt(scanner.nextLine().trim());
            
            if (newQuantity < 0) {
                System.out.println("❌ Stock quantity cannot be negative.");
                return;
            }
            
            // Update the stock (uses WatchModel's setter)
            foundModel.updateStock(locationChoice - 1, newQuantity);
            
            // NOTE: For a persistent application, you would need a saveInventoryToCSV() method here 
            // to write the updated data back to the file.
            System.out.println("\n✅ Stock updated successfully for " + foundModel.getModel() + " at C" + (59 + locationChoice) + ".");
            System.out.println("   New Total Stock for model: " + foundModel.getTotalStock());
            
        } catch (NumberFormatException e) {
            // Error Handling prerequisite fulfilled
            System.out.println("❌ Invalid input. Please enter a number for choice/quantity.");
        }
    }
}
