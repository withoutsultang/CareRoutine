package com.withoutsultang.careroutine.viewmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.withoutsultang.careroutine.view.FindAccountActivity;

@SuppressLint("StaticFieldLeak")
public class FindAccountViewModel extends ViewModel {

    //파이어베이스 데이터베이스 사용 선언
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    //데이터베이스 path만 똑같이
    DatabaseReference userRef = database.getReference("users");

    //xml에서 데이터바인딩한 변수 관찰가능한 변수로 생성
    public ObservableField<String> fidemail = new ObservableField<>("");

    public ObservableField<String> fidbirth = new ObservableField<>("");

    public ObservableField<String> fpwemail = new ObservableField<>("");

    public ObservableField<String> fpwcode = new ObservableField<>("");

    public ObservableField<String> currentpw = new ObservableField<>("");

    public ObservableField<String> changepw = new ObservableField<>("");
    //화면전환시 필요
    private Context context;

    //화면전환시 필요
    public FindAccountViewModel(Context context) {
        this.context = context;
    }

    public void onClickSearch() {

    }

    public void onClickSend(){
        String email = fpwemail.get();
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "이메일 인증 코드가 성공적으로 전송되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "이메일 인증 코드 전송 실패.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickCodeCheck() {
        String pwCode = fpwcode.get();
        firebaseAuth.verifyPasswordResetCode(pwCode)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "이메일 인증 성공.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "유효하지 않은 인증 코드입니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void onClickPwCheck() {
        String pwCode = fpwcode.get();
        String changePw = changepw.get();
        firebaseAuth.confirmPasswordReset(pwCode,changePw)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "비밀번호가 재설정되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "비밀번호 재설정에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
