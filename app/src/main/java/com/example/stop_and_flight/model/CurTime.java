package com.example.stop_and_flight.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CurTime {
    private String Year;
    private String Month;
    private String Day;
    private String Hour;
    private String Minute;

    public CurTime()
    {
        long now = System.currentTimeMillis();
        Date date = new Date(now);

        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        Year = CurYearFormat.format(date);
        Month = CurMonthFormat.format(date);
        Day = CurDayFormat.format(date);
        Hour = CurHourFormat.format(date);
        Minute = CurMinuteFormat.format(date);
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String day) {
        Day = day;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }

    public String getMinute() {
        return Minute;
    }

    public void setMinute(String minute) {
        Minute = minute;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getIntMonth() {
        return Integer.parseInt(Month);
    }

    public int getIntDay() {
        return Integer.parseInt(Day);
    }

    public int getIntHour() {
        return Integer.parseInt(Hour);
    }

    public int getIntMinute() {
        return Integer.parseInt(Minute);
    }

    public int getIntYear() {
        return Integer.parseInt(Year);
    }
}
