package com.sis.clighteningboost.Utills;

import android.app.Application;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketInstance extends Application {
    String mAccessToken;
    public SocketInstance(String accessToken){
        this.mAccessToken = accessToken;
    }
    private Socket mSocket;
    {
        IO.Options options = new IO.Options();
        Map<String, List<String>> headers = new HashMap<>();
        String bearer = "Bearer "+ mAccessToken;
        headers.put("Authorization", Arrays.asList(bearer));
        options.extraHeaders = headers;

        try {
            mSocket = IO.socket("https://realtime.nextlayer.live",options);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public Socket getSocket() {
        return mSocket;
    }
}
