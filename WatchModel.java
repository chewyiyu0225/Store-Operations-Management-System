package fop;

public class WatchModel {
    String modelName1;
    double price1;
    int quantity1;

    public WatchModel(String modelName, double price, int quantity) {
        modelName1 = modelName;
        price1 = price;
        quantity1 = quantity;
    }

    // Getters and Setters needed for "Edit Information" later
    public String getModelName() { return modelName1; }
    public double getPrice() { return price1; }
    public void setPrice(double price) { price1 = price; }
    public int getQuantity() { return quantity1; }
    public void setQuantity(int quantity) { quantity1 = quantity; }

    public String toCSV() { return modelName1 + "," + price1 + "," + quantity1; }
    public String toString() { return modelName1 + " | RM" + price1 + " | Qty: " + quantity1; }
}
