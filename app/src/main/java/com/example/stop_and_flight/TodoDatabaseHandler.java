package com.example.stop_and_flight;

import com.example.stop_and_flight.model.Task;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class TodoDatabaseHandler {
    private DatabaseReference mDatabase;

    public TodoDatabaseHandler(DatabaseReference mdatabase){
        mDatabase = mdatabase;
    }

    public void insert_todoDB(String uid, Task todo, int task_id){
        mDatabase.child("TASK").child(uid).child(Integer.toString(task_id)).child("todo").child(Integer.toString(todo.getId())).setValue(todo);
    }

    public void update_todoDB(String uid, int type, String text, int todo_id, int task_id) {
        String key = mDatabase.child("TASK").child(uid).child(Integer.toString(task_id)).child("todo").child(Integer.toString(todo_id)).getKey();
        Task task = new Task(type, task_id,todo_id, text);

        Map<String, Object> Values = task.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/TASK/" + uid + "/" + task_id + "/todo/" + key, Values);
        mDatabase.updateChildren(childUpdates);
    }

    public void delete_todoDB(String uid, Task todo, int task_id)
    {
        mDatabase.child("TASK").child(uid).child(Integer.toString(task_id)).child("todo").child(Integer.toString(todo.getId())).removeValue();
    }
}
