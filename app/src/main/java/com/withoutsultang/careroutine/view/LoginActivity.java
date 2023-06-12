import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.careroutine.R;
import com.google.android.gms.common.SignInButton;
import com.google.firebase.auth.FirebaseAuth;
import com.withoutsultang.careroutine.view.FindAccountActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editboxId, editboxPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editboxId = findViewById(R.id.editboxId);
        editboxPw = findViewById(R.id.editboxPw);

        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        TextView textViewFindId = findViewById(R.id.textView2);
        SignInButton btnLoginG = findViewById(R.id.btnLoginG);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
//                finish();
//            }
//        });

        textViewFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, FindAccountActivity.class));
                finish();
            }
        });

        btnLoginG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginG();
            }
        });
    }

    private void login() {
        String id = editboxId.getText().toString().trim();
        String pw = editboxPw.getText().toString().trim();

        if (TextUtils.isEmpty(id)) {
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(id, pw)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, FindAccountActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show();
                    }
                });
        // Perform login logic
        // ...

        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, FindAccountActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginG() {
        // Perform Google login logic
        // ...
    }
}
