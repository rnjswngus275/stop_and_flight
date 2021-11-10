package com.doremifa.stop_and_flight.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.doremifa.stop_and_flight.AlarmReceiver;
import com.doremifa.stop_and_flight.R;
import com.doremifa.stop_and_flight.model.Ticket;
import com.doremifa.stop_and_flight.model.CurTime;
import com.doremifa.stop_and_flight.utils.TicketDatabaseHandler;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TicketingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TicketingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private static final int DEFAULT = -1;
    private int mParam1;
    private String mParam2;
    private String Todo = null;
    private String Friend = null;
    private String UID;
    private AlarmManager AM;
    private PendingIntent ServicePending;
    private final Context context;
    private DatabaseReference mDatabase;
    private int flag = 0;
    private int Id;
    private int updateId = -1;
    private String ticket_Date;
    private List<String> select_Date = new ArrayList<>();
    private CurTime curTime;
    private HashMap<String, Object> TicketMap;
    int count3=1;

    public TicketingFragment(Context context) {
        this.context = context;
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Ticketing_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TicketingFragment newInstance(int param1, String param2, Context context) {
        TicketingFragment fragment = new TicketingFragment(context);
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user != null){
            UID  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            // update인 경우 - ticketbottomSheetDialog에서 데이터를 가져옴
            Todo = getArguments().getString("Todo");
            Friend = getArguments().getString("Friend");
            updateId = getArguments().getInt("Id");
            ticket_Date = getArguments().getString("Date");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticketing, container, false);
        curTime = new CurTime();

        Button select_data_button = view.findViewById(R.id.select_data_button);
        Button select_todo_button = view.findViewById(R.id.select_todo_button);
        Button select_friend_button = view.findViewById(R.id.select_friend_button);
        TimePicker depart_time = view.findViewById(R.id.depart_time);
        TimePicker arrive_time = view.findViewById(R.id.arrive_time);
        Button ticketing_button = view.findViewById(R.id.ticketing_button);
        // context 전달 필요 = Adapter까지 전달!!
        select_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateId != DEFAULT)
                {
                    ((TicketingBottomSheetDialog) getParentFragment()).DialogReplaceFragment(SelectTodoFragment.newInstance(updateId, Todo, context));

                }
                else
                {
                    ((TicketingBottomSheetDialog) getParentFragment()).DialogReplaceFragment(SelectTodoFragment.newInstance(updateId, null,  context));
                }
            }
        });

        select_friend_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateId != DEFAULT)
                {
                    ((TicketingBottomSheetDialog) getParentFragment()).DialogReplaceFragment(SelectFriendFragment.newInstance(updateId, Friend, Todo, context));

                }
                else
                {
                    ((TicketingBottomSheetDialog) getParentFragment()).DialogReplaceFragment(SelectFriendFragment.newInstance(updateId, null, Todo,  context));
                }
            }

        });

        if (Todo != null)
        {
            select_todo_button.setText(Todo);
        }

        if (Friend != null)
        {
            select_friend_button.setText(Friend);
        }

        if (ticket_Date == null)
        {
            ticket_Date = curTime.getWeekset();
        }
        select_data_button.setText(ticket_Date);

        CalendarConstraints.Builder calendarConstraintbuilder = new CalendarConstraints.Builder();
        calendarConstraintbuilder.setValidator(DateValidatorPointForward.now());
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setCalendarConstraints(calendarConstraintbuilder.build());
        MaterialDatePicker picker = builder.build();

        select_data_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                picker.show(getFragmentManager(), picker.toString());
            }
        });

        picker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                Pair<Long, Long> getdate = (Pair<Long, Long>) selection;
                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");

                Date start = new Date(getdate.first);
                Date end = new Date(getdate.second);
                CurTime curTime = new CurTime();

                select_Date = curTime.getDatesBetweenUsingJava7(start, end);
                select_data_button.setText(dateformat.format(start) +" ~ " +dateformat.format(end));
            }
        });


        ticketing_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Todo == null)
                {
                    Todo = "뭐든 잘할 수 있어!!";
                }
                int depart_hour = curTime.getIntHour();
                int depart_min = curTime.getIntMinute();
                int arrive_hour = curTime.getIntHour();
                int arrive_min = curTime.getIntMinute();

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    depart_hour = depart_time.getHour();
                    depart_min = depart_time.getMinute();
                    arrive_hour = arrive_time.getHour();
                    arrive_min = arrive_time.getMinute();

                }
                if (select_Date.size() > 0)
                {
                    for (String date : select_Date)
                    {
                        String[] date_time = date.split("-");
                        int Day = Integer.parseInt(date_time[2]);

                        if (Day == curTime.getIntDay())
                        {
                            if(curTime.getIntHour() < depart_hour || (curTime.getIntHour() == depart_hour && curTime.getIntMinute() <= depart_min)) {
                                check_Schedule(date, depart_hour, depart_min, arrive_hour, arrive_min);
                            }
                            else {
                                Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            check_Schedule(date, depart_hour,depart_min, arrive_hour, arrive_min);
                        }
                    }
                }
                else {
                    String[] date_time = ticket_Date.split("-");
                    int Day = Integer.parseInt(date_time[2]);
                    if (Day == curTime.getIntDay())
                    {
                        if(curTime.getIntHour() < depart_hour || (curTime.getIntHour() == depart_hour && curTime.getIntMinute() <= depart_min))
                        {
                            check_Schedule(ticket_Date, depart_hour, depart_min, arrive_hour, arrive_min);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        check_Schedule(ticket_Date, depart_hour,depart_min, arrive_hour, arrive_min);
                    }
                }
            }
        });
        return view;
    }

    private void check_Schedule(String Date, int depart_hour , int depart_min, int arrive_hour, int arrive_min)
    {
        flag = 0;
        mDatabase.child("TICKET").child(UID).child(Date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    if (fileSnapshot != null) {
                        TicketMap = (HashMap<String, Object>) fileSnapshot.getValue();
                        String depart = String.valueOf(TicketMap.get("depart_time"));
                        String arrive = String.valueOf(TicketMap.get("arrive_time"));
                        Id = Integer.parseInt(String.valueOf(TicketMap.get("id")));
                        String[] depart_arr = depart.split(":");
                        String[] arrive_arr = arrive.split(":");

                        if (Integer.parseInt(depart_arr[0]) > arrive_hour ||
                                (Integer.parseInt(depart_arr[0]) == arrive_hour &&
                                        Integer.parseInt(depart_arr[1]) > arrive_min))
                        {
                            continue;

                        }
                        else if (Integer.parseInt(arrive_arr[0]) < depart_hour ||
                                (Integer.parseInt(arrive_arr[0]) == depart_hour &&
                                        Integer.parseInt(arrive_arr[1]) < depart_min))
                        {
                            continue;
                        }
                        else {
                            flag = 1;
                            break;
                        }
                    }
                }
                if (flag == 0)
                {
                    time_Validity(Date, depart_hour, depart_min, arrive_hour, arrive_min);
                }
                else
                {
                    Toast.makeText(getContext(), "이미 예약된 시간이 있습니다.", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
    }
private void check_Friend(String UID, String ticket_Date, Ticket ticket)
    {
        if (Friend != null)
            mDatabase.child("users").child(Friend).child("together").child(UID).child(ticket_Date).child(String.valueOf(Id)).setValue(ticket);
    }

    private void time_Validity(String ticket_Date, int depart_hour , int depart_min, int arrive_hour, int arrive_min)
    {
        TicketDatabaseHandler db = new TicketDatabaseHandler(mDatabase);

        Random random = new Random();
        int requestcode = random.nextInt();

        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("requestcode",requestcode);

        String depart_time =  depart_hour + ":" + depart_min;
        String arrive_time =  arrive_hour + ":" + arrive_min;
        Ticket ticket = new Ticket(depart_time, arrive_time, Todo, ++Id , 0, requestcode);

        if (depart_hour > 12 && arrive_hour < 12)
        {
            if (updateId != DEFAULT)
            {
                db.update_ticketDB(UID, ticket_Date, depart_time, arrive_time, Todo, updateId,requestcode);
            }
            else
            {
                db.insert_ticketDB(UID, ticket_Date, ticket);
                check_Friend(UID,ticket_Date,ticket);
                SetAlarmManager(ticket_Date, depart_hour, depart_min, requestcode);
            }
            Toast.makeText(getContext(),  "예약 되었습니다.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            if((depart_hour < arrive_hour) || ((depart_hour == arrive_hour) && (depart_min + 1 <= arrive_min)))
            {
                if (updateId != DEFAULT)
                {
                    db.update_ticketDB(UID, ticket_Date, depart_time, arrive_time, Todo, updateId,requestcode);
                }
                else
                {
                    db.insert_ticketDB(UID, ticket_Date, ticket);
                    SetAlarmManager(ticket_Date, depart_hour, depart_min, requestcode);
                    check_Friend(UID,ticket_Date,ticket);
                }
                Toast.makeText(getContext(),  "예약 되었습니다.", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "출발 시간이 도착 시간 보다 적어도 1분 빨라야 합니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void SetAlarmManager(String ticket_Date, int dpth, int dptm, int requestcode) {
        AM = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        String date_time= YEAR+"-"+(MONTH+1)+"-"+DAY+" "+ ticket_dpt+":"+00;
//        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//        Date datetime = null;
//        try {
//            datetime = new Date(dateFormat.parse(date_time).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        Calendar cal = Calendar.getInstance();
        cal.clear();

        String[] date_time = ticket_Date.split("-");
        int Year = Integer.parseInt(date_time[0]);
        int Month = Integer.parseInt(date_time[1]);
        int Day = Integer.parseInt(date_time[2]);


        cal.set(Year, Month - 1, Day, dpth, dptm);
//        cal.setTime(datetime);
        //Receiver로 보내기 위한 인텐트
        Intent intent_alarm = new Intent(context, AlarmReceiver.class);

        ServicePending = PendingIntent.getBroadcast(context, requestcode, intent_alarm, PendingIntent.FLAG_ONE_SHOT);

        long calc_time = cal.getTimeInMillis();

        if (Build.VERSION.SDK_INT < 23) {
            // 19 이상
            if (Build.VERSION.SDK_INT >= 19) {
                AM.setExact(AlarmManager.RTC_WAKEUP, calc_time , ServicePending);
            }
            // 19 미만
            else {
                // pass
                // 알람셋팅
                AM.set(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
            }
            // 23 이상
        } else {
            AM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
        }
    }
}
