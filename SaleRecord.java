package fop;

public class SaleRecord {
    String date, time, customerName, modelName, paymentMethod, employeeID;
    int quantity;
    double totalPrice;

    public SaleRecord(String date, String time, String customerName, String modelName,int quantity, double totalPrice, String paymentMethod, String employeeID) {
        this.date = date; 
        this.time = time;
        this.customerName = customerName;
        this.modelName = modelName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.employeeID = employeeID;
    }

    // Getters and Setters for Editing
    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getCustomerName() { return customerName; }
    public String getModelName() { return modelName; }
    public int getQuantity() { return quantity; }
    public String getPaymentMethod() { return paymentMethod ; }
    public double getTotalPrice() { return totalPrice; }
    public String getEmployeeID() { return employeeID; }

    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setModelName(String modelName) { this.modelName = modelName; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    
    public String toCSV() {
        return String.join(",", date, time, customerName, modelName, String.valueOf(quantity), String.valueOf(totalPrice),paymentMethod, employeeID);
    }
    
    public String toString() { return date + " | " + customerName + " | " + modelName + " | RM" + totalPrice; }
}   

