package com.example.stop_and_flight;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {
    public int type;
    private int id;
    private String task;
    public List<Task> todo;
    public Map<String, Boolean> stars = new HashMap<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public Task() {
        this.type = 0;
        this.id = 1;
        this.task = null;
    }

    public Task(int type, String task, int id) {
        this.type = type;
        this.id = id;
        this.task = task;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("type", type);
        result.put("task", task);
        result.put("id", id);
        result.put("stars", stars);

        return result;
    }
}
