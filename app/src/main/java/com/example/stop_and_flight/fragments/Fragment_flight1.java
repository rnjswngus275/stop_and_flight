package com.example.stop_and_flight.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.stop_and_flight.R;
import com.example.stop_and_flight.utils.Accessibility;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_flight1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_flight1 extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    DatabaseReference mDatabase ;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivity myContext;
    private CountDownTimer countDownTimer;
    private TextView count_txt;
    private TextView read_time;
    String uid="";
    String time="";
    String set_year;
    String set_date;
    String set_day;
    String today;
    HashMap<String,Object> ticket_info=new HashMap<String,Object>();
    HashMap<String,Object> ticket_info2=new HashMap<String,Object>();
    String set_dpt_time = null;
    String set_arr_time= null;
    String set_requestcode= null;
    String set_id= null;
    String set_todo=null;
    String set_success=null;
    long emergency_time;


    String[] getTicket;
    int arr;
    int dpt;
    int num;//일정개수
    private OnTimePickListener onTimePickListener;
    // Required empty public constructor

    // Flight Activity로 출발시간 도착시간 데이터 보내기
    public interface OnTimePickListener{
        void onTimeSelected(String arr, String dpt);
    }
    public Fragment_flight1() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_flight1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_flight1 newInstance(String param1, String param2) {
        Fragment_flight1 fragment = new Fragment_flight1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    private String getTime(String set_hour,String set_min) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int c_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int c_min = calendar.get(Calendar.MINUTE);
        int c_sec = calendar.get(Calendar.SECOND);

        Calendar baseCal = new GregorianCalendar(year, month, day, c_hour, c_min, c_sec);        //현재날짜
        Calendar targetCal = new GregorianCalendar(year, month, day, Integer.parseInt(set_hour), Integer.parseInt(set_min), 0);  //비교대상날짜
        //지금은 현재날짜에서 +2일로 해놨는데 나중에 설정된 날짜 시간 받아와서 해야함

        long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000;        //비교대상 날짜-현재날짜를 1000분의 1초 단위로 하는게 gettimeinmills 그래서 1000으로 나눠줘야함
        long diffDays = diffSec / (24 * 60 * 60);

        targetCal.add(Calendar.DAY_OF_MONTH, (int) (-diffDays));

        int hourTime = (int) Math.floor((double) (diffSec / 3600));
        int minTime = (int) Math.floor((double) (((diffSec - (3600 * hourTime)) / 60)));
        int secTime = (int) Math.floor((double) (((diffSec - (3600 * hourTime)) - (60 * minTime))));

        String hour = String.format("%02d", hourTime);
        String min = String.format("%02d", minTime);
        String sec = String.format("%02d", secTime);

        return  hour + " 시간 " + min + " 분 " + sec + "초 남았습니다.";

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }
    public String[] ticket_infomation (String arr,String dpt,String id,String success,String todo){

        String[] ticket=new String[5];
        ticket[0]=arr;
        ticket[1]=dpt;
        ticket[2]=id;
        ticket[3]=success;
        ticket[4]=todo;
        return ticket;
    }

    public void countDownTimer(View v,String set_hour,String set_min,String set_h,String set_m){

        Calendar baseCal = new GregorianCalendar(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,Integer.parseInt(set_hour),Integer.parseInt(set_min));        //출발
        Calendar baseCal2 = new GregorianCalendar(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,Integer.parseInt(set_h),Integer.parseInt(set_m));        //도착

        //지금은 현재날짜에서 +2일로 해놨는데 나중에 설정된 날짜 시간 받아와서 해야함

        long diffSec = (baseCal2.getTimeInMillis() - baseCal.getTimeInMillis());        //비교대상 날짜-현재날짜를 1000분의 1초 단위로 하는게 gettimeinmills 그래서 1000으로 나눠줘야함
//        long diffDays = diffSec / (24 * 60 * 60);
        countDownTimer = new CountDownTimer(diffSec,1000) {          //첫번째 인자 : 총시간(제한시간) 두번째 인수: 몇초마다 타이머 작동
            @Override
            public void onTick(long millisUntilFinished) {
                count_txt=(TextView)v.findViewById(R.id.count_txt);

                count_txt.setText(getTime(set_h,set_m));
            }

            @Override
            public void onFinish() {
                replaceFragment(FlightSuccessFragment.newInstance(today,set_arr_time, set_dpt_time,set_todo,set_id));
                onDestroy();
            }
        };
    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager =getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flight, fragment).addToBackStack(null).commitAllowingStateLoss();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance(); // 유저 계정 정보 가져오기
        mDatabase = FirebaseDatabase.getInstance().getReference(); // 파이어베이스 realtime database 에서 정보 가져오기

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); // 로그인한 유저의 정보 가져오기
        if(user!=null){
            uid  = user.getUid(); // 로그인한 유저의 고유 uid 가져오기
        }

        View v = inflater.inflate(R.layout.fragment_flight1, container, false);

        Button emergencybutton = v.findViewById(R.id.button_emergency);
        Button appaccessbutton = v.findViewById(R.id.button_app);
        TextView todo =v.findViewById(R.id.todo_txt);
        LottieAnimationView animation =v.findViewById(R.id.lottie);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 5 && timeOfDay < 11){
            animation.setAnimation("51900-good-morning.json");
            animation.loop(true);
            animation.playAnimation();
        }else if(timeOfDay >= 11 && timeOfDay < 17){
            animation.setAnimation("51901-good-afternoon.json");
            animation.loop(true);
            animation.playAnimation();
        }else if(timeOfDay >= 17 && timeOfDay < 21){
            animation.setAnimation("51902-good-evening.json");
            animation.loop(true);
            animation.playAnimation();

        }else if(timeOfDay >= 21 && timeOfDay < 24){
            animation.setAnimation("51903-good-night.json");
            animation.loop(true);
            animation.playAnimation();
        }
        else if(timeOfDay >= 0 && timeOfDay <5){
            animation.setAnimation("51903-good-night.json");
            animation.loop(true);
            animation.playAnimation();
        }


        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int c_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int c_min = calendar.get(Calendar.MINUTE);
        int c_sec = calendar.get(Calendar.SECOND);

        String str_year=Integer.toString(year);
        String str_month=Integer.toString(month+1);
        String str_day=Integer.toString(day);

        today=str_year+"-"+str_month+"-"+str_day;

        ArrayList al =new ArrayList();

        //데이터 베이스 읽어오기(시간) 조건 : 오늘날짜에 맞음, 시간
        final DatabaseReference ref = mDatabase.child("TICKET").child(uid).child(today);
        Query query =ref.orderByChild("depart_time");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                al.clear();
                for (DataSnapshot messageData : snapshot.getChildren()) {// child 내에 있는 데이터만큼 반복합니다.
                    //for (변수선언:배열 또는 배열을 리턴하는 함수(받을 데이터))

                    ticket_info2=(HashMap<String, Object>)messageData.getValue();
                    String set_arr= (String.valueOf( ticket_info2.get("arrive_time")));
                    String set_dpt= (String.valueOf( ticket_info2.get("depart_time")));
                    String set_id=(String.valueOf( ticket_info2.get("id")));
                    Long set_requestcode= (Long) ticket_info2.get("requestcode");
                    String set_success= (String.valueOf(ticket_info2.get("success")));
                    String set_todo=(String.valueOf(ticket_info2.get("todo")));

                    al.addAll(Arrays.asList(ticket_infomation(set_arr,set_dpt,set_id,set_success,set_todo)));


                }

                int size=al.size();

                for(int i=3;i<size;i=i+5){
                    if(String.valueOf(al.get(i)).equals("0"))
                    {
                        set_arr_time = (String) al.get(i-3);
                        set_dpt_time = (String) al.get(i-2);
                        set_id= (String) al.get(i-1);
                        set_todo=(String)al.get(i+1);

                        int idx=set_dpt_time.indexOf(":");
                        String set_time1=set_dpt_time.substring(0,idx);
                        String set_time2=set_dpt_time.substring(idx+1);

                        int idx2=set_arr_time.indexOf(":");
                        String set_time_arr1=set_arr_time.substring(0,idx2);
                        String set_time_arr2=set_arr_time.substring(idx2+1);

                        countDownTimer(v,set_time1,set_time2,set_time_arr1,set_time_arr2);
                        countDownTimer.start();
                        break;
                    }
                }

                onTimePickListener.onTimeSelected(set_arr_time,set_dpt_time);

                read_time= v.findViewById(R.id.read_time);
                read_time.setText(time);
                todo.setText(set_todo);
//                btn_suc.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                    //이 코드를 타이머가 끝나면 일어나도록 하기
//                      replaceFragment(FlightSuccessFragment.newInstance(today,set_dpt_time, set_arr_time,set_goal,set_id));
//
//                    }
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference ref2 = mDatabase.child("users").child(uid).child("emergency_time");
        ref2.addValueEventListener(new ValueEventListener() {        //emergency_time 읽어오기
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot != null)
                    emergency_time = snapshot.getValue(long.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        emergencybutton.setOnClickListener(this);
        appaccessbutton.setOnClickListener(this);
        // Inflate the layout for this fragment


        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_emergency:
                ShowEmergencyMessage(v);
                break;
            case R.id.button_app:
                ShowAccessApplist(v);
                break;
        }
    }

    private void ShowAccessApplist(View v) {

        ArrayList<String> applist = new ArrayList<>();

        BottomSheetDialog AccessSheetDialog = new BottomSheetDialog(getActivity());
        AccessSheetDialog.setContentView(R.layout.app_dialog_searchable_spinner);
//        AccessSheetDialog.getWindow().setLayout(1000, 1200);
//        AccessSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        AccessSheetDialog.getWindow().getContainer().setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        EditText editText = AccessSheetDialog.findViewById(R.id.app_edit_text);
        ListView listView = AccessSheetDialog.findViewById(R.id.app_list_view);
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, applist);
        mDatabase.child("APP").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot fileSnapshot : snapshot.getChildren()) {
                    String appname = fileSnapshot.getValue(String.class);
                    applist.add(appname);
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ReadAndWriteSnippets", "loadPost:onCancelled", error.toException());
            }
        });
        listView.setAdapter(adapter);
        AccessSheetDialog.show();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent sintent = new Intent(getActivity(), Accessibility.class); // 이동할 컴포넌트
                sintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                sintent.putExtra("appname", adapter.getItem(position).toString());
                Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(adapter.getItem(position).toString());
                startActivity(launchIntent);
                AccessSheetDialog.dismiss();
            }
        });
    }

    private void ShowEmergencyMessage(View v) {
        AlertDialog.Builder embuilder = new AlertDialog.Builder(getActivity());
        embuilder.setTitle("손님!! 비상 탈출 하시겠습니까?");
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.emergency_dialog, null);
        embuilder.setView(view);


        embuilder.setPositiveButton("살고싶어요..", new DialogInterface.OnClickListener() {
            final long time2=System.currentTimeMillis();
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(((time2 - emergency_time) / 1000.0) > 86400 || emergency_time == 0){          //24시간 지나거나 디폴트 값이면 비상탈출 가능
                    mDatabase.child("users").child(uid).child("emergency_time").setValue(time2);        //지금 시간으로 갱신
                    replaceFragment(FlightFailureFragment.newInstance(today,set_arr_time, set_dpt_time,set_todo,set_id));
                    onDestroy();
                }
                else{
                    Toast.makeText(getContext(), "비상탈출한 지 24시간이 지나지 않았습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        embuilder.setNegativeButton("조금 더 해볼래요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "비상탈출을 취소하였습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        embuilder.show();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof OnTimePickListener){
            onTimePickListener=(OnTimePickListener) context;

        }else{
            throw new RuntimeException(context.toString()+"must iple");
        }
    }
    @Override
    public void onDetach(){
        super.onDetach();
        onTimePickListener=null;
    }
}