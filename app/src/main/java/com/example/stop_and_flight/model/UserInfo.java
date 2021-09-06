package com.example.stop_and_flight.model;

public class UserInfo {
    private String Nickname;
    private int Weektime;
    private int todaytime;

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public int getWeektime() {
        return Weektime;
    }

    public void setWeektime(int weektime) {
        Weektime = weektime;
    }

    public int getTodaytime() {
        return todaytime;
    }

    public void setTodaytime(int todaytime) {
        this.todaytime = todaytime;
    }
}
