package com.example.stop_and_flight.model;

public class UserInfo {
    private String nickname;
    private int todayTime;
    private int weekTime;

    public UserInfo(String nickname, int todayTime, int weekTime) {
        this.nickname = nickname;
        this.todayTime = todayTime;
        this.weekTime = weekTime;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getTodayTime() {
        return todayTime;
    }

    public void setTodayTime(int todayTime) {
        this.todayTime = todayTime;
    }

    public int getWeekTime() {
        return weekTime;
    }

    public void setWeekTime(int weekTime) {
        this.weekTime = weekTime;
    }

}
