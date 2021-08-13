package com.example.stop_and_flight;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stop_and_flight.fragment.AddNewTask;
import com.example.stop_and_flight.fragment.TicketingBottomSheetDialog;
import com.example.stop_and_flight.fragment.TicketingFragment;
import com.example.stop_and_flight.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TodoSelectAdapter extends RecyclerView.Adapter<TodoSelectAdapter.ViewHolder> {

    public static final int HEADER = 0;
    public static final int CHILD = 1;
    private List<Task> taskList;
    private Context context;
    private TaskDatabaseHandler db;
    private TodoDatabaseHandler tododb;
    private Bundle bundle;
    private String  UID;

    public TodoSelectAdapter(TaskDatabaseHandler db, TodoDatabaseHandler tododb, Context context, String UID, Bundle bundle) {
        this.db = db;
        this.tododb = tododb;
        this.context = context;
        this.UID = UID;
        this.bundle = bundle;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case HEADER:
                System.out.println("check : header");
                itemView = inflater.from(parent.getContext()).inflate(R.layout.task_select_layout, parent, false);
                ViewHolder header = new ViewHolder(itemView);
                return header;
            case CHILD:
                System.out.println("check : child");
                itemView = inflater.from(parent.getContext()).inflate(R.layout.todo_layout, parent, false);
                ViewHolder Child_header = new ViewHolder(itemView);
                return Child_header;
        }
        return null;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final Task item = taskList.get(position);
        switch (item.getViewType()) {
            case HEADER:
                holder.refferalItem = item;
                holder.header_title.setText(item.getTask());
                holder.header_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.todo == null) {
                            item.todo = new ArrayList<>();
                            int pos = taskList.indexOf(holder.refferalItem);
                            int count = 0;
                            while (taskList.size() > pos + 1 && taskList.get(pos + 1).getViewType() == CHILD) {
                                item.todo.add(taskList.remove(pos + 1));
                                count++;
                            }
                            notifyItemRangeRemoved(pos + 1, count);
                        } else {
                            int pos = taskList.indexOf(holder.refferalItem);
                            int index = pos + 1;
                            for (Task i : item.todo) {
                                taskList.add(index, i);
                                index++;
                            }
                            notifyItemRangeInserted(pos + 1, index - pos - 1);
                            item.todo = null;
                        }
                    }
                });
                break;
            case CHILD:
                holder.sub_title.setText(taskList.get(position).getTask());
                holder.sub_title.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bundle != null)
                        {
                            bundle.putString("Todo", item.getTask());
                            ((MainActivity) getActivity()).replaceFragment(TicketingFragment.newInstance("update", null, bundle, context));
                        }
                        else
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("Todo", item.getTask());
                            ((MainActivity) getActivity()).replaceFragment(TicketingFragment.newInstance(null, null, bundle, context));
                        }
                    }
                });
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return taskList.get(position).getViewType();
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
        if (item.getViewType() == HEADER)
            db.delete_TaskDB(UID, item);
        else if (item.getViewType() == CHILD)
            tododb.delete_todoDB(UID, item, item.getParent_id());
        taskList.remove(position);
        notifyDataSetChanged();
    }


    public void editItem(int position){
        FragmentManager manager = getActivity().getSupportFragmentManager();
        Task item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putInt("parent_id", item.getParent_id());
        bundle.putString("task", item.getTask());
        bundle.putInt("type", item.getViewType());
        bundle.putString("UID", UID);
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(manager, AddNewTask.TAG);
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView header_title;
        public TextView sub_title;
        public ImageView btn_expand_toggle;
        public Task refferalItem;

        ViewHolder(View view){
            super(view);
            header_title = view.findViewById(R.id.TodoSelectRecyclerText);
            btn_expand_toggle = view.findViewById(R.id.btn_expand_toggle);
            sub_title = view.findViewById(R.id.todoRecyclerText);
        }

    }

}
