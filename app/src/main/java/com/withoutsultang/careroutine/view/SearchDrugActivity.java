package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.careroutine.R;
import com.example.careroutine.databinding.ActivityLoginBinding;
import com.example.careroutine.databinding.ActivitySearchDrugBinding;
import com.withoutsultang.careroutine.viewmodel.LoginViewModel;
import com.withoutsultang.careroutine.viewmodel.SearchDrugViewModel;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class SearchDrugActivity extends AppCompatActivity {

    private SearchDrugViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);

        ActivitySearchDrugBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_search_drug);

        viewModel = new SearchDrugViewModel(this);

        binding.setViewModel(viewModel);

        binding.result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();
                    String drugName = binding.result.getText().toString();
                    intent.putExtra("result", drugName);
                    setResult(RESULT_OK, intent);
                    finish();
                }

        });

    }

}