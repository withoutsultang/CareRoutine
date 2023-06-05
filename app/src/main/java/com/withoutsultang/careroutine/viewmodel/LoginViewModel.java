package com.withoutsultang.careroutine.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.careroutine.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withoutsultang.careroutine.model.User;

public class LoginViewModel extends ViewModel {

    private FirebaseDatabase database;

    DatabaseReference userRef = database.getReference("users");

    public ObservableField<String> eID = new ObservableField<>("");
    public ObservableField<String> ePW = new ObservableField<>("");

    private Context context;

    public LoginViewModel() {
        database = FirebaseDatabase.getInstance();
    }

    private static MutableLiveData<Boolean> navigateToSignUpActivity = new MutableLiveData<>();
    private static MutableLiveData<Boolean> navigateToFindPwActivity = new MutableLiveData<>();
    private static MutableLiveData<Boolean> navigateToFindIdActivity = new MutableLiveData<>();

    public LoginViewModel(Context context) {
        this.context = context;
    }

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
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        context.startActivity(intent);

                    } else {
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
    }

    public void loginG(Activity activity) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.getString(com.example.careroutine.R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(activity, gso);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        activity.startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void firebaseAuthWithGoogle(GoogleSignInAccount account, OnCompletionListener<AuthResult> onCompletionListener){
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        database.signInWithCredential(credential)
                .addOnCompleteListener(onCompletionListener);
    }

    public void signUp() {
        navigateToSignUpActivity();
    }

    public void findId() {navigateToFindIdActivity();}

    public void findPw() {
        navigateToFindPwActivity();
    }

    public static LiveData<Boolean> getNavigateToSignUpActivity() {
        return navigateToSignUpActivity;
    }

    public static LiveData<Boolean> getNavigateToFindIdActivity() {
        return navigateToFindIdActivity;
    }

    public static LiveData<Boolean> getNavigateToFindPwActivity() {
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