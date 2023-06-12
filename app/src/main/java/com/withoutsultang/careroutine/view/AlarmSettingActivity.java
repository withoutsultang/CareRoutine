package com.withoutsultang.careroutine.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.careroutine.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AlarmSettingActivity extends AppCompatActivity {
    private TextView txtDrug;
    private TimePicker timePicker;
    private LinearLayout alarmMorningInfo, alarmAfternoonInfo, alarmEveningInfo;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        // Firebase Realtime Database 초기화
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child("alarms");

        // 뷰 요소 초기화
        txtDrug = findViewById(R.id.txtDrug);
        timePicker = findViewById(R.id.time_picker);
        alarmMorningInfo = findViewById(R.id.alarm_morning_info);
        alarmAfternoonInfo = findViewById(R.id.alarm_afternoon_info);
        alarmEveningInfo = findViewById(R.id.alarm_evening_info);

        // 약 검색 버튼 클릭 시 SearchDrugActivity로 이동
        Button btnSearchDrug = findViewById(R.id.btn_search_drug);
        btnSearchDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AlarmSettingActivity.this, SearchDrugActivity.class);
                startActivityForResult(intent, 1);
            }
        });



        // 전체 삭제 버튼 클릭 시 Firebase 데이터베이스에서 모든 알림 삭제
        Button btnDeleteAll = findViewById(R.id.btn_delete_all);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseRef.removeValue();
            }
        });

        // 아침 알림 추가 버튼 클릭 시 Firebase 데이터베이스에 알림 추가
        Button btnAddMorning = findViewById(R.id.btn_add_morning);
        btnAddMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm("morning");
            }
        });

        // 점심 알림 추가 버튼 클릭 시 Firebase 데이터베이스에 알림 추가
        Button btnAddAfternoon = findViewById(R.id.btn_add_afternoon);
        btnAddAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm("afternoon");
            }
        });

        // 저녁 알림 추가 버튼 클릭 시 Firebase 데이터베이스에 알림 추가
        Button btnAddEvening = findViewById(R.id.btn_add_evening);
        btnAddEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAlarm("evening");
            }
        });

        // 아침 알림 삭제 버튼 클릭 시 Firebase 데이터베이스에서 알림 삭제
        Button btnDeleteMorning = findViewById(R.id.btn_delete_morning);
        btnDeleteMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm("morning");
            }
        });

        // 점심 알림 삭제 버튼 클릭 시 Firebase 데이터베이스에서 알림 삭제
        Button btnDeleteAfternoon = findViewById(R.id.btn_delete_afternoon);
        btnDeleteAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm("afternoon");
            }
        });

        // 저녁 알림 삭제 버튼 클릭 시 Firebase 데이터베이스에서 알림 삭제
        Button btnDeleteEvening = findViewById(R.id.btn_delete_evening);
        btnDeleteEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm("evening");
            }
        });

        // Firebase Realtime Database에서 데이터 변경 감지 및 표시
        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, String previousChildName) {
                showAlarmInfo(snapshot);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, String previousChildName) {
                showAlarmInfo(snapshot);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                clearAlarmInfo(snapshot.getKey());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // SearchDrugActivity에서 선택한 값을 가져와서 txtDrug에 표시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String result = data.getStringExtra("mFlags");
                txtDrug.setText(result);
                // result에는 SecondActivity의 TextView의 텍스트가 저장되어 있습니다.
            }
        }
    }

    // Firebase 데이터베이스에 알림 추가
    private void addAlarm(String time) {
        String drugName = txtDrug.getText().toString().trim();
        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        String key = databaseRef.push().getKey();
        if (key != null) {
            Map<String, Object> alarmValues = new HashMap<>();
            alarmValues.put("drugName", drugName);
            alarmValues.put("hour", hour);
            alarmValues.put("minute", minute);

            databaseRef.child(time).child(key).updateChildren(alarmValues);
        }
    }

    // Firebase 데이터베이스에서 알림 삭제
    private void deleteAlarm(String time) {
        databaseRef.child(time).removeValue();
    }

    // Firebase 데이터베이스에서 가져온 알림 정보를 화면에 표시
    private void showAlarmInfo(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        if (key != null) {
            String drugName = dataSnapshot.child("drugName").getValue(String.class);
            int hour = dataSnapshot.child("hour").getValue(Integer.class);
            int minute = dataSnapshot.child("minute").getValue(Integer.class);

            TextView textView = createAlarmTextView(drugName, hour, minute);

            switch (key) {
                case "morning":
                    alarmMorningInfo.removeAllViews();
                    alarmMorningInfo.addView(textView);
                    break;
                case "afternoon":
                    alarmAfternoonInfo.removeAllViews();
                    alarmAfternoonInfo.addView(textView);
                    break;
                case "evening":
                    alarmEveningInfo.removeAllViews();
                    alarmEveningInfo.addView(textView);
                    break;
            }
        }
    }



    // 화면에 표시된 알림 정보를 삭제
    private void clearAlarmInfo(String time) {
        switch (time) {
            case "morning":
                alarmMorningInfo.removeAllViews();
                break;
            case "afternoon":
                alarmAfternoonInfo.removeAllViews();
                break;
            case "evening":
                alarmEveningInfo.removeAllViews();
                break;
        }
    }

    // 알림 정보를 담은 TextView 생성
    private TextView createAlarmTextView(String drugName, int hour, int minute) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        textView.setText(drugName + " - " + hour + ":" + minute);
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setPadding(8, 8, 8, 8);
//        textView.setBackground(ContextCompat.getDrawable(this, R.xml.stroke_icon));

        return textView;
    }

}
