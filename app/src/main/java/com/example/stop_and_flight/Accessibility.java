package com.example.stop_and_flight;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

public class Accessibility extends AccessibilityService {

    String getString;
    private static final String TAG ="tag" ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
       getString=intent.getParcelableExtra("flight");

       System.out.println(getString+"getstring 확인 안에서 ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        boolean denyApp = false;
        System.out.println("확인 ---------------------accessibility----------- ");

        try {
            if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {      //디바이스 화면의 상태가 변화할때마다 이벤트 감지
                System.out.println(getString+"확인 on인지 아닌지");
                if(getString=="on"){
                CharSequence packagename = "";        //TODO:이 변수에 허용앱리스트 넣고 반복문 돌려야함
                if (packagename.equals(event.getPackageName())) {                //허용앱이 아닌앱과 패키지명이 equal 할때 앱을 종료한다.
                    System.out.println("확인 : 패키지 네임 "+packagename);
                    Toast.makeText(this.getApplicationContext(), event.getPackageName() + "앱이 거부되었습니다", Toast.LENGTH_LONG);
                    gotoflight();
                } else {
                    gotoflight();
                    System.out.println("확인 else문 안 ");

                }
                }
            }
//                gotoflight();
           Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
           Log.e(TAG, "Catch Event TEXT : " + event.getText());
            Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
           Log.e(TAG, "Catch Event getSource : " + event.getSource());
           Log.e(TAG, "=========================================================================");

        }catch (NullPointerException e){
        }



    }

    @Override
    public void onInterrupt() {

    }

    @Override
    public boolean onUnbind(Intent intent) {

        return super.onUnbind(intent);
    }

    //TODO: 이부분 Flight Activity가 실행되도록
    private void gotoflight(){
        Intent intent = new Intent();
        intent.setAction("android.intent.action.FLIGHTACTIVITY");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }
}
