package com.example.stop_and_flight;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.ArrayList;

public class Accessibility extends AccessibilityService {

    String getString;
    ArrayList<AppInfo> appname;
    Intent intent2;
    private static final String TAG ="tag" ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getString=intent.getStringExtra("flight");
        appname=(ArrayList<AppInfo>)intent.getSerializableExtra("applist");

//        ArrayList<String> list = (ArrayList<String>) intent.getSerializableExtra("applist");
//
//        int size=list.size();
//        for(int i=0;i<size;i++){
//            System.out.println("확인 앱 리스트 "+list.get(i));
//        }
        System.out.println(appname+"appname 확인 안에서 ");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        boolean denyApp = false;


        String currentActivityName = getClass().getSimpleName().trim();
        if(getString!=null&&getString.equals("1")){
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {      //디바이스 화면의 상태가 변화할때마다 이벤트 감지
                System.out.println("확인 activity name1 "+currentActivityName);
//                appname=intent2.getStringExtra("appname");

                    int size=appname.size();
                    for(int i=0;i<size;i++){
                        System.out.println(appname+"appname 확인 안에서 22");
                        //허용앱에서 선택을 햇을때 event packagename 이 안바뀌거나 getName한게 안받아지거나
                    if (appname.get(i).getName()!="com.example.stop_and_flight"&&appname.get(i).getName()!=null&&appname.get(i).getName().equals(event.getPackageName().toString())) {                //허용앱이 아닌앱과 패키지명이 equal 할때 앱을 종료한다.
                        System.out.println("확인 : 패키지 네임 "+appname);

                        Toast.makeText(this.getApplicationContext(), event.getPackageName() + "앱이 실행되었습니다", Toast.LENGTH_LONG);
                        break;

                    }
                    else {
                        gotoflight();
                        System.out.println("확인 Catch Event Package Name :" + event.getPackageName());
                        System.out.println("확인 else문 안 ");

                    }
                    }
            }
            }
//                gotoflight();
//            Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
//            Log.e(TAG, "Catch Event TEXT : " + event.getText());
//            Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
//            Log.e(TAG, "Catch Event getSource : " + event.getSource());
//            Log.e(TAG, "=========================================================================");


    }

    @Override
    public void onInterrupt() {

    }


    //TODO: 이부분 Flight Activity가 실행되도록
    private void gotoflight(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.FLIGHTACTIVITY");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_FORWARD_RESULT
                | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        startActivity(intent);
        System.out.println("확인 다시 생성!");
    }
}