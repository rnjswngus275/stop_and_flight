package com.example.stop_and_flight;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    private String depart_time;
    private String arrive_time;
    private String Todo;
    private int id;
    private String wait;
    private Map<String, Boolean> stars = new HashMap<>();

    public Ticket() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public String getArrive_time() {
        return arrive_time;
    }

    public void setArrive_time(String arrive_time) {
        this.arrive_time = arrive_time;
    }

    public String getTodo() {
        return Todo;
    }

    public void setTodo(String todo) {
        Todo = todo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWait() {
        return wait;
    }

    public void setWait(String wait) {
        this.wait = wait;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public String getDepart_time() {
        return depart_time;
    }

    public void setDepart_time(String depart_time) {
        this.depart_time = depart_time;
    }

    public Ticket(String depart_time, String arrive_time, String goal, int id, String wait) {
        this.depart_time = depart_time;
        this.arrive_time = arrive_time;
        this.Todo = goal;
        this.id = id;
        this.wait = wait;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depart_time", depart_time);
        result.put("arrive_time", arrive_time);
        result.put("id", id);
        result.put("wait", wait);
        result.put("Todo", Todo);
        result.put("stars", stars);

        return result;
    }
}
