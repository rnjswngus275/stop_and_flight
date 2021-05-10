package com.example.stop_and_flight;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ticketing_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ticketing_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UID;
    private String select_todo;
    private DatabaseReference mDatabase;

    public Ticketing_Fragment() {
        // Required empty public constructor
    }

    private List<String> goal_list = new ArrayList<>();
    private TextView textView;
    private ScrollChoice scrollChoice;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ticketing_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Ticketing_Fragment newInstance(String param1, String param2) {
        Ticketing_Fragment fragment = new Ticketing_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            UID = getArguments().getString("UID", "0");
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticketing, container, false);
        // Inflate the layout for this fragment

        init_todo(goal_list);
        scrollChoice = (ScrollChoice) view.findViewById(R.id.scroll_choice);
        scrollChoice.addItems(goal_list, 1);
        scrollChoice.setOnItemSelectedListener(new ScrollChoice.OnItemSelectedListener() {
            @Override
            public void onItemSelected(ScrollChoice scrollChoice, int position, String name) {
                select_todo = name;
            }});

        long now = System.currentTimeMillis();
        Date date = new Date(now);

        int arrive_hour;
        int arrive_min;

        TimePicker depart_time = (TimePicker)view.findViewById(R.id.depart_time);
        TimePicker arrive_time = (TimePicker)view.findViewById(R.id.arrive_time);

        Button ticketing_button = (Button) view.findViewById(R.id.ticketing_button);

        /* 현재 시간과 날짜를 받아오는 부분 */
        SimpleDateFormat CurYearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat CurMonthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat CurDayFormat = new SimpleDateFormat("dd");
        SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
        SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

        String strCurYear = CurYearFormat.format(date);
        String strCurMonth = CurMonthFormat.format(date);
        String strCurDay = CurDayFormat.format(date);
        String strCurHour = CurHourFormat.format(date);
        String strCurMinute = CurMinuteFormat.format(date);

        ticketing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int depart_hour;
                int depart_min;
                int arrive_hour;
                int arrive_min;

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    depart_hour = depart_time.getHour();
                    depart_min = depart_time.getMinute();
                    arrive_hour = arrive_time.getHour();
                    arrive_min = arrive_time.getMinute();
                }
                else
                {
                    depart_hour = depart_time.getCurrentHour();
                    depart_min = depart_time.getCurrentMinute();
                    arrive_hour = arrive_time.getCurrentHour();
                    arrive_min = arrive_time.getCurrentMinute();
                }

                if((Integer.parseInt(strCurHour) < depart_hour) || ((Integer.parseInt(strCurHour) == depart_hour) && (Integer.parseInt(strCurMinute) <= depart_min)))
                {
                    if((depart_hour < arrive_hour) || ((depart_hour == arrive_hour) && (depart_min <= arrive_min)))
                    {
                        String depart_time =  Integer.toString(depart_hour) + ":" + Integer.toString(depart_min);
                        String arrive_time =  Integer.toString(arrive_hour) + ":" + Integer.toString(arrive_min);
                        insert_TicketDB(depart_time, arrive_time, select_todo);
                    }
                    else {
                        Toast.makeText(getContext(), "출발 시간이 도착 시간 보다 빨라야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void init_todo(List<String> goal_list) {
        goal_list.clear();
        goal_list.add("없음");
        search_GOALDB();
    }

    private void search_GOALDB() {
    }

    private void insert_TicketDB(String depart_time, String arrive_time, String todo) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Ticket ticket = new Ticket(depart_time, arrive_time, todo);
        mDatabase.child("TICKET").child(UID).child(todo).setValue(ticket);
    }
}