package com.doremifa.stop_and_flight;

public class friend_model {
    private String nickname;
    private String message;
    private String UID;
    private String count_idle;
    private String count_myidle;

    public friend_model(){
        this.nickname=null;
        this.message=null;
        this.UID=null;
        this.count_myidle =null;
        this.count_idle=null;

    }
    public friend_model(String nickname,String message,String UID,String count_myidle,String count_idle){
        this.nickname=nickname;
        this.message=message;
        this.UID=UID;
        this.count_myidle =count_myidle;
        this.count_idle=count_idle;

    }

    public String getNickname() {
        return nickname;
    }

    public String getMessage() {
        return message;
    }

    public String getUID() {
        return UID;
    }

    public String getCount_myidle() {
        return count_myidle;
    }

    public String getCount_idle() {
        return count_idle;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setCount_myidle(String count_myidle) {
        this.count_myidle = count_myidle;
    }

    public void setCount_idle(String count_idle) {
        this.count_idle = count_idle;
    }
}
