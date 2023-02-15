package com.example.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivityLoginBinding;
import com.example.careroutine.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setLoginViewModel(loginViewModel);

//        해당 객체가 액티비티의 라이프사이클을 참조하고 데이터가 변경되면 refresh
//        binding.setLifecycleOwner(this);
//        loginViewModel =
//            new ViewModelProvider(this,
//            ViewModelProvider.AndroidViewModelFactory
//            .getInstance(getApplication()))
//            .get(LoginViewModel.class);
    }
}