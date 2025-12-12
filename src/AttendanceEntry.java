public class AttendanceEntry {
    private final String employeeId;
    private final String date;
    private final String time;
    private final String status;

    public AttendanceEntry(String employeeId, String date, String time, String status) {
        this.employeeId = employeeId;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }
}
