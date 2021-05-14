package com.example.stop_and_flight;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    public static final int HEADER = 0;
    public static final int CHILD = 1;

    private List<Task> taskList;
    private Context context;
    private TaskDatabaseHandler db;
    private String  UID;

    public TaskAdapter(TaskDatabaseHandler db, Context context, String UID) {
        this.db = db;
        this.context = context;
        this.UID = UID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        Context context = parent.getContext();
//        float dp = context.getResources().getDisplayMetrics().density;
//
//        int subItemPaddingLeft = (int) (18 * dp);
//        int subItemPaddingTopAndBottom = (int) (5 * dp);
//
//        switch (viewType) {
//            case HEADER:
//                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
//                ViewHolder header = new ViewHolder(itemView);
//                return header;
//            case CHILD:
//                TextView itemTextView = new TextView(context);
//                itemTextView.setPadding(subItemPaddingLeft, subItemPaddingTopAndBottom, 0, subItemPaddingTopAndBottom);
//                itemTextView.setTextColor(0x88000000);
//                itemTextView.setLayoutParams(
//                        new ViewGroup.LayoutParams(
//                                ViewGroup.LayoutParams.MATCH_PARENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT));
//                return new ViewHolder(itemTextView) {
//                };
//        }
        return new ViewHolder(itemView);
    }

    public  void onBindViewHolder(ViewHolder holder, int position){
        final Task item = taskList.get(position);
        holder.task.setText(item.getTask());
    }

    public  int getItemCount(){
        return  taskList.size();
    }

    public void setTasks(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return context;
    }

    public void deleteItem(int position){
        Task item = taskList.get(position);
        db.delete_TaskDB(UID, item);
        taskList.remove(position);
        notifyDataSetChanged();
    }


    public void editItem(int position){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Task item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        bundle.putInt("type", item.getType());
        bundle.putString("UID", UID);
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(manager, AddNewTask.TAG);
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView task;
        public ImageView btn_expand_toggle;

        ViewHolder(View view){
            super(view);
            task = view.findViewById(R.id.taskRecyclerText);
        }
    }

}
