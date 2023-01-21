package com.sis.clighteningboost;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

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
        }, 600000);
        stopObserver();
        startObserver();
    }

    public void stopObserver() {
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(observer);
    }

    public void startObserver() {
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);
    }

    public void registerSessionListener(TimeOutListener timeOutListener) {
        this.listener = timeOutListener;
    }

    private AppLifecycleObserver observer;

    @Override
    public void onCreate() {
        super.onCreate();
        observer = new AppLifecycleObserver();
    }


    class AppLifecycleObserver implements DefaultLifecycleObserver {

        @Override
        public void onStart(@NonNull LifecycleOwner owner) { // app moved to foreground
        }

        @Override
        public void onStop(@NonNull LifecycleOwner owner) { // app moved to background
            Log.d(this.getClass().getSimpleName(), "app moved to background");
            listener.onSessionTimeOut();
        }
    }
}
