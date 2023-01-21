package com.sis.clighteningboost;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.sis.clighteningboost.Activities.BaseActivity;
import com.sis.clighteningboost.Activities.MerchantLink;

import java.util.Timer;
import java.util.TimerTask;

public class MyApp extends Application implements Application.ActivityLifecycleCallbacks {

    private BaseActivity currentActivity;

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = (BaseActivity) activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = (BaseActivity) activity;

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;

    }


    @Override
    public void onCreate() {
        super.onCreate();
        AppLifecycleObserver observer = new AppLifecycleObserver();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(observer);
    }


    class AppLifecycleObserver implements DefaultLifecycleObserver {
        Timer timer = new Timer();

        @Override
        public void onStart(@NonNull LifecycleOwner owner) { // app moved to foreground
            timer.cancel();
        }

        @Override
        public void onStop(@NonNull LifecycleOwner owner) { // app moved to background
            Log.d(this.getClass().getSimpleName(), "app moved to background");

            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Log.d(this.getClass().getSimpleName(), "on session timed out");
                    currentActivity.sp.clearAll();
                    currentActivity.sp.saveStringValue("merchant_id", null);
                    currentActivity.openActivity(MerchantLink.class);
                    currentActivity.finish();
                }
                // 1 min set for timeout.
            }, 60000);



        }
    }
}
