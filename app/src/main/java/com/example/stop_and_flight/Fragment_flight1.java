package com.example.stop_and_flight;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Fragment_flight1 extends Fragment {
    private CountDownTimer countDownTimer;
    private TextView count_txt;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        countDownTimer();
        countDownTimer.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flight1, container, false);

        Button emergencybutton = v.findViewById(R.id.button_emergency);
        Button appaccessbutton = v.findViewById(R.id.button_app);

        List<PackageInfo> packlist = new List<PackageInfo>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(@Nullable Object o) {
                return false;
            }

            @NonNull
            @Override
            public Iterator<PackageInfo> iterator() {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(@NonNull T[] a) {
                return null;
            }

            @Override
            public boolean add(PackageInfo packageInfo) {
                return false;
            }

            @Override
            public boolean remove(@Nullable Object o) {
                return false;
            }

            @Override
            public boolean containsAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(@NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends PackageInfo> c) {
                return false;
            }

            @Override
            public boolean removeAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(@NonNull Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public PackageInfo get(int index) {
                return null;
            }

            @Override
            public PackageInfo set(int index, PackageInfo element) {
                return null;
            }

            @Override
            public void add(int index, PackageInfo element) {

            }

            @Override
            public PackageInfo remove(int index) {
                return null;
            }

            @Override
            public int indexOf(@Nullable Object o) {
                return 0;
            }
        // Inflate the layout for this fragment
        return v;
    }


    public void countDownTimer(){
        countDownTimer = new CountDownTimer(200000,1000) {          //첫번째 인자 : 총시간(제한시간) 두번째 인수: 몇초마다 타이머 작동
            @Override
            public void onTick(long millisUntilFinished) {
                count_txt=(TextView)getView().findViewById(R.id.count_txt);
                count_txt.setText(getTime());
            }

            @Override
            public void onFinish() {

            }
        };
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

    private String getTime(){
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int c_hour = calendar.get(Calendar.HOUR_OF_DAY);
        int c_min = calendar.get(Calendar.MINUTE);
        int c_sec = calendar.get(Calendar.SECOND);

        Calendar baseCal = new GregorianCalendar(year,month,day,c_hour,c_min,c_sec);        //현재날짜
        Calendar targetCal = new GregorianCalendar(year,month,day+2,0,0,0);  //비교대상날짜
        //지금은 현재날짜에서 +2일로 해놨는데 나중에 설정된 날짜 시간 받아와서 해야함

        long diffSec = (targetCal.getTimeInMillis() - baseCal.getTimeInMillis()) / 1000;        //비교대상 날짜-현재날짜를 1000분의 1초 단위로 하는게 gettimeinmills 그래서 1000으로 나눠줘야함
        long diffDays = diffSec / (24*60*60);

        targetCal.add(Calendar.DAY_OF_MONTH, (int)(-diffDays));

        int hourTime = (int)Math.floor((double)(diffSec/3600));
        int minTime = (int)Math.floor((double)(((diffSec - (3600 * hourTime)) / 60)));
        int secTime = (int)Math.floor((double)(((diffSec - (3600 * hourTime)) - (60 * minTime))));

        String hour = String.format("%02d", hourTime);
        String min = String.format("%02d", minTime);
        String sec = String.format("%02d", secTime);

        return year+"년"+month+"월"+ (day+2)+"일 까지 " + hour + " 시간 " +min + " 분 "+ sec + "초 남았습니다.";

    }

    private void ShowEmergencyMessage(View v) {
        AlertDialog.Builder embuilder = new AlertDialog.Builder(getActivity());
        embuilder.setTitle("손님!! 비상 탈출 하시겠습니까?");
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View view = factory.inflate(R.layout.emergency_dialog, null);
        embuilder.setView(view);
        embuilder.setPositiveButton("살고싶어요..", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "끔", Toast.LENGTH_SHORT).show();
            }
        });
        embuilder.setNegativeButton("조금 더 해볼래요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "안 끔", Toast.LENGTH_SHORT).show();
            }
        });
        embuilder.show();
    }




    private void installedApplist(List<String> applist) {
        List<PackageInfo> packList = getActivity().getPackageManager().getInstalledPackages(0);
        PackageInfo packInfo = null;
        for (int i=0; i < packList.size(); i++)
        {
            packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
            {
                applist.add(packInfo.packageName);
            }
        }
    }

      @Override
    public void onDestroy() {
        super.onDestroy();
        try{
            countDownTimer.cancel();
        } catch (Exception e) {}
        countDownTimer=null;
    }
}