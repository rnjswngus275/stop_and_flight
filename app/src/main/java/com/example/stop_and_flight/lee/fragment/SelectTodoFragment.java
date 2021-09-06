package com.example.stop_and_flight.lee.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.lee.TaskDatabaseHandler;
import com.example.stop_and_flight.lee.TodoDatabaseHandler;
import com.example.stop_and_flight.lee.TodoSelectAdapter;
import com.example.stop_and_flight.lee.model.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTodoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTodoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private RecyclerView selectTaskRecyclerView;
    private TodoSelectAdapter todoSelectAdapter;
    public ArrayList<Task> taskList = new ArrayList<>();;
    private static String UID;
    private DatabaseReference mDatabase;
    private Context context;
    private Task getTask;
    private Task getTodo;
    private TaskDatabaseHandler db;
    private TodoDatabaseHandler tododb;
    private HashMap<String, Object> TodoMap;
    private HashMap<String, Object> TaskMap;

    public SelectTodoFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTodoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTodoFragment newInstance(int param1, String param2, Context context) {
        SelectTodoFragment fragment = new SelectTodoFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            System.out.println(mParam1 + mParam2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_select_todo, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        db = new TaskDatabaseHandler(mDatabase);
        tododb = new TodoDatabaseHandler(mDatabase);
        selectTaskRecyclerView = v.findViewById(R.id.selectTaskRecyclerView);
        Fragment BottomSheetdialog = ((TicketingBottomSheetDialog) getParentFragment());

        todoSelectAdapter = new TodoSelectAdapter(db, tododb, context, UID, mParam1, BottomSheetdialog);
        mDatabase.child("TASK").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                selectTaskRecyclerView.removeAllViewsInLayout();
                taskList.clear();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        TaskMap = (HashMap<String, Object>) fileSnapshot.getValue();
                        System.out.println(TaskMap);
                        int type = Integer.parseInt(String.valueOf(TaskMap.get("viewType")));
                        int task_id = Integer.parseInt(String.valueOf(TaskMap.get("id")));
                        int parent_id = Integer.parseInt(String.valueOf(TaskMap.get("parent_id")));
                        getTask = new Task(type, parent_id, task_id, (String)TaskMap.get("task"));
                        taskList.add(getTask);
                        for (DataSnapshot todoSnapshot : fileSnapshot.child("todo").getChildren()) {
                            if (todoSnapshot != null){
                                TodoMap = (HashMap<String, Object>) todoSnapshot.getValue();
                                type = Integer.parseInt(String.valueOf(TodoMap.get("viewType")));
                                int todo_id = Integer.parseInt(String.valueOf(TodoMap.get("id")));
                                getTodo = new Task(type, task_id, todo_id, (String)TodoMap.get("task"));
                                taskList.add(getTodo);
                            }
                        }
                    }
                }
                todoSelectAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });

        selectTaskRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        Collections.reverse(taskList);
        todoSelectAdapter.setTasks(taskList);
        selectTaskRecyclerView.setAdapter(todoSelectAdapter);
        // Inflate the layout for this fragment
        return v;
    }
}