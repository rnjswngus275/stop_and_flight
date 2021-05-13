package com.example.stop_and_flight;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class TaskDatabaseHandler {
    private DatabaseReference mDatabase;
    public ArrayList taskList = new ArrayList<>();
    private static final String TAG = "ReadAndWriteSnippets";


    public TaskDatabaseHandler(DatabaseReference database){
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void insert_task(String uid, Task task){
        mDatabase.child("TASK").child(uid).child(Integer.toString(task.getId())).setValue(task);
    }

    public void update_TaskDB(String uid, int type, String text, int id) {
        String key = mDatabase.child("TASK").child(uid).child(Integer.toString(id)).getKey();
        Task task = new Task(type, text, id);

        Map<String, Object> Values = task.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/TASK/" + uid + "/" + key, Values);
        mDatabase.updateChildren(childUpdates);
    }

    public void delete_TaskDB(String uid, int id)
    {
        mDatabase.child("TASK").child(uid).child(Integer.toString(id)).removeValue();
    }
}
