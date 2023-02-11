package com.example.careroutine;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView id = findViewById(R.id.textView37);
        TextView pw = findViewById(R.id.textView38);
        Button btn_test = findViewById(R.id.button15);

        btn_test.setOnClickListener(new View.OnClickListener(){
                @Override
                        public void onClick(View v) {
                    StringBuilder jsonHtml = new StringBuilder();
                    new Thread(() -> {
                    try {
                        URL phpUrl = new URL("http://13.124.123.123/testdb.php");
                        HttpURLConnection conn = (HttpURLConnection) phpUrl.openConnection();
                        if (conn != null) {
                            conn.setConnectTimeout(10000);
                            conn.setUseCaches(false);
                            if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                while (true) {
                                    String line = br.readLine();
                                    if (line == null)
                                        break;
                                    jsonHtml.append(line + "\n");

                                }
                                br.close();
                            }
                            conn.disconnect();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject jObject = new JSONObject(jsonHtml.toString());

                        String getid = jObject.get("test_table").toString();
                        String getpw = jObject.get("test_table").toString();

                        id.post(new Runnable() {
                            @Override
                            public void run() {
                                id.setText(getid);
                            }
                        });
                        pw.post(new Runnable() {
                            @Override
                            public void run() {
                                pw.setText(getpw);
                            }
                        });



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }).start(); }

            });
        }
    }
