package com.withoutsultang.careroutine.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider.Factory;

import com.example.careroutine.BuildConfig;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withoutsultang.careroutine.view.FindAccountActivity;


public class LoginViewModel extends ViewModel {

    private FirebaseDatabase database;
    private static final int RC_SIGN_IN = 9001;

    DatabaseReference userRef = database.getReference("users");

    public ObservableField<String> eID = new ObservableField<>("");
    public ObservableField<String> ePW = new ObservableField<>("");

    private Context context;

    public LoginViewModel(Context context) {
        this.context = context;
        database = FirebaseDatabase.getInstance();
    }

    private MutableLiveData<Boolean> navigateToSignUpActivity = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToFindPwActivity = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToFindIdActivity = new MutableLiveData<>();

    public void login() {
        String id = eID.get();
        String pw = ePW.get();

        if (id == null || id.isEmpty()) {
            Toast.makeText(context, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (pw == null || pw.isEmpty()) {
            Toast.makeText(context, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, FindAccountActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
    }

    public void loginG(Context context) {
        Activity activity = (Activity) context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(com.example.careroutine.R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account, OnCompleteListener<AuthResult> onCompletionListener){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        FirebaseAuth.getInstance().signInWithCredential(credential)
                .addOnCompleteListener(onCompletionListener);
    }

    public void loginWithRecaptcha(Context context){
        Activity activity = (Activity) context;
        SafetyNet.getClient(activity).verifyWithRecaptcha(BuildConfig.RECAPTCHA_SITE_KEY)
                .addOnSuccessListener(activity, success ->{
                    login();
                })
                .addOnFailureListener(activity, e -> {
                    if (e instanceof ApiException) {
                        ApiException apiException = (ApiException) e;
                        // An error occurred when communicating with the reCAPTCHA service.
                        int statusCode = apiException.getStatusCode();
                        Toast.makeText(activity, "인증 실패", Toast.LENGTH_SHORT).show();
                    } else {
                        // A different, unknown type of error occurred.
                        Toast.makeText(activity, "인증 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void signUp() {
        navigateToSignUpActivity();
    }

    public void findId() {navigateToFindIdActivity();}

    public void findPw() {
        navigateToFindPwActivity();
    }

    public  LiveData<Boolean> getNavigateToSignUpActivity() {
        return navigateToSignUpActivity;
    }

    public  LiveData<Boolean> getNavigateToFindIdActivity() {
        return navigateToFindIdActivity;
    }

    public  LiveData<Boolean> getNavigateToFindPwActivity() {
        return navigateToFindPwActivity;
    }

    public void navigateToSignUpActivity() {
        navigateToSignUpActivity.setValue(true);
    }
    public void navigateToFindIdActivity() { navigateToFindIdActivity.setValue(true); }
    public void navigateToFindPwActivity() {
        navigateToFindPwActivity.setValue(true);
    }
}