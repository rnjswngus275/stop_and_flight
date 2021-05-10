package com.example.stop_and_flight;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Goal {
    public String goal;
    public int  goal_id;
    public Map<String, Boolean> stars = new HashMap<>();

    public Goal() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Goal(String goal, int goal_id) {
        this.goal = goal;
        this.goal_id = goal_id;

    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("goal", goal);
        result.put("goal_id", goal_id);
        result.put("stars", stars);

        return result;
    }
}
