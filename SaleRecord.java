package fop;

public class SaleRecord {
    String date1, time1, customerName1, modelName1, paymentMethod1, employeeID1;
    int quantity1;
    double totalPrice1;

    public SaleRecord(String date, String time, String customerName, String modelName,int quantity, double totalPrice, String paymentMethod, String employeeID) {
        date1 = date; 
        time1 = time;
        customerName1 = customerName;
        modelName1 = modelName;
        quantity1 = quantity;
        totalPrice1 = totalPrice;
        paymentMethod1 = paymentMethod;
        employeeID1 = employeeID;
    }

    // Getters and Setters for Editing
    public String getDate() { return date1; }
    public String getTime() { return time1; }
    public String getCustomerName() { return customerName1; }
    public String getModelName() { return modelName1; }
    public int getQuantity() { return quantity1; }
    public void getPaymentMethod(String method) { paymentMethod1 = method; }
    public void getTotalPrice(double price) { totalPrice1 = price; }
    public String getEmployeeID() { return employeeID1; }

    public void setCustomerName(String customerName) { customerName1 = customerName; }
    public void setModelName(String modelName) { modelName1 = modelName; }
    public void setQuantity(int quantity) { quantity1 = quantity; }
    public void setTotalPrice(double totalPrice) { totalPrice1 = totalPrice; }
    public void setPaymentMethod(String paymentMethod) { paymentMethod1 = paymentMethod; }

    
    public String toCSV() {
        return String.join(",", date1, time1, customerName1, modelName1, String.valueOf(quantity1), String.valueOf(totalPrice1),paymentMethod1, employeeID1);
    }
    
    public String toString() { return date1 + " | " + customerName1 + " | " + modelName1 + " | RM" + totalPrice1; }

    String getTotalPrice() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    String getPaymentMethod() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}   

