package com.withoutsultang.careroutine.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableField;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.careroutine.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;

public class SearchDrugActivity extends AppCompatActivity {

    EditText edit;
    TextView text;
    Button button;
    XmlPullParser xpp;

    String key = "tY7etj%2FBiCBfKVQczOG1HSXUbmILW9sZqpF3V9X%2Bhb%2Fe1e5kAbGU%2BhSo6Gqr81CfgucWI5EMt0HQBaXqBKin0Q%3D%3D";
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_drug);

        edit = (EditText) findViewById(R.id.edit);
        text = (TextView) findViewById(R.id.result);
        button = (Button) findViewById(R.id.button);
    }

    //Button을 클릭했을 때 자동으로 호출되는 callback method....
    public void mOnClick(View v) {
        //Android 4.0 이상 부터는 네트워크를 이용할 때 반드시 Thread 사용해야 함
        new Thread(new Runnable() {
            @Override
            public void run() {
                data = getXmlData(); //아래 메소드를 호출하여 XML data를 파싱해서 String 객체로 얻어오기

                //UI Thread(Main Thread)를 제외한 어떤 Thread도 화면을 변경할 수 없기때문에
                //runOnUiThread()를 이용하여 UI Thread가 TextView 글씨 변경하도록 함
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        text.setText(data);
                    }
                });
            }
        }).start();
    }

    //XmlPullParser를 이용하여 Naver 에서 제공하는 OpenAPI XML 파일 파싱하기(parsing)
    String getXmlData() {
        StringBuffer buffer = new StringBuffer();

        String str = edit.getText().toString(); //EditText에 작성된 Text얻어오기
        String location = URLEncoder.encode(str); //한글의 경우 인식이 안되기에 utf-8 방식으로 encoding..

        String queryUrl = "http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/getDrbEasyDrugList?serviceKey=" + key + "&trustEntpName=" + location + "&pageNo=1&startPage=1&numOfRows=3";
        try {
            URL url = new URL(queryUrl); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream(); //url위치로 입력스트림 연결

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8")); //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        buffer.append("파싱 시작.,\n\n");
                        break;

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName(); //태그이름 가져오기

                        if (tag.equals("item")) ; //첫번째 검색결과
                        else if (tag.equals("entpName")) {
                            buffer.append("회사 이름 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("itemName")) {
                            buffer.append("제품 명 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("itemSeq")) {
                            buffer.append("제품 번호 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("efcyQesitm")) {
                            buffer.append("효능 : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        } else if (tag.equals("useMethodQesitm")) {
                            buffer.append(" : ");
                            xpp.next();
                            buffer.append(xpp.getText());
                            buffer.append("\n");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        break;
                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.equals("item")) buffer.append("\n");
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {

        }
        buffer.append("파싱 끝\n");
        return buffer.toString();
    }
}