package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.careroutine.R;

public class MainActivity extends AppCompatActivity {
    Button btnAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAlarm = findViewById(R.id.btn_alarm);
        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AlarmSettingActivity.class);
                startActivity(intent);
            }
        });

        Button btnSearchDrug = findViewById(R.id.btn_search_drug);
        btnSearchDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchDrugActivity();
            }
        });
    }

    public void openSearchDrugActivity() {
        Intent intent = new Intent(this, SearchDrugActivity.class);
        startActivity(intent);
    }
}
