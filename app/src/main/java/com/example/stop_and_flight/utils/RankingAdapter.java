package com.example.stop_and_flight.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.DateInfo;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    public static final int WINNER = 0;
    public static final int SIX = 1;
    public List<DateInfo> DateInfo;
    private Context context;

    public RankingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType / 3) {
            case WINNER:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank1_layout, parent, false);
                ViewHolder header = new ViewHolder(itemView);
                return header;
            case SIX:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank2_layout, parent, false);
                ViewHolder five_header = new ViewHolder(itemView);
                return five_header;
            default:
                itemView = inflater.from(parent.getContext()).inflate(R.layout.rank3_layout, parent, false);
                ViewHolder ten_header = new ViewHolder(itemView);
                return ten_header;
        }
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final DateInfo item = DateInfo.get(position);
        holder.NicknameTitle.setText(item.getNickname());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public  int getItemCount(){
        return  DateInfo.size();
    }

    public void setRank(List<DateInfo> DateInfo){
        this.DateInfo = DateInfo;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView NicknameTitle;
        ViewHolder(View view){
            super(view);
            NicknameTitle = view.findViewById(R.id.Ranking_winner);
        }

    }

}
