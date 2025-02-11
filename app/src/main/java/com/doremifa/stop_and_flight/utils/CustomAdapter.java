package com.doremifa.stop_and_flight.utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.doremifa.stop_and_flight.R;
import com.doremifa.stop_and_flight.model.Dictionary;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private final ArrayList<Dictionary> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder{
        protected TextView Todo;
        protected TextView Time;
        public CustomViewHolder(View view) {
            super(view);

            this.Todo = (TextView) view.findViewById(R.id.Todo_listitem);
            this.Time = (TextView) view.findViewById(R.id.Time_listitem);

        }


    }
    public CustomAdapter(ArrayList<Dictionary> list) {
        this.mList = list;
    }
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);


        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        viewholder.Todo.setText(mList.get(position).getTodo());
        viewholder.Time.setText(String.valueOf(mList.get(position).getTime()));


    }
    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}