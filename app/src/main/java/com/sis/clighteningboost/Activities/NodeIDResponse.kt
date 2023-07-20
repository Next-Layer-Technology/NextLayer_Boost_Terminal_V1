package com.sis.clighteningboost.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.sis.clighteningboost.R

class NodeIDResponse : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_node_idresponse)
        Handler().postDelayed({ finish() }, 2000)
    }
}