package com.example.stop_and_flight;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.webianks.library.scroll_choice.ScrollChoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static android.content.ContentValues.TAG;
import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ticketing_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ticketing_Fragment extends Fragment {

    Context mContext;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String UID;
    private String real_uid; //규원님 이거 제가 uid 가져오려고 만든 변수예요 원래 있던 코드는 디폴트값으로 설정되어있는것같아서 101~104줄 추가해써욤
    private String select_todo;
    private HashMap<String, Object> TodoMap;
    private AlarmManager AM;
    private PendingIntent ServicePending;
    int dptH;
    int dptM;
    private DatabaseReference mDatabase;
    private int YEAR;
    private int MONTH;
    private int DAY;
    private String ticket_Date;
    int num;
    HashMap<String,Object> num_info=new HashMap<>();
    public Ticketing_Fragment() {
        // Required empty public constructor
    }

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
        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            real_uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            if (getArguments().getString(ARG_PARAM2) != null)
                UID = getArguments().getString(ARG_PARAM2);
            else
                UID = getArguments().getString("UID", "0");
        }
    }
    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        mContext= context;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticketing, container, false);
        // Inflate the layout for this fragment
        TextView txt_date=(TextView)view.findViewById(R.id.txt_Date);
        ImageButton btn_date=(ImageButton) view.findViewById(R.id.btn_datepick);


        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c=Calendar.getInstance();
                int mYear=c.get(Calendar.YEAR);
                int mMonth=c.get(Calendar.MONTH);
                int mDay=c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog =new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt_date.setText(year+"년"+(monthOfYear+1)+"월"+dayOfMonth+"일");
                        YEAR=year;
                        MONTH=monthOfYear;
                        DAY=dayOfMonth;
                        String str_year=Integer.toString(year);
                        String str_month=Integer.toString(monthOfYear+1);
                        String str_day=Integer.toString(dayOfMonth);
                        ticket_Date= str_year+"-"+str_month+"-"+str_day;
                    }
                },mYear,mMonth,mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        Button select_todo_button = (Button) view.findViewById(R.id.select_todo_button);

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

        select_todo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(SelectTodoFragment.newInstance(null, UID));
            }
        });

        if (mParam1 != null)
        {
            select_todo_button.setText(mParam1);
        }

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
                    dptH=depart_hour;
                    dptM=depart_min;
                }
                else
                {
                    depart_hour = depart_time.getCurrentHour();
                    depart_min = depart_time.getCurrentMinute();
                    arrive_hour = arrive_time.getCurrentHour();
                    arrive_min = arrive_time.getCurrentMinute();
                    dptH=depart_hour;
                    dptM=depart_min;
                }

                if((Integer.parseInt(strCurHour) < depart_hour) || ((Integer.parseInt(strCurHour) == depart_hour) && (Integer.parseInt(strCurMinute) <= depart_min)))
                {
                    if((depart_hour < arrive_hour) || ((depart_hour == arrive_hour) && (depart_min <= arrive_min)))
                    {
                        String depart_time =  Integer.toString(depart_hour) + ":" + Integer.toString(depart_min);
                        String arrive_time =  Integer.toString(arrive_hour) + ":" + Integer.toString(arrive_min);
                        insert_TicketDB(depart_time, arrive_time, mParam1, ticket_Date);
                    }
                    else {
                        Toast.makeText(getContext(), "출발 시간이 도착 시간 보다 빨라야 합니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(), "현재 보다 이전 시간을 설정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getContext(), "확인버튼", Toast.LENGTH_SHORT).show();

                //날짜 db에 저장
//                insert_DateDB(YEAR,MONTH,DAY,select_todo);

                //프래그먼트 전환
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
                onDestroy();
            }
        });
        return view;
    }//oncreateview 끝

    private void insert_TicketDB(String depart_time, String arrive_time, String todo, String ticket_date) {
        //mDatabase = FirebaseDatabase.getInstance().getReference(); 규원님 제가 이라인 oncreate로 올려놓았어용
        Ticket ticket = new Ticket(depart_time, arrive_time, todo);
        mDatabase.child("TICKET").child(real_uid).child(ticket_date).child(String.valueOf(num)).setValue(ticket);

        mDatabase.child("TICKET").child(real_uid).child(ticket_date).child(String.valueOf(num)).child("wait").setValue("true");
        mDatabase.child("TICKET").child(real_uid).child(ticket_date).child(String.valueOf(num)).child("id").setValue(num);
        num++;
        mDatabase.child("TICKET").child(real_uid).child("total_num").setValue(num);
        System.out.println("확인"+num);

    }
    //TODO: todo가 저장이 안됨
//    private void insert_DateDB(int year,int month,int day,String todo){
//
//        String date= year+"/"+month+"/"+day;
//        mDatabase.child("TICKET").child(real_uid).child(date).child("date").setValue(date);
//
//    }

    @Override
    public void onDestroy() {
        AM = (AlarmManager) mContext.getSystemService(mContext.ALARM_SERVICE);

//        String date_time= YEAR+"-"+(MONTH+1)+"-"+DAY+" "+ ticket_dpt+":"+00;
//        SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


//        Date datetime = null;
//        try {
//            datetime = new Date(dateFormat.parse(date_time).getTime());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(datetime+"확인datetime");
        int dpth=dptH;
        int dptm=dptM;
        Calendar cal=Calendar.getInstance();
        cal.clear();
//        System.out.println(dptH+"확인 ticketdpt");
//        cal.set(Calendar.YEAR,YEAR);
//        cal.set(Calendar.MONTH,MONTH+1);
//        cal.set(Calendar.DAY_OF_MONTH,DAY);
//
//
//        cal.set(Calendar.HOUR_OF_DAY, dpth);
//        cal.set(Calendar.MINUTE, dptm);
//        cal.set(Calendar.SECOND, 0);

        cal.set(YEAR,MONTH,DAY,dpth,dptm);
        System.out.println(cal.getTime()+"확인 cal에 셋된시간");


//        cal.setTime(datetime);
        //Receiver로 보내기 위한 인텐트
        Intent intent_alarm = new Intent(mContext,AlarmReceiver.class);

        ServicePending = PendingIntent.getBroadcast(
                mContext, 0, intent_alarm, PendingIntent.FLAG_ONE_SHOT);
        long calc_time=cal.getTimeInMillis();
        if (Build.VERSION.SDK_INT < 23) {
            // 19 이상
            if (Build.VERSION.SDK_INT >= 19) {
                AM.setExact(AlarmManager.RTC_WAKEUP,calc_time , ServicePending);
                System.out.println("확인 19이상");

            }
            // 19 미만
            else {
                // pass
                // 알람셋팅
                AM.set(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
                System.out.println("확인 19미만");

            }
            // 23 이상
        } else {
            AM.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calc_time, ServicePending);
            System.out.println("확인 23이상");
        }
        System.out.println("확인 알람설정 ok");


        super.onDestroy();

    }
    //    private void insert_DateDB(int year,int month,int day,String todo){
//
//        String date= year+"/"+month+"/"+day;
//        mDatabase.child("TICKET").child(real_uid).child(date).child("date").setValue(date);
//
//    }
}