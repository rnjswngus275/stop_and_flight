package com.example.stop_and_flight.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.stop_and_flight.fragments.TicketingBottomSheetDialog;
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
    public static AlarmManager mAlarmMgr=null;
    public static PendingIntent mAlarmIntent=null;

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
        ViewHolder viewHolder = new ViewHolder(itemView, viewType);
        return viewHolder;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final Ticket item = TicketList.get(position);
        holder.refferalItem = item;
        holder.CountryTitle.setText(item.getTodo());
        holder.DepartTitle.setText(formatAmPm(item.getDepart_time()));
        holder.ArriveTitle.setText(formatAmPm(item.getArrive_time()));
        String[] depart_time =  item.getDepart_time().split(":");

        String[] setDay = item.getDate().split("-");
        CurTime curTime = new CurTime();
        if (curTime.getIntYear() > Integer.parseInt(setDay[0]) || (curTime.getIntYear() == Integer.parseInt((setDay[0])) && curTime.getIntMonth() > Integer.parseInt(setDay[1])) ||
                (curTime.getIntYear() == Integer.parseInt((setDay[0])) && curTime.getIntMonth() == Integer.parseInt(setDay[1]) && curTime.getIntDay() >= Integer.parseInt(setDay[2])))
        {
            if (item.getSuccess() == 1 || curTime.getIntHour() > Integer.parseInt(depart_time[0]) || (curTime.getIntHour() == Integer.parseInt(depart_time[0]) && curTime.getIntMinute() > Integer.parseInt(depart_time[1])))
            {
                holder.mTimelineView.setMarker(context.getDrawable(R.drawable.ic_baseline_keyboard_arrow_down_24), context.getColor(R.color.color_4));
                holder.mTimelineView.setEndLineColor(context.getColor(R.color.color_3), getItemViewType(position));
                holder.mTimelineView.setStartLineColor(context.getColor(R.color.color_3), getItemViewType(position));
                holder.ticketView.setBackgroundBeforeDivider(context.getDrawable(R.color.brickred));
                holder.ticketView.setBackgroundAfterDivider(context.getDrawable(R.color.brickred));
            }
            if (item.getSuccess() == 2)
            {
                holder.stampImage.setImageResource(R.drawable.stamp);
                holder.ticketView.setBackgroundBeforeDivider(context.getDrawable(R.color.color_2));
                holder.ticketView.setBackgroundAfterDivider(context.getDrawable(R.color.color_2));
                setStarPoint(holder, item);
                holder.review.setText(item.getMemo());
            }
        }
    }

    private void setStarPoint(ViewHolder holder, Ticket item) {
        float deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
        float deviceHeight = context.getResources().getDisplayMetrics().heightPixels;
        Bitmap ori_img = BitmapFactory.decodeResource(context.getResources(), R.drawable.starpoint2);

        float bitmapWidth = ori_img.getWidth();
        float bitmapHeight = ori_img.getHeight();

        float scaleHeight = deviceWidth * bitmapHeight  / bitmapWidth;
        float scaleWidth = deviceHeight * bitmapWidth  / bitmapHeight;

        Bitmap resizeBp = Bitmap.createScaledBitmap(ori_img, (int)deviceWidth, (int)scaleHeight, true);

        Bitmap bm1 = null;
//        bm1 = Bitmap.createBitmap(resizeBp, 0, 0, ori_img.getWidth(), ori_img.getHeight());
        switch ((int)item.getRating())
        {
            case 0:
                bm1 = Bitmap.createBitmap(resizeBp, 0, 0, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 1:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 2:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*2/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 3:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*3/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 4:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*4/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 5:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*5/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 6:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*6/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 7:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*7/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 8:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*8/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 9:
                bm1 = Bitmap.createBitmap(resizeBp, 0, (resizeBp.getHeight())*9/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
            case 10:
                bm1 = Bitmap.createBitmap(resizeBp, 0, resizeBp.getHeight()*10/11, resizeBp.getWidth(), resizeBp.getHeight()/11);
                break;
        }
        holder.starImage.setImageBitmap(bm1);
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
        int requestcode = item.getRequestcode();
        cancelAlarmManager(requestcode);
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
        bundle.putString("Date", item.getDate());
        BottomSheetDialogFragment ticketingBottomSheetDialog = new TicketingBottomSheetDialog(context);
        ticketingBottomSheetDialog.setArguments(bundle);
        ticketingBottomSheetDialog.show(fragmentManager, ticketingBottomSheetDialog.getTag());
    }


    public void cancelAlarmManager(int requestcode){
        if(mAlarmIntent != null) {
            mAlarmMgr = (AlarmManager) getContext().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(getContext().getApplicationContext(), CalenderAdapter.class);
            mAlarmIntent = PendingIntent.getBroadcast(getContext().getApplicationContext(),requestcode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            mAlarmMgr.cancel(mAlarmIntent);
            mAlarmIntent.cancel();
            mAlarmMgr = null;
            mAlarmIntent = null;
        }}

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView CountryTitle;
        public TextView DepartTitle;
        public TextView ArriveTitle;
        public TicketView ticketView;
        public ImageView stampImage;
        public ImageView starImage;
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
            starImage = view.findViewById(R.id.starImage);
            mTimelineView = (TimelineView) itemView.findViewById(R.id.timeline);
            mTimelineView.initLine(viewType);
        }

    }

}
