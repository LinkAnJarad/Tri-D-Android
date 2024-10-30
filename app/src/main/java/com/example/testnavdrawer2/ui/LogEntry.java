package com.example.testnavdrawer2.ui;

public class LogEntry {
    private String date;
    private String time_in;
    private String time_out;

    public LogEntry(String date, String time_in, String time_out) {
        this.date = date;
        this.time_in = time_in;
        this.time_out = time_out;
    }

    // Getters
    public String getDate() { return date; }
    public String getTimeIn() { return time_in; }
    public String getTimeOut() { return time_out; }
}
