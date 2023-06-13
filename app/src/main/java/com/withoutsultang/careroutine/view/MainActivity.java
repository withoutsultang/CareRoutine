package com.withoutsultang.careroutine.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.careroutine.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    Button btnAlarm;
    private LinearLayout alarmMorningInfo, alarmAfternoonInfo, alarmEveningInfo;
    private DatabaseReference databaseRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Firebase Realtime Database 초기화
        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("alarms");
        } else {
            // 사용자가 로그인하지 않은 경우에 대한 처리 (예: 로그인 페이지로 이동, 오류 메시지 표시 등)
        }


        alarmMorningInfo = findViewById(R.id.alarm_morning_info);
        alarmAfternoonInfo = findViewById(R.id.alarm_afternoon_info);
        alarmEveningInfo = findViewById(R.id.alarm_evening_info);

        btnAlarm = findViewById(R.id.button15);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
                startActivity(intent);
            }
        });

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


    private void showAlarmInfo(DataSnapshot dataSnapshot) {
        String time = dataSnapshot.getKey();

        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
            String key = childSnapshot.getKey();

            if (key != null) {
                String drugName = childSnapshot.child("drugName").getValue(String.class);
                int hour = childSnapshot.child("hour").getValue(Integer.class);
                int minute = childSnapshot.child("minute").getValue(Integer.class);

                TextView textView = createAlarmTextView(drugName, hour, minute);

                switch (time) {
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
    }

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
        return textView;
    }
}
