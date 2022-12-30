package com.sis.clighteningboost;

import android.app.Application;

import com.sis.clighteningboost.Activities.BaseActivity;
import com.sis.clighteningboost.Interface.TimeOutListener;

import java.util.Timer;
import java.util.TimerTask;

public class MyApp extends Application {
    private TimeOutListener listener;
    public void startUserTimeOut() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                listener.onSessionTimeOut();
            }
            // 10 min set for timeout.
        },600000);
    }

    public void registerSessionListener(TimeOutListener timeOutListener) {
            this.listener = listener;
    }
}
