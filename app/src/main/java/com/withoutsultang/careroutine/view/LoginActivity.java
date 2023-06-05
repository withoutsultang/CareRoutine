package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivityLoginBinding;
import com.withoutsultang.careroutine.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityLoginBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        viewModel = new LoginViewModel(this);

        binding.setViewModel(viewModel);

        viewModel.getNavigateToSignUpActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
            }
        });

        viewModel.getNavigateToFindIdActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, FindAccountActivity.class));
                finish();
            }
        });

        viewModel.getNavigateToFindPwActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, FindAccountActivity.class));
                finish();
            }
        });

    }
}