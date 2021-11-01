package com.example.stop_and_flight.model;

public class UserInfo {
    private String nickname;
    private int studytime;;

    public UserInfo(String nickname, int studytime) {
        this.nickname = nickname;
        this.studytime = studytime;
    }

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
