package com.example.stop_and_flight.model;

public class Dictionary {
    private String Todo;
    private int Time;

    public String getTodo() {
        return Todo;
    }

    public void setTodo(String todo) {
        Todo = todo;
    }

    public int getTime() {
        return Time;
    }

    public void setTime(int time) {
        Time = time;
    }

    public Dictionary(String todo, int time) {

        Todo = todo;
        Time = time;
    }
}