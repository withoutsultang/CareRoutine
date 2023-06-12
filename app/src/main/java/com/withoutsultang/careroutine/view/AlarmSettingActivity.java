package com.withoutsultang.careroutine.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.careroutine.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmSettingActivity extends Activity {
    private LinearLayout alarmInfoContainer;
    private TimePicker timePicker;
    private List<String> alarmTimes;
    private int requestCode = 0;
    private Switch switchVibe;
    private Button btnDelete;

    // 채널 ID 정의
    private static final String CHANNEL_ID = "my_channel_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        alarmInfoContainer = findViewById(R.id.alarm_info_container);
        timePicker = findViewById(R.id.time_picker);
        Button btnAdd = findViewById(R.id.btn_add);
        Button btnSave = findViewById(R.id.btn_save);
        switchVibe = findViewById(R.id.switch_vibe);
        btnDelete = findViewById(R.id.btn_delete);

        alarmTimes = new ArrayList<>();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarmInfo();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarms();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllAlarms();
            }
        });
    }

    private void addAlarmInfo() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        String alarmTime = String.format("%02d:%02d", hour, minute);
        alarmTimes.add(alarmTime);

        Button alarmButton = new Button(this);
        alarmButton.setText(String.valueOf(alarmTime));
        alarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAlarmInfo(alarmTime);
            }
        });

        alarmInfoContainer.addView(alarmButton);
    }

    private void removeAlarmInfo(String alarmTime) {
        alarmTimes.remove(alarmTime);
        refreshAlarmInfo();
    }

    private void refreshAlarmInfo() {
        alarmInfoContainer.removeAllViews();
        for (String alarmTime : alarmTimes) {
            Button alarmButton = new Button(this);
            alarmButton.setText(alarmTime);
            alarmButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeAlarmInfo(alarmTime);
                }
            });
            alarmInfoContainer.addView(alarmButton);
        }
    }

    private void saveAlarms() {
        if (alarmTimes.isEmpty()) {
            Toast.makeText(this, "알람을 추가해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        // 알람 채널 생성
        createNotificationChannel();

        for (int i = 0; i < alarmTimes.size(); i++) {
            String alarmTime = alarmTimes.get(i);
            String[] timeParts = alarmTime.split(":");
            int hour = Integer.parseInt(timeParts[0]);
            int minute = Integer.parseInt(timeParts[1]);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);

            if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                // 알람 시간이 과거인 경우, 캘린더에 1일을 추가합니다.
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            int alarmId = requestCode++;
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // 알림 생성
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.drug)
                    .setContentTitle("알람")
                    .setContentText("알람이 울립니다.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setAutoCancel(true);

            // 진동 설정
            if (switchVibe.isChecked()) {
                long[] vibrationPattern = {0, 500, 200, 500}; // 진동 패턴: 지연, 진동, 지연, 진동...
                builder.setVibrate(vibrationPattern);
            }

            // 알림 등록
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(alarmId, builder.build());

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Toast.makeText(this, "알람이 저장되었습니다.", Toast.LENGTH_SHORT).show();
    }

    // 알림 채널 생성
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AlarmChannel";
            String description = "Channel for alarm notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    private void deleteAllAlarms() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        for (int i = 0; i < alarmTimes.size(); i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }

        alarmTimes.clear();
        refreshAlarmInfo();

        Toast.makeText(this, "모든 알람이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
