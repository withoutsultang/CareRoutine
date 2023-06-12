package com.withoutsultang.careroutine.viewmodel;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LoginViewModel extends ViewModel {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference userRef = database.getReference("users");

    public ObservableField<String> email = new ObservableField<>("");
    public ObservableField<String> pw = new ObservableField<>("");

    private Context context;

    private static MutableLiveData<Boolean> navigateToSignUpActivity = new MutableLiveData<>();

    private static MutableLiveData<Boolean> navigateToFindAccountActivity = new MutableLiveData<>();

    private static MutableLiveData<Boolean> navigateToMainActivity = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        this.context = context;
    }


    public void onClickSignUp() {

        navigateToSignUpActivity();

    }

    public void onClickFindAccount() {

        navigateToFindAccountActivity();

    }


    public void onClickLogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String inputemail = email.get();
        String inputpw = pw.get();
        auth.signInWithEmailAndPassword(inputemail,inputpw).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser user = auth.getCurrentUser();
                if(user != null){
                    navigateToMainActivity();
                }
                else {
                    Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static LiveData<Boolean> getNavigateToSignUpActivity() {
        return navigateToSignUpActivity;
    }

    public void navigateToSignUpActivity() {
        navigateToSignUpActivity.setValue(true);
    }

    public static LiveData<Boolean> getNavigateToFindAccountActivity() {
        return navigateToFindAccountActivity;
    }

    public void navigateToFindAccountActivity() {
        navigateToFindAccountActivity.setValue(true);
    }

    public static LiveData<Boolean> getNavigateToMainActivity() {
        return navigateToMainActivity;
    }

    public void navigateToMainActivity() {
        navigateToMainActivity.setValue(true);
    }
}

