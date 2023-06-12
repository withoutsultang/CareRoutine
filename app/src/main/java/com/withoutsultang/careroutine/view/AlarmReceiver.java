package com.withoutsultang.careroutine.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 알람이 울렸을 때 실행될 동작을 여기에 정의합니다.
        Toast.makeText(context, "알람이 울렸습니다!", Toast.LENGTH_SHORT).show();
    }
}
