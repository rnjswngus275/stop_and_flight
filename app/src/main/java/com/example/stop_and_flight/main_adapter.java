package com.example.stop_and_flight;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.stop_and_flight.model.Ticket;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class main_adapter extends PagerAdapter {
    private List<Ticket> models;
    private List<main_model>models2;
    private LayoutInflater layoutInflater;
    private Context context;


    public main_adapter(List<Ticket> models, Context context) {
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

        TextView dpt_time,arr_time,todo;

        dpt_time = view.findViewById(R.id.dpt_time);
        arr_time=view.findViewById(R.id.arr_time);
        todo=view.findViewById(R.id.todo);


        dpt_time.setText(models.get(position).getDepart_time());
        arr_time.setText(models.get(position).getArrive_time());
        todo.setText(models.get(position).getTodo());


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(position==0) {
//                    Intent intent = new Intent(context, TargetActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//
//                }
//                else if(position==1){
//                    Intent intent = new Intent(context, HomeActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
//                else if(position==2){
//                    Intent intent = new Intent(context, MartActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
//                else if(position==3){
//                    Intent intent = new Intent(context, CarActivity.class);
//                    //intent.putExtra("param", models.get(position).getTitle());
//                    context.startActivity(intent);
//                }
            }

        });

        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
