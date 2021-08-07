package com.example.stop_and_flight;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.example.stop_and_flight.model.CalendarModel;
import com.example.stop_and_flight.model.Ticket;
import com.example.stop_and_flight.singleton.CalendarSingle;
import com.example.stop_and_flight.singleton.Schedule;
import com.github.mikephil.charting.charts.PieChart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;

public class CalendarFragment extends Fragment {
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
    private DatabaseReference databaseReference;
    private String UID;
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

        View view = inflater.inflate(R.layout.activity_calendar, container, false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        mCalendarView = (CalendarView) view.findViewById(R.id.calendarView);
        databaseReference =  FirebaseDatabase.getInstance().getReference();
        Calendar calendar = Calendar.getInstance();
        List<EventDay> eventDay = new ArrayList <>();
        mCalendarView.setDate(calendar);
        mCalendarView.setEvents(eventDay);

        /* this.mSchedules = scheduleModel.getSchedules();*/

        this.gCalendarSingles = calendarModel.getmCalendars();
        Log.i("가져옴",valueOf(gCalendarSingles.size()));
        for(int i = 0; i< gCalendarSingles.size(); i++) {

            calendar.set(gCalendarSingles.get(i).getYear(), gCalendarSingles.get(i).getMonth(), gCalendarSingles.get(i).getDay());
            gEventDats.add(new EventDay(calendar,R.drawable.ic_message_black_48dp)); //이벤트가 있는 날짜에 빨간원 표시
            mCalendarView.setDate(gEventDats.get(i).getCalendar());
            mCalendarView.setEvents(gEventDats);
        }
        mCalendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) { //날짜를 클릭할때마다 해당 날짜의 목표,별점,메모 뷰 보여주는 이벤트
                TextView datetitle= (TextView)view.findViewById(R.id.textView19);
                TextView todo = (TextView)view.findViewById(R.id.textView20);
                TextView Depart_time = (TextView)view.findViewById(R.id.textView21);
                TextView Arrive_time = (TextView)view.findViewById(R.id.textView22);
                TextView Flight_time = (TextView)view.findViewById(R.id.textView23);
                TextView memo = (TextView)view.findViewById(R.id.textView24);
                TextView star = (TextView)view.findViewById(R.id.textView25);

                Calendar calendar = eventDay.getCalendar();

                int YEAR=calendar.get(Calendar.YEAR);
                int MONTH=calendar.get(Calendar.MONTH) + 1;
                int DATE=calendar.get(Calendar.DATE);
                String clickdate = YEAR+"년 "+MONTH+"월 "+DATE+"일";
                String date = YEAR + "-" + MONTH + "-" + DATE;
                System.out.println(clickdate);
                datetitle.setText(clickdate); //클릭한날짜 보여주는 텍스트뷰



                databaseReference.child("TICKET").child(UID).child(date).addValueEventListener(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                            if (fileSnapshot != null) {
                                Ticket ticket = new Ticket();
                                ticket = fileSnapshot.getValue(Ticket.class);
                                todo.setText(ticket.getTodo());
                                Depart_time.setText(ticket.getDepart_time());
                                Arrive_time.setText(ticket.getArrive_time());
                                Flight_time.setText("총 "+ (Integer.parseInt(ExtractOnlyNumbers(ticket.getArrive_time()))-Integer.parseInt(ExtractOnlyNumbers(ticket.getDepart_time()))) +"분 동안 집중했어요!");
                                memo.setText(ticket.getWait());
                                star.setText(ticket.getWait());
                                System.out.println("check >>>>>>> " + ticket.getTodo());


                            }
                            else {
                                todo.setText("");
                                Depart_time.setText("");
                                Arrive_time.setText("");
                                Flight_time.setText("");
                                memo.setText("");
                                star.setText("");
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
                    }
                });

                // setTextPreview(eventDay);
            }
        });
        // textView = findViewById(R.id.preview_note);
/*        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });*/

        ValueEventListener calendarlistener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get CalendarSingle object and use the values to update the UI
//                CalendarSingle calendarSingle = dataSnapshot.getValue(CalendarSingle.class);
//                MyEventDay myEventDay;
//                Intent returnIntent = new Intent();
//                Calendar calendar = Calendar.getInstance();
//                calendar.set(calendarSingle.getYear(),calendarSingle.getMonth(),calendarSingle.getDay());
//                myEventDay = new MyEventDay(calendar, R.drawable.ic_message_black_48dp, calendarSingle.getNote());
//                returnIntent.putExtra(CalendarActivity.RESULT, myEventDay);
//                setResult(Activity.RESULT_OK, returnIntent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        // databaseReference.addValueEventListener(calendarlistener);




        return view;
    }


    //note페이지로 이동
/*        Intent intent = new Intent(this, NotePreviewActivity.class);
        if(eventDay instanceof MyEventDay){
            intent.putExtra(EVENT, (MyEventDay) eventDay);
        }
        startActivity(intent);*/


/*    DatePickerBuilder builder = new DatePickerBuilder(this, listener)
            .pickerType(CalendarView.ONE_DAY_PICKER);

    DatePicker datePicker = builder.build();
    datePicker.show();*/

    /*    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (requestCode == ADD_NOTE && resultCode == RESULT_OK) {
                MyEventDay myEventDay = data.getParcelableExtra(RESULT);
                mCalendarView.setDate(myEventDay.getCalendar());
                mEventDays.add(myEventDay);
                mCalendarView.setEvents(mEventDays);
            }
        }*/


//    private void setTextPreview(EventDay eventDay) {
//        textView.setText();
//        //이후에 캘린더 텍스트뷰에 미리보기 추가
//    }



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



}