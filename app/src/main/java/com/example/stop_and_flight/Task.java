package com.example.stop_and_flight;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Task {
    private int viewType;
    private int parent_id;
    private int id;
    private String task;
    private int todo_size;
    public ArrayList<Task> todo;
    public Map<String, Boolean> stars = new HashMap<>();

    public ArrayList<Task> getTodo() {
        return todo;
    }

    public void setTodo(Map<String, Task> todo) {
        this.todo = new ArrayList<>();
        for(Task o: todo.values()){
            this.todo.add(o);
        }
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        viewType = viewType;
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
    }

    public Task(int type, int parent_id, int id, String task) {
        this.parent_id = parent_id;
        this.viewType = type;
        this.id = id;
        this.task = task;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("viewType", viewType);
        result.put("parent_id", parent_id);
        result.put("id", id);
        result.put("task", task);
        result.put("stars", stars);

        return result;
    }
}
