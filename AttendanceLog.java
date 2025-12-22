package fop;

public class AttendanceLog {
    String inputdate,inputtime,inputemployeeID,inputaction;

    public AttendanceLog(String date, String time, String employeeID, String action) {
        inputdate = date;
        inputtime = time;
        inputemployeeID = employeeID;
        inputaction = action;
    }

    public String getDate() { return inputdate; }
    public String getTime() { return inputtime; }
    public String getEmployeeID() { return inputemployeeID; }
    public String getAction() { return inputaction; }

    public String toCSV() {
        return inputdate + "," + inputtime + "," + inputemployeeID + "," + inputaction;
    }

    public String toString() {
        return "[" + inputdate + " " + inputtime + "] " + inputemployeeID + " - " + inputaction;
    }
}