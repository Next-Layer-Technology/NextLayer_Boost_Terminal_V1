package com.sis.clighteningboost.Utills;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.sis.clighteningboost.R;

import java.util.Random;

public class StaticClass {


    Context context;

    public StaticClass(Context context) {
        this.context = context;
    }

    public void toast(String text){

        Toast.makeText(context,text, Toast.LENGTH_SHORT).show();

    }

//    private void closeKeyboard() {
//        View view = context.getApplicationContext().getCurrentFocus();
//        if (view != null) {
//            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//        }
//    }

    public boolean isConnected(){

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;

    }

    public int getAnimation(int i){

        switch (i){

            case 0: return R.anim.blink_anim;
            case 1: return R.anim.bounce;
            case 2: return R.anim.fadein;
            case 3: return R.anim.lefttoright;
            case 4: return R.anim.righttoleft;
            default: return R.anim.zoomin;
        }

    }

    public Animation getOneInAll(int i){


        switch (i){

            case 0: return AnimationUtils.loadAnimation(context,R.anim.blink_anim);
            case 1: return AnimationUtils.loadAnimation(context,R.anim.bounce);
            case 2: return AnimationUtils.loadAnimation(context,R.anim.fadein);
            case 3: return AnimationUtils.loadAnimation(context,R.anim.fadeout);
            case 4: return AnimationUtils.loadAnimation(context,R.anim.j_blinking);
            case 5: return AnimationUtils.loadAnimation(context,R.anim.j_rotation);
            case 6: return AnimationUtils.loadAnimation(context,R.anim.lefttoright);
            case 7: return AnimationUtils.loadAnimation(context,R.anim.mixed_anim);
            case 8: return AnimationUtils.loadAnimation(context,R.anim.righttoleft);
            case 9: return AnimationUtils.loadAnimation(context,R.anim.rotate);
            case 10: return AnimationUtils.loadAnimation(context,R.anim.rotation);
            case 11: return AnimationUtils.loadAnimation(context,R.anim.sample_anim);
            case 12: return AnimationUtils.loadAnimation(context,R.anim.zoomin);
            case 13: return AnimationUtils.loadAnimation(context,R.anim.zoomout);

            default: return AnimationUtils.loadAnimation(context,R.anim.blink_anim);
        }

    }

    public Animation rightToLeft(){


        return AnimationUtils.loadAnimation(context,R.anim.righttoleft);
    }

    public Animation getSelectedAnim(int i){


        return AnimationUtils.loadAnimation(context.getApplicationContext(),getAnimation(i));

    }

    public int smallAnimations(){

        switch (new Random().nextInt(4)){

            case 0: return R.anim.blink_anim;
            case 1: return R.anim.bounce;
            case 2: return R.anim.fadein;
            case 3: return R.anim.lefttoright;
            default: return R.anim.righttoleft;

        }

    }

    public Animation randomAnimations(){

        return AnimationUtils.loadAnimation(context.getApplicationContext(),smallAnimations());

    }



}
