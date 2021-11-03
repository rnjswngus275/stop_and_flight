package com.example.stop_and_flight.model;

public class Dictionary {
    private String Todo;
    private String Time;

    public String getTodo() {
        return Todo;
    }

    public void setTodo(String todo) {
        Todo = todo;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Dictionary(String todo, String time) {

        Todo = todo;
        Time = time;
    }
}