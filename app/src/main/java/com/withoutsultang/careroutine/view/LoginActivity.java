package com.withoutsultang.careroutine.view;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivityLoginBinding;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.withoutsultang.careroutine.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {

    private LoginViewModel viewModel;
    Button button;
    GoogleApiClient mGoogleApiClient;
    String SiteKey = "6Lc4wWkmAAAAAClfBmHw9Qsn4ulSJhPbiid_x8Pc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        viewModel = new LoginViewModel(this);

        binding.setViewModel(viewModel);


        button = findViewById(R.id.btnLogin);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafetyNet.SafetyNetApi.verifyWithRecaptcha(mGoogleApiClient, SiteKey)
                        .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                            @Override
                            public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                Status status = recaptchaTokenResult.getStatus();
                                if ((status != null) && status.isSuccess()) {
                                    // reCAPTCHA 서비스와 통신이 성공한 경우입니다.
                                    // 사용자가 CAPTCHA를 완료했다면 result.getTokenResult()를 사용하여 사용자 응답 토큰을 가져올 수 있습니다.
                                    viewModel.onClickLogin();
                                } else {
                                    // reCAPTCHA 서비스와 통신 중 오류가 발생한 경우입니다.
                                    // 오류를 적절히 처리하려면 상태 코드를 참조하세요.

                                    Toast.makeText(LoginActivity.this, "인증 실패", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(LoginActivity.this)
                .build();
        mGoogleApiClient.connect();


        viewModel.getNavigateToSignUpActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
            }
        });

        viewModel.getNavigateToFindAccountActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, FindAccountActivity.class));
                finish();
            }
        });

        viewModel.getNavigateToMainActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // Google Play 서비스와의 연결이 성공한 경우 호출됩니다.
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Google Play 서비스와의 연결이 일시적으로 중단된 경우 호출됩니다.
    }
}
