package com.example.stop_and_flight.lee.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.stop_and_flight.lee.DialogCloseListener;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.lee.TaskDatabaseHandler;
import com.example.stop_and_flight.lee.TodoDatabaseHandler;
import com.example.stop_and_flight.lee.model.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ActionBottomDialog";
    public static int id = 1;
    private static String UID;
    private static int Task_size;
    private static int Todo_size;
    private static int Type;
    private EditText newTaskText;
    private Button newTaskSaveButton;
    private DatabaseReference mDatabase;
    private TaskDatabaseHandler db;
    private TodoDatabaseHandler tododb;

    public static AddNewTask newInstance(String uid, int type, int task_size, int todo_size){
        UID = uid;
        Type = type;
        Task_size = task_size;
        Todo_size = todo_size;
        return new AddNewTask();
    }

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        if (getArguments() != null) {
            UID = getArguments().getString("UID", "0");
            Task_size = getArguments().getInt("Task_size", 0);
            Todo_size = getArguments().getInt("Todo_size", 0);
        }
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle SavedInstanceState){
        View v = inflater.inflate(R.layout.new_task, container, false);
        // dialog 형태의 task 입력창 출력
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle SavedInstanceState){
        super.onViewCreated(view, SavedInstanceState);
        newTaskText = Objects.requireNonNull(getView()).findViewById(R.id.newTaskText);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        db = new TaskDatabaseHandler(mDatabase);
        tododb = new TodoDatabaseHandler(mDatabase);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newTaskText.setText(task);
            if(task.length() > 0)
                newTaskSaveButton.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.colorPrimaryDark));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setTextColor(Color.GRAY);
                }
                else
                {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newTaskText.getText().toString();
                if(finalIsUpdate){
                    if (bundle.getInt("type") == 0)
                        db.update_TaskDB(bundle.getString("UID"), bundle.getInt("type"), text, bundle.getInt("id"));
                    else
                        tododb.update_todoDB(bundle.getString("UID"), bundle.getInt("type"), text, bundle.getInt("id"), bundle.getInt("parent_id"));
                }
                else{
                    if (Type == 0) {
                        Task task = new Task(Type, 0, Task_size + 1, text);
                        db.insert_taskDB(UID, task);
                    } else {
                        Task todo = new Task(Type, Task_size, Todo_size + 1, text);
                        tododb.insert_todoDB(UID, todo, Task_size);
                    }
                }
                dismiss();
            }
        });
    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
