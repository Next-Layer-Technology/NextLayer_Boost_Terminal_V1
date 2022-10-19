
package com.sis.clighteningboost.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.sis.clighteningboost.R;

public class NodeIDResponse extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_idresponse);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
            }
        },2000);
    }
}