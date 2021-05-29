package com.example.stop_and_flight;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;

import androidx.annotation.RequiresApi;

//알람을 받아서 filght activity를 실행
public class AlarmReceiver extends BroadcastReceiver {
    //PowerManager.WakeLock 빈객체 선언한다.
    private static PowerManager.WakeLock sCpuWakeLock;
    private static WifiManager.WifiLock sWifiLock;
    private static ConnectivityManager manager;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        // 절전모드로 와이파이 꺼지는것을 방지
        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        sWifiLock = wifiManager.createWifiLock("wifilock");
        sWifiLock.setReferenceCounted(true);
        sWifiLock.acquire();

        // 시스템에서 powermanager 받아옴
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

        // 객체의 제어레벨 설정
        sCpuWakeLock = pm.newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                        PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.ON_AFTER_RELEASE, "app:alarm");

        //acquire 함수를 실행하여 앱을 깨운다. cpu 를 획득한다
        sCpuWakeLock.acquire();

        manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

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

