package com.example.stop_and_flight;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stop_and_flight.fragments.TicketingFragment;
import com.example.stop_and_flight.model.Ticket;
import com.vipulasri.ticketview.TicketView;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.ViewHolder> {

    private Context context;
    private List<Ticket> TicketList;
    private TicketDatabaseHandler db;
    private String  UID;

    public TicketAdapter(TicketDatabaseHandler db, Context context, String UID) {
        this.db = db;
        this.context = context;
        this.UID = UID;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.from(parent.getContext()).inflate(R.layout.ticket_layout, parent, false);
        ViewHolder header = new ViewHolder(itemView);
        return header;
    }


    public  void onBindViewHolder(ViewHolder holder, int position){
        final Ticket item = TicketList.get(position);
        holder.refferalItem = item;
        holder.CountryTitle.setText(item.getTodo());
        holder.DepartTitle.setText(item.getDepart_time());
        holder.ArriveTitle.setText(item.getArrive_time());
        if (item.getWait().equals("false"))
        {
            holder.ticketView.setBackgroundBeforeDivider(Drawable.createFromPath("@color/shadow"));
            holder.ticketView.setBackgroundAfterDivider(Drawable.createFromPath("@color/shadow"));
        }
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


    public void editItem(int position){
        Ticket item = TicketList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("Id", item.getId());
        bundle.putString("Todo", item.getTodo());
        bundle.putString("Depart_time", item.getDepart_time());
        bundle.putString("Arrive_time", item.getArrive_time());
        ((MainActivity) getActivity()).replaceFragment(TicketingFragment.newInstance("update", null, bundle));

    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView CountryTitle;
        public TextView DepartTitle;
        public TextView ArriveTitle;
        public TextView user_name;
        public TextView user_class;
        public ImageView user_image;
        public TicketView ticketView;
        public Ticket refferalItem;

        ViewHolder(View view){
            super(view);
            ticketView = view.findViewById(R.id.ticketView);
            CountryTitle = view.findViewById(R.id.CountryTitle);
            DepartTitle = view.findViewById(R.id.DepartTitle);
            ArriveTitle = view.findViewById(R.id.ArriveTitle);
            user_name = view.findViewById(R.id.user_name);
            user_class = view.findViewById(R.id.user_class);
            user_image = view.findViewById(R.id.user_image);
        }

    }

}
