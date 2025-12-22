package fop;

public class Employee {
    // Fields matching the CSV columns: ID, Name, Password, Role
    String inputemployeeID,inputname,inputpassword,inputrole;

    public Employee(String employeeID, String name, String password, String role) {
        inputemployeeID = employeeID;
        inputname = name;
        inputpassword = password;
        inputrole = role;
    }

    public String getEmployeeID() { return inputemployeeID; }
    public String getName() { return inputname; }
    public String getPassword() { return inputpassword; }
    public String getRole() { return inputrole; }

    public void setName(String name) { inputname = name; }
    public void setPassword(String password) { inputpassword = password; }
    public void setRole(String role) { inputrole = role; }

    public String toCSV() {
        return inputemployeeID + "," + inputname + "," + inputpassword + "," + inputrole;
    }

    public String toString() {
        return "ID: " + inputemployeeID + " | Name: " + inputname + " (" + inputrole + ")";
    }
}

