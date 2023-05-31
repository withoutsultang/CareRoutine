package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.withoutsultang.careroutine.model.User;

public class FindAccountViewModel extends ViewModel {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference userRef = database.getReference("users");

    //xml에서 데이터바인딩한 변수 관찰가능한 변수로 생성
    public ObservableField<String> femlname = new ObservableField<>("");
    public ObservableField<String> femlbirth = new ObservableField<>("");
    public ObservableField<String> fpwemail = new ObservableField<>("");
    public ObservableField<String> fpwcode = new ObservableField<>("");
    public ObservableField<String> nowpw = new ObservableField<>("");
    public ObservableField<String> changepw = new ObservableField<>("");

    private Context context;

    public FindAccountViewModel(Context context) {
        this.context = context;
    }

    public void onClickSearch(String name, String birthday) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild("name").equalTo(name);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null && user.getBirth().equals(birthday)) {
                        String toastMessage = ("이메일: " + user.getEmail());
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                Toast.makeText(context, "이메일을 찾을수 없습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String toastMessage = ("데이터베이스 에러: " + error.getMessage());
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onClickSend() {
    }

    public void onClickCheckCode() {

    }

    public void onClickCheckpw() {

    }

}