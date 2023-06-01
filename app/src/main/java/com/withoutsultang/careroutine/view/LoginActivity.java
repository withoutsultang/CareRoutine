package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.Toast;

import com.example.careroutine.databinding.ActivityLoginBinding;
import com.withoutsultang.careroutine.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
        LoginViewModel loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        ActivityLoginBinding binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);

        loginViewModel.getLoginResult().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String result) {
                Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        });

    }
}