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

public class SignUpViewModel extends ViewModel {

    public ObservableField<String> eID = new ObservableField<>("");
    public ObservableField<String> ePW = new ObservableField<>("");
    public ObservableField<String> eBirth = new ObservableField<>("");
    public ObservableField<String> eEmail= new ObservableField<>("");
    public ObservableField<String> eName= new ObservableField<>("");

    private Context context;

    private static MutableLiveData<Boolean> navigateToLoginActivity = new MutableLiveData<>();

    public SignUpViewModel(Context context) {
        this.context = context;
    }

    public void onClickVerify() {
    }
    public void onClickSignUp() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("users");
        auth.createUserWithEmailAndPassword(eEmail.get(), ePW.get())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        String userId = auth.getCurrentUser().getUid();
                        User user = new User(eEmail.get(), ePW.get(), eName.get(), eBirth.get());
                        usersRef.child(userId).setValue(user);

                        // 회원가입 성공 처리
                        navigateToLoginActivity();
                    } else {
                        // 회원가입 실패 처리
                        Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static LiveData<Boolean> getNavigateToLoginActivity() {
        return navigateToLoginActivity;
    }

    public void navigateToLoginActivity() {
        navigateToLoginActivity.setValue(true);
    }
}
