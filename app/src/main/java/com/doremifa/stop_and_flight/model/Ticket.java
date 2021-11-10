package com.doremifa.stop_and_flight.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private String depart_time;
    private String arrive_time;
    private String date;
    private String todo;
    private int success;
    private String memo;
    private float rating;
    private int id;
    private int requestcode;

    public Ticket() {
        this.depart_time = null;
        this.arrive_time = null;
        this.date = null;
        this.todo = null;
        this.id = 0;
        this.success = 0;
        this.requestcode = 0;
    }

    public float getRating() {
        return rating * 2;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Ticket(String depart_time, String arrive_time, String todo, int id, int Success, int requestcode) {
        this.depart_time = depart_time;
        this.arrive_time = arrive_time;
        this.todo = todo;
        this.id = id;
        this.success = Success;
        this.requestcode=0;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getRequestcode(){return requestcode;}

    public void setRequestcode(int requestcode)
    {this.requestcode=requestcode;}

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depart_time", depart_time);
        result.put("arrive_time", arrive_time);
        result.put("id", id);
        result.put("success", success);
        result.put("requestcode",requestcode);
        result.put("todo", todo);
        result.put("memo", memo);
        result.put("rating", rating);

        return result;
    }
}
