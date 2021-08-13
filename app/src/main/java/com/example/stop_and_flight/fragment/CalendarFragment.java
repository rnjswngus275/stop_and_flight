package com.example.stop_and_flight.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.stop_and_flight.CalenderAdapter;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.TicketDatabaseHandler;
import com.example.stop_and_flight.fragment.AddNewTask;
import com.example.stop_and_flight.model.CalendarModel;
import com.example.stop_and_flight.model.CurTime;
import com.example.stop_and_flight.model.Ticket;
import com.example.stop_and_flight.singleton.CalendarSingle;
import com.example.stop_and_flight.singleton.Schedule;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
    private int YEAR;
    private int MONTH;
    private int DAY;

    public static final String RESULT = "result";
    public static final String EVENT = "event";
    private static final int ADD_NOTE = 44;
    private CalendarView mCalendarView;
    //private TextView textView;
    private List<EventDay> mEventDays = new ArrayList<>();
    private List<CalendarSingle> gCalendarSingles;
    private CalendarModel calendarModel = new CalendarModel("캘린더");
    private List<EventDay> gEventDats = new ArrayList <>();
    private List<Schedule> mSchedules = new ArrayList <>();
    private ProgressDialog progressDialog;
    public ArrayList<Ticket> CalendarDataList = new ArrayList<>();
    public String getPostType() {
        return "캘린더";
    }


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
        reviewRecyclerView = view.findViewById(R.id.reviewRecyclerView);
        mDatabase =  FirebaseDatabase.getInstance().getReference();
        db = new TicketDatabaseHandler(mDatabase);
        Context ct = container.getContext();
        calenderAdapter = new CalenderAdapter(db, ct, UID);
        curTime = new CurTime();

        YEAR =  curTime.getIntYear();
        MONTH =  curTime.getIntMonth();
        DAY =  curTime.getIntDay();

        String ticket_Date = YEAR + "-" + MONTH + "-" + DAY;

        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                Day day = collapsibleCalendar.getSelectedDay();
                Log.i(getClass().getName(), "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());
                String date = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
                getTicketDate(date);
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
        getTicketDate(ticket_Date);

        BottomSheetDialogFragment ticketingBottomSheetDialog = new TicketingBottomSheetDialog(ct);

        ticketFab = view.findViewById(R.id.ticketFab);
        ticketFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ticketingBottomSheetDialog.show(getFragmentManager(), ticketingBottomSheetDialog.getTag());
            }
        });


        reviewRecyclerView.setLayoutManager(new LinearLayoutManager(ct, LinearLayoutManager.VERTICAL, false));

        Collections.reverse(TicketList);
        calenderAdapter.setTicket(TicketList);
        reviewRecyclerView.setAdapter(calenderAdapter);


//        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
//        databaseReference =  FirebaseDatabase.getInstance().getReference();
//        Calendar calendar = Calendar.getInstance();
//        List<EventDay> eventDay = new ArrayList <>();
//        mCalendarView.setDate(calendar);
//        mCalendarView.setEvents(eventDay);
//
//        /* this.mSchedules = scheduleModel.getSchedules();*/
//
//        this.gCalendarSingles = calendarModel.getmCalendars();
//        Log.i("가져옴",valueOf(gCalendarSingles.size()));
//        for(int i = 0; i< gCalendarSingles.size(); i++) {
//
//            calendar.set(gCalendarSingles.get(i).getYear(), gCalendarSingles.get(i).getMonth(), gCalendarSingles.get(i).getDay());
//            gEventDats.add(new EventDay(calendar,R.drawable.ic_message_black_48dp)); //이벤트가 있는 날짜에 빨간원 표시
//            mCalendarView.setDate(gEventDats.get(i).getCalendar());
//            mCalendarView.setEvents(gEventDats);
//        }
//        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
//            @Override
//            public void onDayClick(EventDay eventDay) { //날짜를 클릭할때마다 해당 날짜의 목표,별점,메모 뷰 보여주는 이벤트
//                TextView datetitle= (TextView)view.findViewById(R.id.textView19);
//                TextView todo = (TextView)view.findViewById(R.id.textView20);
//                TextView Depart_time = (TextView)view.findViewById(R.id.textView21);
//                TextView Arrive_time = (TextView)view.findViewById(R.id.textView22);
//                TextView Flight_time = (TextView)view.findViewById(R.id.textView23);
//                TextView memo = (TextView)view.findViewById(R.id.textView24);
//                TextView star = (TextView)view.findViewById(R.id.textView25);
//
//                Calendar calendar = eventDay.getCalendar();
//
//                int YEAR=calendar.get(Calendar.YEAR);
//                int MONTH=calendar.get(Calendar.MONTH) + 1;
//                int DATE=calendar.get(Calendar.DATE);
//                String clickdate = YEAR+"년 "+MONTH+"월 "+DATE+"일";
//                String date = YEAR + "-" + MONTH + "-" + DATE;
//                System.out.println(clickdate);
//                datetitle.setText(clickdate); //클릭한날짜 보여주는 텍스트뷰
//
//
//
//                databaseReference.child("TICKET").child(UID).child(date).addValueEventListener(new ValueEventListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
//                            if (fileSnapshot != null) {
//                                Ticket ticket = new Ticket();
//                                ticket = fileSnapshot.getValue(Ticket.class);
//                                todo.setText(ticket.getTodo());
//                                Depart_time.setText(ticket.getDepart_time());
//                                Arrive_time.setText(ticket.getArrive_time());
//                                Flight_time.setText("총 "+ (Integer.parseInt(ExtractOnlyNumbers(ticket.getArrive_time()))-Integer.parseInt(ExtractOnlyNumbers(ticket.getDepart_time()))) +"분 동안 집중했어요!");
//                                memo.setText(ticket.getWait());
//                                star.setText(ticket.getWait());
//                                System.out.println("check >>>>>>> " + ticket.getTodo());
//
//
//                            }
//                            else {
//                                todo.setText("");
//                                Depart_time.setText("");
//                                Arrive_time.setText("");
//                                Flight_time.setText("");
//                                memo.setText("");
//                                star.setText("");
//                            }
//
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
//                    }
//                });
//
//                // setTextPreview(eventDay);
//            }
//        });
//        // textView = findViewById(R.id.preview_note);
///*        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addNote();
//            }
//        });*/
//
//        ValueEventListener calendarlistener = new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // Get CalendarSingle object and use the values to update the UI
////                CalendarSingle calendarSingle = dataSnapshot.getValue(CalendarSingle.class);
////                MyEventDay myEventDay;
////                Intent returnIntent = new Intent();
////                Calendar calendar = Calendar.getInstance();
////                calendar.set(calendarSingle.getYear(),calendarSingle.getMonth(),calendarSingle.getDay());
////                myEventDay = new MyEventDay(calendar, R.drawable.ic_message_black_48dp, calendarSingle.getNote());
////                returnIntent.putExtra(CalendarActivity.RESULT, myEventDay);
////                setResult(Activity.RESULT_OK, returnIntent);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Getting Post failed, log a message
//                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
//                // ...
//            }
//        };
//        // databaseReference.addValueEventListener(calendarlistener);
//
//
//
        return view;
    }

    private  void invalidateRecursive(ViewGroup layout){

        int count = layout.getChildCount();
        View child ;
        for (int i = 0; i<count ;i++){
            child = layout.getChildAt(i);
            if (child instanceof ViewPager){
                ViewPager pager = (ViewPager)child;
                pager.getAdapter().notifyDataSetChanged();
            }
            if (child instanceof ViewGroup){
                ViewGroup group = (ViewGroup)child;
                invalidateRecursive(group);
            }
        }
    }

    public String ExtractOnlyNumbers(String str) { //문자가 섞인 string 문자열에서 숫자만 추출하는 함수
        String intStr = str.replaceAll("[^0-9]", "");
        System.out.println(intStr);
        return intStr;
    }

    public int Calculate(String str1, String str2){ //문자열 str1,str2을 정수로 변환하여 시간차이를 계산하는 함수
        int result  =Integer.parseInt(str1)-Integer.parseInt(str2);
        return result;
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
                TicketList.sort(new Comparator<Ticket>() {
                    @Override
                    public int compare(Ticket o1, Ticket o2) {
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
                    }
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