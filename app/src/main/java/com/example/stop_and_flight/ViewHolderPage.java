package com.example.stop_and_flight;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPage extends RecyclerView.ViewHolder {

    private TextView tv_title;
    private LinearLayout rl_layout;

    DataPage data;

    ViewHolderPage(View itemView) {
        super(itemView);
        tv_title = itemView.findViewById(R.id.tv_title);
        rl_layout = itemView.findViewById(R.id.rl_layout);
    }

    public void onBind(DataPage data){
        this.data = data;

        tv_title.setText(data.getDate());
        //rl_layout.setBackgroundResource(data.getColor());
    }
}