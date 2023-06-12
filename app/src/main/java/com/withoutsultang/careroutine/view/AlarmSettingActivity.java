package com.withoutsultang.careroutine.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.careroutine.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private String selectedDrug;

    private ChildEventListener childEventListener = new ChildEventListener() {
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
    };

    // 사용자의 고유 ID를 저장할 변수 추가
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_setting);

        // Firebase Realtime Database 초기화
        databaseRef = FirebaseDatabase.getInstance().getReference().child("users").child("alarms");

        // 사용자의 고유 ID 가져오기
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getUid();
        }

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
                databaseRef.child(userId).removeValue();
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
        databaseRef.child(userId).child("morning").addChildEventListener(childEventListener);
        databaseRef.child(userId).child("afternoon").addChildEventListener(childEventListener);
        databaseRef.child(userId).child("evening").addChildEventListener(childEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // ChildEventListener 해제
        databaseRef.child(userId).child("morning").removeEventListener(childEventListener);
        databaseRef.child(userId).child("afternoon").removeEventListener(childEventListener);
        databaseRef.child(userId).child("evening").removeEventListener(childEventListener);
    }

    // SearchDrugActivity에서 선택한 값을 가져와서 txtDrug에 표시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            selectedDrug = data.getStringExtra("drugName"); // 선택된 약 이름을 변수에 저장
            txtDrug.setText(selectedDrug); // 선택된 약 이름을 텍스트뷰에 표시
        }
    }

    // Firebase 데이터베이스에 알림 추가
    private void addAlarm(String time) {
        if (selectedDrug == null) {
            Toast.makeText(this, "약을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getCurrentHour();
        int minute = timePicker.getCurrentMinute();

        String key = databaseRef.child(time).push().getKey();
        if (key != null) {
            Map<String, Object> alarmValues = new HashMap<>();
            alarmValues.put("drugName", selectedDrug); // 선택된 약 이름 사용
            alarmValues.put("hour", hour);
            alarmValues.put("minute", minute);

            databaseRef.child(time).child(key).updateChildren(alarmValues);
        }
    }

    // Firebase 데이터베이스에서 알림 삭제
    private void deleteAlarm(String time) {
        databaseRef.child(userId).child(time).removeValue();
    }

    // Firebase 데이터베이스에서 가져온 알림 정보를 화면에 표시
    private void showAlarmInfo(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        if (key != null) {
            String drugName = dataSnapshot.child("drugName").getValue(String.class);
            int hour = dataSnapshot.child("hour").getValue(Integer.class);
            int minute = dataSnapshot.child("minute").getValue(Integer.class);

            TextView textView = createTextView(drugName, hour, minute);

            if (key.equals("morning")) {
                alarmMorningInfo.addView(textView);
            } else if (key.equals("afternoon")) {
                alarmAfternoonInfo.addView(textView);
            } else if (key.equals("evening")) {
                alarmEveningInfo.addView(textView);
            }
        }
    }

    // Firebase 데이터베이스에서 삭제된 알림 정보를 화면에서 제거
    private void clearAlarmInfo(String key) {
        if (key != null) {
            if (key.equals("morning")) {
                alarmMorningInfo.removeAllViews();
            } else if (key.equals("afternoon")) {
                alarmAfternoonInfo.removeAllViews();
            } else if (key.equals("evening")) {
                alarmEveningInfo.removeAllViews();
            }
        }
    }

    // 알림 정보를 담은 TextView 생성
    private TextView createTextView(String drugName, int hour, int minute) {
        TextView textView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dpToPx(16));
        textView.setLayoutParams(params);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        textView.setText(drugName + " - " + hour + ":" + minute);
        textView.setTextColor(ContextCompat.getColor(this, R.color.black));

        return textView;
    }

    // dp를 px로 변환
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
