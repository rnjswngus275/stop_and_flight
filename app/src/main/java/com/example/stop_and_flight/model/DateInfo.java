package com.example.stop_and_flight.model;

public class DateInfo {
    private String nickname;
    private int studytime;
    private String today;


    public DateInfo(String nickname, int studytime, String today) {
        this.nickname = nickname;
        this.studytime = studytime;
        this.today=today;
    }

    public DateInfo(){}

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getStudytime() {
        return studytime;
    }

    public void setStudytime(int studytime) {
        this.studytime = studytime;
    }


}

