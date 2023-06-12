package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withoutsultang.careroutine.view.AlarmSettingActivity;
import com.withoutsultang.careroutine.view.SearchDrugActivity;

public class SearchDrugViewModel extends ViewModel {

    Context context;


    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public ObservableField<String> txtSearchD = new ObservableField<>("");
    public ObservableField<String> txtD = new ObservableField<>("");

    public SearchDrugViewModel(Context context) {
        this.context = context;
    }

    public void onClickD() {
        String searchQuery = txtSearchD.get();
        DatabaseReference drugsRef = database.getReference("drugs");

        drugsRef.orderByKey().equalTo(searchQuery).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String drugName = snapshot.getKey();
                    String drugValue = snapshot.getValue(String.class);
                    txtD.set("약 이름: " + drugName + "\n약 세부사항: " + drugValue);
                    startAlarmSettingActivity(drugName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 처리할 오류가 있을 경우의 코드
            }

            private void startAlarmSettingActivity(String drugName) {
                Intent intent = new Intent(context, AlarmSettingActivity.class);
                intent.putExtra("drugName", drugName);
                context.startActivity(intent);
            }
        });
    }
}