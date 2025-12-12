public class Employee {
    private final String id;
    private final String name;
    private final String role;
    private final String password;
    private final String outletCode;

    public Employee(String id, String name, String role, String password, String outletCode) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.password = password;
        this.outletCode = outletCode;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }

    public String getOutletCode() {
        return outletCode;
    }
}
