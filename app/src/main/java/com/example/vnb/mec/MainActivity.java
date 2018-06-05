package com.example.vnb.mec;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
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

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {

    private Button startButton;
    private Socket mSocket;
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


        startButton = (Button) findViewById(R.id.start_button);

        TaskApplication app = (TaskApplication) getApplication();
        mSocket = app.getSocket();

        mSocket.on("finish task", taskFinish);
        mSocket.connect();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mSocket.disconnect();
        mSocket.off("finish task", taskFinish);

    }
    Thread closeActivity = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                for (int i = 0;i < 5; ++ i){
                    JSONObject task = new JSONObject();
                    try {
                        task.put("task_id",i);
                        task.put("time",5000);
                        task.put("start_time", System.currentTimeMillis());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("new task",task);
                    Thread.sleep(2000);
                }
            } catch (Exception e) {
                e.getLocalizedMessage();
            }
        }
    });

    public void start(){
        closeActivity.start();
    }

    private Emitter.Listener taskFinish = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            JSONObject data = (JSONObject) args[0];

            final Integer taskID;
            final Long startTime;
            try {
                taskID = data.getInt("task_id");
                startTime = data.getLong("start_time");
            } catch (JSONException e) {
                return;
            }

            final Long finishTime = System.currentTimeMillis() - startTime;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mTextMessage.append("task_id: " + taskID.toString() + "  ");
                    mTextMessage.append("start_time: " + startTime + "  time: " + finishTime.toString() + "\n");
                }
            });

        }
    };

//    public void getRequest() {
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder().build();
//        Request request = new Request.Builder().url(urlPath).get().build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println(e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                System.out.println(response.body().string());
//
//            }
//        });
//    }
//
//    public void postRequest() {
//        final long sendTime = System.currentTimeMillis();
//
//        OkHttpClient okHttpClient = new OkHttpClient();
//        RequestBody requestBody = new FormBody.Builder().build();
//        Request request = new Request.Builder().url(urlPath).post(requestBody).build();
//        Call call = okHttpClient.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                final long receiveTime = System.currentTimeMillis();
//                System.out.println(receiveTime - sendTime);
//
//                }
//        });
//    }

}
