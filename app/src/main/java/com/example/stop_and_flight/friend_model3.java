package com.example.stop_and_flight;

public class friend_model3 {
    private String date;
    private String arrive;
    private String depart;
    private String nickname;
    private String UID;
    private String todo;
    private int num;

    public friend_model3(){
        this.date=null;
        this.nickname=null;
        this.UID=null;
        this.arrive=null;
        this.depart=null;
        this.todo=null;
        this.num=0;
    }

    public friend_model3(String nickname,String UID,String date,String arrive,String depart,String todo,int num){
        this.date=date;
        this.nickname=nickname;
        this.UID=UID;
        this.depart=depart;
        this.arrive=arrive;
        this.todo=todo;
        this.num=num;
    }

    public String getDate(){return date;}
    public String getNickname() {
        return nickname;
    }
    public String getUID() {
        return UID;
    }
    public String getArrive() {
        return arrive;
    }
    public String getDepart() {
        return depart;
    }
    public String getTodo() { return todo; }
    public int getCount(){return num;}

    public void setUID(String UID) {
        this.UID = UID;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public void setDate(String date) {this.date = date;}
    public void setArrive(String arrive) {
        this.arrive = arrive;
    }
    public void setDepart(String depart) {
        this.depart = depart;
    }
    public void setTodo(String todo) {
        this.todo = todo;
    }
    public void setCount(int num ){this.num=num;}


}
