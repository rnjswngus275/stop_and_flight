package com.example.stop_and_flight.utils;

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

import com.example.stop_and_flight.fragments.AddNewTask;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.Task;

import java.util.ArrayList;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    public static final int WINNER = 0;
    public static final int FIVE = 1;
    public static final int TEN = 2;
    public List<Task> taskList;
    private Context context;

    public RankingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType) {
            case WINNER:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank1_layout, parent, false);
                ViewHolder header = new ViewHolder(itemView);
                return header;
            case FIVE:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank2_layout, parent, false);
                ViewHolder five_header = new ViewHolder(itemView);
                return five_header;
            case TEN:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank3_layout, parent, false);
                ViewHolder ten_header = new ViewHolder(itemView);
                return ten_header;
        }
        return null;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final Task item = taskList.get(position);
        switch (item.getViewType()) {
            case WINNER:
                break;
            case FIVE:
                break;
            case TEN:
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

    public void setRank(List<Task> taskList){
        this.taskList = taskList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return context;
    }

    public void deleteItem(int position){
    }

    public void editItem(int position){
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ViewHolder(View view){
            super(view);

        }

    }

}
