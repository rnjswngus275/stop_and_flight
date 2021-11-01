package com.example.stop_and_flight.model;

public class main_model {
    private String depart_time;
    private String arrive_time;



    public main_model(String depart_time, String arrive_time) {

        this.depart_time = depart_time;
        this.arrive_time = arrive_time;

    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }


}
