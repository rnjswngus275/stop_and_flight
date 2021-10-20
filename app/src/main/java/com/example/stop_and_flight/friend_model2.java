package com.example.stop_and_flight;

public class friend_model2 {
    private String nickname;
    private String message;
    private String UID;

    public friend_model2(){
        this.nickname=null;
        this.message=null;
        this.UID=null;

    }
    public friend_model2(String nickname,String message,String UID){
        this.nickname=nickname;
        this.message=message;
        this.UID=UID;
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


    public void setUID(String UID) {
        this.UID = UID;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
