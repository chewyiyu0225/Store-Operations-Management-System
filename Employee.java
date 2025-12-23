package fop;

public class Employee {
    // Fields matching the CSV columns: ID, Name, Password, Role
    String employeeID,name,password,role;

    public Employee(String employeeID, String name, String password, String role) {
        this.employeeID = employeeID;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    public String getEmployeeID() { return this.employeeID; }
    public String getName() { return this.name; }
    public String getPassword() { return this.password; }
    public String getRole() { return this.role; }

    public void setName(String name) { this.name = name; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(String role) { this.role = role; }

    public String toCSV() {
        return employeeID + "," + name + "," + password + "," + role;
    }

    public String toString() {
        return "ID: " + employeeID + " | Name: " + name + " (" + role + ")";
    }
}

