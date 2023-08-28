package com.sis.clighteningboost

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.sis.clighteningboost.Activities.BaseActivity
import com.sis.clighteningboost.Activities.MerchantLink
import dagger.hilt.android.HiltAndroidApp
import java.util.*

@HiltAndroidApp
class MyApp : Application(), ActivityLifecycleCallbacks {
    private var currentBaseActivity: BaseActivity? = null
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
    override fun onActivityStarted(activity: Activity) {
        currentBaseActivity = if (activity is BaseActivity) activity else null
        Log.d(this.javaClass.simpleName, "activity started")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.d(this.javaClass.simpleName, "activity resumed")
        currentBaseActivity = if (activity is BaseActivity) activity else null
    }

    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    override fun onActivityDestroyed(activity: Activity) {
        Log.d(this.javaClass.simpleName, "activity destroyed")
        currentBaseActivity = null
    }

    override fun onCreate() {
        super.onCreate()
        val observer = AppLifecycleObserver()
        ProcessLifecycleOwner.get().lifecycle.addObserver(observer)
        registerActivityLifecycleCallbacks(this)
    }

    internal inner class AppLifecycleObserver : DefaultLifecycleObserver {
        var timer = Timer()
        override fun onStart(owner: LifecycleOwner) { // app moved to foreground
            timer.cancel()
            timer = Timer()
            Log.d(this@MyApp.javaClass.simpleName, "timer cancelled")
        }

        override fun onStop(owner: LifecycleOwner) { // app moved to background
            Log.d(this@MyApp.javaClass.simpleName, "app moved to background")
            Log.d(this@MyApp.javaClass.simpleName, "timer started")
            timer.schedule(object : TimerTask() {
                override fun run() {
                    Log.d(this@MyApp.javaClass.simpleName, "on session timed out")
                    if (currentBaseActivity != null) {
                        currentBaseActivity!!.sp!!.clearAll()
                        currentBaseActivity!!.sp!!.saveStringValue("merchant_id", null)
                        currentBaseActivity!!.openActivity(MerchantLink::class.java)
                        currentBaseActivity!!.finish()
                    }
                }
            }, 30000)
        }
    }
}