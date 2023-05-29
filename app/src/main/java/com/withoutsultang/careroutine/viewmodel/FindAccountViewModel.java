package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindAccountViewModel extends ViewModel {

    //파이어베이스 데이터베이스 사용 선언
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    //데이터베이스 path만 똑같이
    DatabaseReference userRef = database.getReference("users");

    //xml에서 데이터바인딩한 변수 관찰가능한 변수로 생성
    public ObservableField<String> fidemail = new ObservableField<>("");
    public ObservableField<String> fidbirth = new ObservableField<>("");

    //화면전환시 필요
    private Context context;

    //화면전환시 필요
    public FindAccountViewModel(Context context) {
        this.context = context;
    }

    public void onClickSearch() {
        /*String text1 = fidemail.get();
        String text2 = fidbirth.get();*/

        //파이어베이스 리얼타임데이터베이스
        /*userRef.child(text1).setValue(text2);*/

        /*if (text1 != null && text2 != null) {
            databaseReference.child(text1).setValue(text2);
        }*/
    }
}
