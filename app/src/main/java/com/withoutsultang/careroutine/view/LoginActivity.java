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
                                    // Indicates communication with reCAPTCHA service was
                                    // successful. Use result.getTokenResult() to get the
                                    // user response token if the user has completed
                                    // the CAPTCHA.
                                    viewModel.onClickLogin();
                                } else {
                                    // An error occurred while communicating with the
                                    // reCAPTCHA service. Refer to the status code to
                                    // handle the error appropriately.

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

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
