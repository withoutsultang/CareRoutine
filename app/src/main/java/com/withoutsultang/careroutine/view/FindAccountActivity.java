package com.withoutsultang.careroutine.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivityFindAccountBinding;
import com.withoutsultang.careroutine.viewmodel.FindAccountViewModel;

public class FindAccountActivity extends AppCompatActivity {

    //뷰모델 선언
    private FindAccountViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //액티비티의 레이아웃을 데이터바인딩하여 연결
        ActivityFindAccountBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_find_account);

        //뷰모델 변수 생성
        viewModel = new FindAccountViewModel(this);

        //레이아웃 변수 viewModel과 액티비티의 viewModel을 바인딩
        binding.setViewModel(viewModel);
    }
}