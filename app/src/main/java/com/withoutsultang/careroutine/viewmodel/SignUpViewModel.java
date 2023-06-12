package com.withoutsultang.careroutine.viewmodel;

import android.app.AlertDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.careroutine.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.withoutsultang.careroutine.model.User;

public class SignUpViewModel extends ViewModel {
    public ObservableField<String> ePW = new ObservableField<>("");
    public ObservableField<String> eBirth = new ObservableField<>("");
    public ObservableField<String> eEmail = new ObservableField<>("");
    public ObservableField<String> eName = new ObservableField<>("");
    public ObservableField<Boolean> isTermsChecked = new ObservableField<>(false);

    private Context context;

    private static MutableLiveData<Boolean> navigateToLoginActivity = new MutableLiveData<>();

    public SignUpViewModel(Context context) {
        this.context = context;
    }

    //중복체크 버튼
    public void onClickVerify() {

        // 이메일 입력 확인
        String email = eEmail.get();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(context, "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이메일 형식 확인
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "올바른 이메일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean isEmailExists = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    if (user != null && email.equals(user.getEmail())) {
                        isEmailExists = true;
                        break;
                    }
                }

                if (isEmailExists) {
                    Toast.makeText(context, "이메일이 이미 존재합니다", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "이메일 사용 가능합니다", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 취소될 경우 처리할 내용 추가
            }
        });
    }

    public void onClickSignUp() {
        String password = ePW.get();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(context, "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 비밀번호 길이 확인
        if (password.length() < 8 || password.length() > 12) {
            Toast.makeText(context, "8 ~ 12자리의 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 비밀번호에 한글, 공백 확인
        if (password.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣\\s]+.*")) {
            Toast.makeText(context, "비밀번호에는 한글과 공백을 입력할 수 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }
        // 비밀번호에 영어, 숫자, 특수기호 확인
        if (!password.matches(".*[a-zA-Z0-9~!@#$%^&*()_+`\\-={}|\\[\\]:\";'<>?,./\\\\]+.*")) {
            Toast.makeText(context, "비밀번호는 영어 알파벳(대소문자 구별), 숫자, 특수기호만 입력 가능합니다", Toast.LENGTH_SHORT).show();
            return;
        }

        // 이름 입력 확인
        String name = eName.get();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(context, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 이름 길이 확인
        if (name.length() < 2) {
            Toast.makeText(context, "2글자 이상 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 이름에 한글 이외의 글자 확인
        if (!name.matches("^[가-힣]*$")) {
            Toast.makeText(context, "한글만 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 생년월일 입력 확인
        String birthday = eBirth.get();
        if (TextUtils.isEmpty(birthday)) {
            Toast.makeText(context, "생년월일을 입력하세요", Toast.LENGTH_SHORT).show();
            return;
        }
        // 생년월일 형식 확인
        if (!birthday.matches("\\d{4}/\\d{2}/\\d{2}")) {
            Toast.makeText(context, "올바른 생년월일 형식이 아닙니다", Toast.LENGTH_SHORT).show();
            return;
        }
        // 약관 동의 체크 확인

        Boolean isTermsCheckedValue = isTermsChecked.get();
        if (!isTermsCheckedValue) {
            // 약관 팝업 표시
            showTermsPopup(context);
        } else {
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
                            Toast.makeText(context, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            navigateToLoginActivity();
                        } else {
                            // 회원가입 실패 처리
                            Toast.makeText(context, "회원가입 실패", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public void showTermsPopup(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.terms_dialog, null);

        TextView tvTermsContent = view.findViewById(R.id.tvTermsContent);
        // 약관 내용을 설정하세요
        tvTermsContent.setText("약관 내용을 입력하세요");

        Button btnTermsAgree = view.findViewById(R.id.btnTermsAgree);
        btnTermsAgree.setOnClickListener(v -> {
            // 약관 동의 처리를 수행하세요
            isTermsChecked.set(true);
        });

        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static LiveData<Boolean> getNavigateToLoginActivity() {
        return navigateToLoginActivity;
    }

    public void navigateToLoginActivity() {
        navigateToLoginActivity.setValue(true);
    }
}
