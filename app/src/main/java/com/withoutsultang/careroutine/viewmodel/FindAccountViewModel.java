package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
    }

    public void onClickSend() {

    }

    public void onClickCheckCode() {

    }

    public void onClickCheckpw() {

    }

}
