package com.example.vnb.mec;

import android.app.Application;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class TaskApplication extends Application{

    private static String urlPath = "http://10.0.2.2:8001/";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(urlPath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }
}
