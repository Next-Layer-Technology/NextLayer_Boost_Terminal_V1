package com.sis.clighteningboost.Services

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.sis.clighteningboost.Services.MyService.TimeDisplay
import com.sis.clighteningboost.Services.MyService
import android.widget.Toast
import java.lang.UnsupportedOperationException
import java.util.*

class MyService : Service() {
    private val mHandler = Handler() //run on another Thread to avoid crash
    private var mTimer: Timer? = null //timer handling
    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        if (mTimer != null) // Cancel if already existed
            mTimer!!.cancel() else mTimer = Timer() //recreate new
        mTimer!!.scheduleAtFixedRate(TimeDisplay(), 0, notify.toLong()) //Schedule task
    }

    override fun onDestroy() {
        super.onDestroy()
        mTimer!!.cancel() //For Cancel Timer
        Log.e("statusServices", "Service is Destroyed")
        Toast.makeText(this, "Service is Destroyed", Toast.LENGTH_SHORT).show()
    }

    //class TimeDisplay for handling task
    internal inner class TimeDisplay : TimerTask() {
        override fun run() {
            // run on another thread
            mHandler.post { // display toast
                Log.e("statusServices", "Service is running")
                Toast.makeText(this@MyService, "Service is running", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val notify = 5000 //interval between two services(Here Service run every 5 Minute)
    }
}