package com.example.stop_and_flight.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    public static final int WINNER = 0;
    public static final int TWO = 1;
    public static final int THREE = 2;
    public static final int FOUR = 3;
    public static final int FIVE = 4;
    public static final int SIX = 1;
    public List<UserInfo> userInfo;
    private final Context context;

    public RankingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType / 5) {
            case WINNER:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank1_layout, parent, false);
                break;
            case SIX:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank2_layout, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank3_layout, parent, false);

                break;
        }
        ViewHolder header = new ViewHolder(itemView);
        return header;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final UserInfo item = userInfo.get(position);
        holder.NicknameTitle.setText(item.getNickname());
        holder.timeTitle.setText(item.getStudytime() + " 분");

        switch (position)
        {
            case WINNER:
                holder.WinnerImage.setImageResource(R.drawable.icon_155530_256);
                break;
            case TWO:
                holder.WinnerImage.setImageResource(R.drawable.icon_155540_256);
                break;
            case THREE:
                holder.WinnerImage.setImageResource(R.drawable.icon_155550_256);
                break;
            case FOUR:
                holder.WinnerImage.setImageResource(R.drawable.icon_155560_256);
                break;
            case FIVE:
                holder.WinnerImage.setImageResource(R.drawable.icon_155570_256);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public  int getItemCount(){
        return  userInfo.size();
    }

    public void setRank(ArrayList<UserInfo> userInfo){
        this.userInfo = userInfo;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView NicknameTitle;
        public TextView timeTitle;
        public ImageView WinnerImage;
        ViewHolder(View view){
            super(view);
            NicknameTitle = view.findViewById(R.id.Ranking_winner);
            timeTitle = view.findViewById(R.id.RankingTime);
            WinnerImage = view.findViewById(R.id.Ranking_image);
        }

    }

}