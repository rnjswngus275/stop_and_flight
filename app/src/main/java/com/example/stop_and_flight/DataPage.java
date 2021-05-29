package com.example.stop_and_flight;

public class DataPage {
    int time;
    String date;
    String stamp;

    public DataPage(int time, String date){
        this.time = time;
        this.date = date;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTime(int time) {
        this.time = time;
    }
}