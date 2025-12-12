import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceService {
    private final Path file = Paths.get("attendance.csv");
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Map<String, LocalTime> clockInTimes = new HashMap<>();

    public AttendanceService() {
        ensureHeader();
    }

    private void ensureHeader() {
        if (Files.exists(file)) {
            return;
        }
        try (BufferedWriter bw = Files.newBufferedWriter(file)) {
            bw.write("EmployeeID,Date,Time,Status");
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Failed to create attendance.csv: " + e.getMessage());
        }
    }

    public void mark(Employee emp, String status, AuthService auth) {
        String date = LocalDate.now().format(dateFmt);
        LocalTime now = LocalTime.now();
        
        // Format time as "09:58 a.m." or "06:05 p.m."
        int hour = now.getHour();
        int minute = now.getMinute();
        String period = hour < 12 ? "a.m." : "p.m.";
        int displayHour = hour == 0 ? 12 : (hour > 12 ? hour - 12 : hour);
        String timeFormatted = String.format("%02d:%02d %s", displayHour, minute, period);

        System.out.println("\n=== Attendance Clock " + status + " ===");
        System.out.println();
        System.out.println("Employee ID: " + emp.getId());
        System.out.println();
        System.out.println("Name: " + emp.getName());
        System.out.println();
        String outletName = auth.getOutletName(emp.getOutletCode());
        System.out.println("Outlet: " + emp.getOutletCode() + " (" + outletName + ")");
        System.out.println();
        System.out.println();

        if ("IN".equals(status)) {
            clockInTimes.put(emp.getId(), now);
            System.out.println("Clock In Successful!");
        } else if ("OUT".equals(status)) {
            System.out.println("Clock Out Successful!");
        }
        
        System.out.println();
        System.out.println("Date: " + date);
        System.out.println();
        System.out.println("Time: " + timeFormatted);
        
        if ("OUT".equals(status)) {
            LocalTime inTime = clockInTimes.get(emp.getId());
            // If not in memory, check last record from CSV
            if (inTime == null) {
                inTime = getLastClockInTime(emp.getId());
            }
            if (inTime != null) {
                Duration duration = Duration.between(inTime, now);
                double hours = duration.toMinutes() / 60.0;
                System.out.println();
                System.out.println("Total Hours Worked: " + String.format("%.1f", hours) + " hours");
                clockInTimes.remove(emp.getId());
            }
        }

        // Store in CSV with 24hr format for data consistency
        String time24hr = now.format(timeFmt);
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardOpenOption.APPEND)) {
            bw.write(String.join(",", emp.getId(), date, time24hr, status));
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Failed to append attendance: " + e.getMessage());
        }
    }

    public List<AttendanceEntry> readAll() {
        List<AttendanceEntry> list = new ArrayList<>();
        if (!Files.exists(file)) {
            return list;
        }
        try (BufferedReader br = Files.newBufferedReader(file)) {
            br.readLine();
            for (String line; (line = br.readLine()) != null; ) {
                String[] t = line.split(",", -1);
                if (t.length >= 4) {
                    list.add(new AttendanceEntry(t[0], t[1], t[2], t[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to read attendance: " + e.getMessage());
        }
        return list;
    }

    private LocalTime getLastClockInTime(String employeeId) {
        List<AttendanceEntry> entries = readAll();
        // Search backwards for last IN record for this employee
        for (int i = entries.size() - 1; i >= 0; i--) {
            AttendanceEntry entry = entries.get(i);
            if (entry.getEmployeeId().equals(employeeId)) {
                if ("IN".equals(entry.getStatus())) {
                    try {
                        return LocalTime.parse(entry.getTime(), timeFmt);
                    } catch (Exception e) {
                        return null;
                    }
                } else {
                    // Already clocked out, no matching IN
                    return null;
                }
            }
        }
        return null;
    }
}
