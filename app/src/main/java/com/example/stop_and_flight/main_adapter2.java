package com.example.stop_and_flight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.stop_and_flight.model.main_model;

public class main_adapter2 extends PagerAdapter {
    private final List<main_model> models;
    private LayoutInflater layoutInflater;
    private final Context context;


    public main_adapter2(List<main_model> models, Context context) {
        this.models = models;
        this.context = context;
    }


    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.main_item, container, false);

        TextView dpt_time,arr_time,todo, txt1,txt2,txt3;

        dpt_time = view.findViewById(R.id.dpt_time);
        arr_time=view.findViewById(R.id.arr_time);
        txt1=view.findViewById(R.id.txt1);
        txt2=view.findViewById(R.id.txt2);
        txt3=view.findViewById(R.id.txt3);
        todo=view.findViewById(R.id.todo);


        txt1.setText(models.get(position).getDepart_time());
        txt2.setText(models.get(position).getArrive_time());
        dpt_time.setText("");
        arr_time.setText("");
        txt3.setText("");


        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
