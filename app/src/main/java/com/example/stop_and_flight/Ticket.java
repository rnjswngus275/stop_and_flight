package com.example.stop_and_flight;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Ticket {
    public String depart_time;
    public String arrive_time;
    public String goal;
    public int wait;
    public Map<String, Boolean> stars = new HashMap<>();

    public Ticket() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Ticket(String depart_time, String arrive_time, String goal) {
        this.depart_time = depart_time;
        this.arrive_time = arrive_time;
        this.goal = goal;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("depart_time", depart_time);
        result.put("arrive_time", arrive_time);
        result.put("goal", goal);
        result.put("stars", stars);

        return result;
    }
}
