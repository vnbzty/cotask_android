package com.example.vnb.mec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button getButton;
    private Button postButton;

    private static String urlPath = "http://10.0.2.2:8080/";

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        getButton = (Button) findViewById(R.id.get_button);
        postButton = (Button) findViewById(R.id.post_button);

        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new getRequest().execute((Void) null);
                getRequest();
            }
        });

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new postRequest().execute((Void) null);
                    postRequest();
            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    public void getRequest() {

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(urlPath).get().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response.body().string());

            }
        });
    }

    public void postRequest() {
        final long sendTime = System.currentTimeMillis();

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().build();
        Request request = new Request.Builder().url(urlPath).post(requestBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final long receiveTime = System.currentTimeMillis();
                System.out.println(receiveTime - sendTime);

                }
        });
    }
//    public static class getRequest extends AsyncTask<Void, Void, String>{
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                URL url = new URL(urlPath);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");// 提交模式
//                // conn.setConnectTimeout(10000);//连接超时 单位毫秒
//                // conn.setReadTimeout(2000);//读取超时 单位毫秒
//                httpURLConnection.connect();
//                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                String line;
//                StringBuilder result = new StringBuilder();
//                while ((line = in.readLine()) != null) {
//                    result.append(line);
//                }
//                return result.toString();
//
//            } catch (Exception e) {
//                return "FAILED: NET: " + e.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(final String result) {
//
//            System.out.println(result);
//
//        }
//    }

//    public static class postRequest extends AsyncTask<Void, Void, String>{
//
//        private Date sendTime;
//        private Date receiveTime;
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            sendTime = Calendar.getInstance().getTime();
//            try {
//                URL url = new URL(urlPath);
//                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("POST");// 提交模式
//                // conn.setConnectTimeout(10000);//连接超时 单位毫秒
//                // conn.setReadTimeout(2000);//读取超时 单位毫秒
//                httpURLConnection.setDoOutput(true);
//                httpURLConnection.setDoInput(true);
//                String param = "";
//                PrintWriter printWriter = new PrintWriter(httpURLConnection.getOutputStream());
//                printWriter.write(param);
//                printWriter.flush();
//                //开始获取数据
//                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
//                String line;
//                StringBuilder result = new StringBuilder();
//                while ((line = in.readLine()) != null) {
//                    result.append(line);
//                }
//                receiveTime = Calendar.getInstance().getTime();
//                return result.toString();
//            } catch (Exception e) {
//                return "FAILED: NET: " + e.toString();
//            }
//        }
//
//        @Override
//        protected void onPostExecute(final String result) {
//
//            System.out.println(sendTime);
//            System.out.println(receiveTime);
//
//        }
//    }

}
