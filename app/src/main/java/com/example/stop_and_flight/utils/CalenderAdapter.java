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

import com.example.stop_and_flight.Fragment.TicketingBottomSheetDialog;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.CurTime;
import com.example.stop_and_flight.model.Ticket;
import com.github.vipulasri.timelineview.TimelineView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.vipulasri.ticketview.TicketView;

import java.util.List;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> TicketList;
    private CurTime curTime;
    private TicketDatabaseHandler db;
    private String  UID;
    private TicketingBottomSheetDialog ticketingBottomSheetDialog;
    private FragmentManager fragmentManager;

    public CalenderAdapter(TicketDatabaseHandler db, Context context, String UID, TicketingBottomSheetDialog ticketingBottomSheetDialog, FragmentManager fragmentManager) {
        this.db = db;
        this.context = context;
        this.UID = UID;
        this.ticketingBottomSheetDialog = ticketingBottomSheetDialog;
        this.fragmentManager = fragmentManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.from(parent.getContext()).inflate(R.layout.ticket_layout, parent, false);
        ViewHolder header = new ViewHolder(itemView, viewType);
        curTime = new CurTime();

        System.out.println("check >>> viewtype : " + viewType);
        return header;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final Ticket item = TicketList.get(position);
        holder.refferalItem = item;
        holder.CountryTitle.setText(item.getTodo());
        holder.DepartTitle.setText(formatAmPm(item.getDepart_time()));
        holder.ArriveTitle.setText(formatAmPm(item.getArrive_time()));
        String[] depart_time =  item.getDepart_time().split(":");

        String[] setDay = item.getDate().split("-");

        if (curTime.getIntYear() > Integer.parseInt(setDay[0]) || (curTime.getIntYear() == Integer.parseInt((setDay[0])) && curTime.getIntMonth() > Integer.parseInt(setDay[1])) ||
                (curTime.getIntYear() == Integer.parseInt((setDay[0])) && curTime.getIntMonth() == Integer.parseInt(setDay[1]) && curTime.getIntDay() >= Integer.parseInt(setDay[2])))
        {
            if (curTime.getIntHour() > Integer.parseInt(depart_time[0]) || (curTime.getIntHour() == Integer.parseInt(depart_time[0]) && curTime.getIntMinute() > Integer.parseInt(depart_time[1])))
            {
                holder.mTimelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), context.getColor(R.color.color_4));
                holder.mTimelineView.setEndLineColor(context.getColor(R.color.color_3), getItemViewType(position));
                holder.mTimelineView.setStartLineColor(context.getColor(R.color.color_3), getItemViewType(position));
                holder.ticketView.setBackgroundBeforeDivider(context.getDrawable(R.color.brickred));
                holder.ticketView.setBackgroundAfterDivider(context.getDrawable(R.color.brickred));
            }
            if (item.getWait().equals("false"))
            {
                holder.stampImage.setImageResource(R.drawable.stamp);
                holder.ticketView.setBackgroundBeforeDivider(context.getDrawable(R.color.color_2));
                holder.ticketView.setBackgroundAfterDivider(context.getDrawable(R.color.color_2));
                holder.review.setText(item.getReview());
            }
        }
    }

    public String formatAmPm(String date)
    {
        String[] date_arr = date.split(":");
        String date_set;
        if (Integer.parseInt(date_arr[0]) < 10)
            date_set = "AM 0" + date_arr[0];
        else if (Integer.parseInt(date_arr[0]) < 12)
            date_set = "AM " + date_arr[0];
        else if(Integer.parseInt(date_arr[0]) % 12 < 10)
            date_set = "PM 0" + Integer.parseInt(date_arr[0]) % 12;
        else
            date_set = "PM " + Integer.parseInt(date_arr[0]) % 12;
        if (Integer.parseInt(date_arr[1]) < 10)
            date_set = date_set + ":0" + date_arr[1];
        else
            date_set = date_set +":" + date_arr[1];
        return date_set;
    }

    public  int getItemCount(){
        return  TicketList.size();
    }

    public void setTicket(List<Ticket> TicketList){
        this.TicketList = TicketList;
        notifyDataSetChanged();
    }

    public Context getContext(){
        return context;
    }

    public void deleteItem(int position){
        Ticket item = TicketList.get(position);
        db.delete_ticketDB(UID, item.getDate(), item);
        TicketList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position, getItemCount());
    }

    public void editItem(int position){
        Ticket item = TicketList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("Todo", item.getTodo());
        BottomSheetDialogFragment ticketingBottomSheetDialog = new TicketingBottomSheetDialog(context);
        ticketingBottomSheetDialog.setArguments(bundle);
        ticketingBottomSheetDialog.show(fragmentManager, ticketingBottomSheetDialog.getTag());
    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView CountryTitle;
        public TextView DepartTitle;
        public TextView ArriveTitle;
        public TicketView ticketView;
        public ImageView stampImage;
        public Ticket refferalItem;
        public TimelineView mTimelineView;
        public TextView review;


        ViewHolder(View view, int viewType){
            super(view);
            ticketView = view.findViewById(R.id.ticketView);
            CountryTitle = view.findViewById(R.id.CountryTitle);
            DepartTitle = view.findViewById(R.id.DepartTitle);
            ArriveTitle = view.findViewById(R.id.ArriveTitle);
            review = view.findViewById(R.id.review);
            stampImage = view.findViewById(R.id.stampImage);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }

    }

}
