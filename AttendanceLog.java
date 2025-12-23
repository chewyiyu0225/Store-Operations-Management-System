package fop;

public class AttendanceLog {
    String date,time,employeeID,action;

    public AttendanceLog(String date, String time, String employeeID, String action) {
        this.date = date;
        this.time = time;
        this.employeeID = employeeID;
        this.action = action;
    }

    public String getDate() { return date; }
    public String getTime() { return time; }
    public String getEmployeeID() { return employeeID; }
    public String getAction() { return action; }

    public String toCSV() {
        return date + "," + time + "," + employeeID + "," + action;
    }

    public String toString() {
        return "[" + date + " " + time + "] " + employeeID + " - " + action;
    }
}
