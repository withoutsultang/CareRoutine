package com.withoutsultang.careroutine.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withoutsultang.careroutine.model.User;

public class LoginViewModel extends ViewModel {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    public ObservableField<String> eID = new ObservableField<>("");
    public ObservableField<String> ePW = new ObservableField<>("");

    private Context context;

    private static MutableLiveData<Boolean> navigateToSignUpActivity = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        this.context = context;
    }


    public void onClickSignUp() {

        navigateToSignUpActivity();

    }

    public static LiveData<Boolean> getNavigateToSignUpActivity() {
        return navigateToSignUpActivity;
    }

    public void navigateToSignUpActivity() {
        navigateToSignUpActivity.setValue(true);
    }
}