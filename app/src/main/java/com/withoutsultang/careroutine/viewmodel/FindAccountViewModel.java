package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindAccountViewModel extends ViewModel {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    public ObservableField<String> fidemail = new ObservableField<>("");
    public ObservableField<String> fidbirth = new ObservableField<>("");

    private Context context;

    public FindAccountViewModel(Context context) {
        this.context = context;
    }

    public void onClickSearch() {
        String text1 = fidemail.get();
        String text2 = fidbirth.get();

        userRef.child(text1).setValue(text2);

        /*if (text1 != null && text2 != null) {
            databaseReference.child(text1).setValue(text2);
        }*/
    }
}
