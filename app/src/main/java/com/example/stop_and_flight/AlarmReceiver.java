package com.example.stop_and_flight;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

//알람을 받아서 filght activity를 실행
public class AlarmReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        System.out.println("진입성공 확인");
        try {
            intent = new Intent(context, FilghtActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, intent,
                    PendingIntent.FLAG_ONE_SHOT);   //이 pending intent는 일회용이라는 플래그

//                    context.startForegroundService(intent);//??
            pi.send();

        } catch (PendingIntent.CanceledException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

