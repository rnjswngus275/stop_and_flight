package com.example.stop_and_flight.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stop_and_flight.R;
import com.example.stop_and_flight.model.CurTime;
import com.example.stop_and_flight.model.Ticket;
import com.example.stop_and_flight.utils.CalenderAdapter;
import com.example.stop_and_flight.utils.RecyclerItemTouchHelper;
import com.example.stop_and_flight.utils.TicketDatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shrikanthravi.collapsiblecalendarview.data.Day;
import com.shrikanthravi.collapsiblecalendarview.widget.CollapsibleCalendar;

import java.util.ArrayList;
import java.util.Collections;

import static java.lang.String.valueOf;

public class CalendarFragment extends Fragment {
    private DatabaseReference mDatabase;
    private RecyclerView reviewRecyclerView;
    public ArrayList<Ticket> TicketList = new ArrayList<>();
    private String UID;
    private Ticket getTicket;
    private CalenderAdapter calenderAdapter;
    private FloatingActionButton ticketFab;
    private TicketDatabaseHandler db;
    private CurTime curTime;
    private String ticket_Date;
    private int YEAR;
    private int MONTH;
    private int DAY;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        CollapsibleCalendar collapsibleCalendar = view.findViewById(R.id.calendarView);
        Context ct = container.getContext();
        BottomSheetDialogFragment ticketingBottomSheetDialog = new TicketingBottomSheetDialog(ct);

        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        db = new TicketDatabaseHandler(mDatabase);
        calenderAdapter = new CalenderAdapter(db, ct, UID, (TicketingBottomSheetDialog) ticketingBottomSheetDialog, getFragmentManager());
        curTime = new CurTime();

        YEAR =  curTime.getIntYear();
        MONTH =  curTime.getIntMonth();
        DAY =  curTime.getIntDay();

        ticket_Date = YEAR + "-" + MONTH + "-" + DAY;

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
                ticket_Date = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
                getTicketDate(ticket_Date);
            }

            @Override
            public void onItemClick(View v) {

            }

            @Override
            public void onDataUpdate() {
            }

            @Override
            public void onMonthChange() {

            }

            @Override
            public void onWeekChange(int position) {

            }
        });

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeTicketContainer);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTicketDate(ticket_Date);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getTicketDate(ticket_Date);

        ticketFab = view.findViewById(R.id.ticketFab);
        ticketFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketingBottomSheetDialog.show(getFragmentManager(), ticketingBottomSheetDialog.getTag());
            }
        });


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(calenderAdapter));
        itemTouchHelper.attachToRecyclerView(reviewRecyclerView);

        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.VERTICAL, false));

        Collections.reverse(TicketList);
        calenderAdapter.setTicket(TicketList);
        reviewRecyclerView.setAdapter(calenderAdapter);

        return view;
    }

    private void getTicketDate(String ticket_Date)
    {
        mDatabase.child("TICKET").child(UID).child(ticket_Date).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reviewRecyclerView.removeAllViewsInLayout();
                TicketList.clear();
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        getTicket = new Ticket();
                        getTicket = fileSnapshot.getValue(Ticket.class);
                        getTicket.setDate(ticket_Date);
                        TicketList.add(getTicket);
                    }
                }
                TicketList.sort((o1, o2) -> {
                    String[] departTime1 = o1.getDepart_time().split(":");
                    String[] departTime2 = o2.getDepart_time().split(":");
                    if (Integer.parseInt(departTime1[0]) == Integer.parseInt(departTime2[0])
                            && Integer.parseInt(departTime1[1]) == Integer.parseInt(departTime2[1]))
                        return 0;
                    if (Integer.parseInt(departTime1[0]) >= Integer.parseInt(departTime2[0])
                            && Integer.parseInt(departTime1[1]) >= Integer.parseInt(departTime2[1]))
                        return 1;
                    if (Integer.parseInt(departTime1[0]) <= Integer.parseInt(departTime2[0])
                            && Integer.parseInt(departTime1[1]) <= Integer.parseInt(departTime2[1]))
                        return -1;
                    return 0;
                });
                calenderAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
    }
}