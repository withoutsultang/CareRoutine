package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivitySignUpBinding;
import com.withoutsultang.careroutine.viewmodel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액티비티의 레이아웃을 데이터바인딩하여 연결
        ActivitySignUpBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

        //뷰모델 변수 생성
        viewModel = new SignUpViewModel(this);

        //레이아웃 변수 viewModel과 액티비티의 viewModel을 바인딩
        binding.setSignUpViewModel(viewModel);

        SignUpViewModel.getNavigateToLoginActivity().observe(this, navigate -> {
            if (navigate) {
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            }
        });
    }
}