/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author yiren
 */
public class WatchModel {
    private final String model;
    private final double price;
    // stockQuantities stores the values from C60, C61, ..., C69
    private final int[] stockQuantities; 

    /**
     * Constructor to create a new WatchModel object.
     * @param model The watch model name (e.g., "DW2300-1").
     * @param price The unit price.
     * @param stockQuantities An array of 10 integers for stock in locations C60-C69.
     */
    public WatchModel(String model, double price, int[] stockQuantities) {
        this.model = model;
        this.price = price;
        this.stockQuantities = stockQuantities;
    }

    // --- Getters ---
    public String getModel() {
        return model;
    }

    public double getPrice() {
        return price;
    }

    public int[] getStockQuantities() {
        return stockQuantities;
    }

    // --- Business Logic ---

    /**
     * Calculates the total stock across all locations (C60-C69).
     * @return The sum of all stock quantities.
     */
    public int getTotalStock() {
        int total = 0;
        for (int quantity : stockQuantities) {
            total += quantity;
        }
        return total;
    }

    /**
     * Updates the stock quantity for a specific location.
     * @param locationIndex Index from 0-9 (0 for C60, 9 for C69).
     * @param newQuantity The new stock level for that location.
     */
    public void updateStock(int locationIndex, int newQuantity) {
        if (locationIndex >= 0 && locationIndex < stockQuantities.length) {
            this.stockQuantities[locationIndex] = newQuantity;
        }
    }

    /**
     * Overridden toString for clean display in the stock list.
     * @return 
     */
    @Override
    
    public String toString() {
        return String.format("%-12s | RM %-8.2f | %-12d", model, price, getTotalStock());
    }
}
