import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private final Path employeeFile = Paths.get("employee.csv");
    private final Path outletFile = Paths.get("outlet.csv");
    private final Map<String, Employee> employees = new HashMap<>();
    private final Map<String, String> outlets = new HashMap<>();
    private Employee currentUser;

    public AuthService() {
        loadOutlets();
        load();
    }

    private void loadOutlets() {
        outlets.clear();
        if (!Files.exists(outletFile)) return;
        try (BufferedReader br = Files.newBufferedReader(outletFile)) {
            br.readLine();
            for (String line; (line = br.readLine()) != null; ) {
                String[] t = line.split(",", -1);
                if (t.length >= 2) {
                    outlets.put(t[0], t[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read outlet.csv: " + e.getMessage());
        }
    }

    private void load() {
        employees.clear();
        if (!Files.exists(employeeFile)) {
            System.err.println("employee.csv not found; no users loaded.");
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(employeeFile)) {
            br.readLine(); // header
            for (String line; (line = br.readLine()) != null; ) {
                String[] t = line.split(",", -1);
                if (t.length >= 4) {
                    String outletCode = t.length >= 5 ? t[4] : "C60";
                    employees.put(t[0], new Employee(t[0], t[1], t[2], t[3], outletCode));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read employee.csv: " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter bw = Files.newBufferedWriter(employeeFile)) {
            bw.write("EmployeeID,EmployeeName,Role,Password,OutletCode");
            bw.newLine();
            for (Employee e : employees.values()) {
                bw.write(String.join(",", e.getId(), e.getName(), e.getRole(), e.getPassword(), e.getOutletCode()));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to write employee.csv: " + e.getMessage());
        }
    }

    public boolean login(String id, String password) {
        Employee e = employees.get(id);
        if (e != null && e.getPassword().equals(password)) {
            currentUser = e;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public Employee getCurrentUser() {
        return currentUser;
    }

    public boolean register(String id, String name, String role, String password, String outletCode) {
        if (id == null || id.isEmpty() || name == null || name.isEmpty() || role == null || role.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }
        if (employees.containsKey(id)) {
            return false;
        }
        employees.put(id, new Employee(id, name, role, password, outletCode));
        save();
        return true;
    }

    public Collection<Employee> listEmployees() {
        return employees.values();
    }

    public String getOutletName(String code) {
        return outlets.getOrDefault(code, code);
    }
}
