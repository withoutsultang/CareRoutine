package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.example.careroutine.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {
    private EditText eID, ePW, eBirth, eEmail, eName;
    private CheckBox checkAgree;
    private Button checkId;
    private Button Registration;
    private Button checkEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        
        //컴포넌트 연결
        eID = findViewById(R.id.eID);
        ePW = findViewById(R.id.ePW);
        eBirth = findViewById(R.id.eBirth);
        eEmail = findViewById(R.id.eEmail);
        eName = findViewById(R.id.eName);
        checkAgree = findViewById(R.id.cbAgree);
        checkId = findViewById(R.id.btn_idCheck);
        checkEmail = findViewById(R.id.btn_emailCheck);
        Registration = findViewById(R.id.bRegistration);

        //FirebaseAuth 인스턴스 가져옴
        mAuth = FirebaseAuth.getInstance();

        //회원가입 버튼 이벤트
        Registration.setOnClickListener(v -> {
            //약관 동의 체크
            if (checkAgree.isChecked()) {
                String email = eEmail.getText().toString();
                String password = ePW.getText().toString();

                //입력 받은 이메일, 비밀번호로 Firebase에 등록 요청
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, task -> {
                            //등록 성공
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(SignUpActivity.this,
                                        "회원가입 성공: " + user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                            //등록 실패    
                            } else {
                                Toast.makeText(SignUpActivity.this,
                                        "회원가입 실패: " + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(SignUpActivity.this, "약관에 동의해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        //이메일 중복확인 버튼 이벤트
        checkEmail.setOnClickListener(v -> {
            //이메일 입력 칸이 빈칸인지 확인
            String email = eEmail.getText().toString().trim();
            if (!TextUtils.isEmpty(email)) {
                FirebaseUser user = mAuth.getCurrentUser();
                // Firebase에 동일한 이메일이 있는지 확인
                if (user.getEmail() == email) {
                    Toast.makeText(SignUpActivity.this, "이미 사용 중인 이메일입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "사용 가능한 이메일입니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SignUpActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

    } //onCreate()
} //Activity