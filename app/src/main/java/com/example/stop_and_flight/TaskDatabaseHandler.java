package com.example.stop_and_flight;
import com.google.firebase.database.DatabaseReference;
import java.util.HashMap;
import java.util.Map;

public class TaskDatabaseHandler {
    private DatabaseReference mDatabase;

    public TaskDatabaseHandler(DatabaseReference mdatabase){
        mDatabase = mdatabase;
    }

    public void insert_taskDB(String uid, Task task){
        mDatabase.child("TASK").child(uid).child(Integer.toString(task.getId())).setValue(task);
    }

    public void update_TaskDB(String uid, int type, String text, int id) {
        String key = mDatabase.child("TASK").child(uid).child(Integer.toString(id)).getKey();
        Task task = new Task(type, 0, id, text);

        Map<String, Object> Values = task.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/TASK/" + uid + "/" + key, Values);
        mDatabase.updateChildren(childUpdates);
    }

    public void delete_TaskDB(String uid, Task task)
    {
        mDatabase.child("TASK").child(uid).child(Integer.toString(task.getId())).removeValue();
    }
}
