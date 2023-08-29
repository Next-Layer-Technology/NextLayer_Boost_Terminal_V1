package com.sis.clighteningboost.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.widget.Toast
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.sis.clighteningboost.R
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import java.util.*

class StaticClass(var context: Context) {
    fun toast(text: String?) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }//we are connected to a network

    //    private void closeKeyboard() {
    //        View view = context.getApplicationContext().getCurrentFocus();
    //        if (view != null) {
    //            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
    //            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    //        }
    //    }
    val isConnected: Boolean
        get() {
            var connected = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            connected =
                if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
                ) {
                    //we are connected to a network
                    true
                } else false
            return connected
        }

    fun getAnimation(i: Int): Int {
        return when (i) {
            0 -> R.anim.blink_anim
            1 -> R.anim.bounce
            2 -> R.anim.fadein
            3 -> R.anim.lefttoright
            4 -> R.anim.righttoleft
            else -> R.anim.zoomin
        }
    }

    fun getOneInAll(i: Int): Animation {
        return when (i) {
            0 -> AnimationUtils.loadAnimation(context, R.anim.blink_anim)
            1 -> AnimationUtils.loadAnimation(context, R.anim.bounce)
            2 -> AnimationUtils.loadAnimation(context, R.anim.fadein)
            3 -> AnimationUtils.loadAnimation(context, R.anim.fadeout)
            4 -> AnimationUtils.loadAnimation(context, R.anim.j_blinking)
            5 -> AnimationUtils.loadAnimation(context, R.anim.j_rotation)
            6 -> AnimationUtils.loadAnimation(context, R.anim.lefttoright)
            7 -> AnimationUtils.loadAnimation(context, R.anim.mixed_anim)
            8 -> AnimationUtils.loadAnimation(context, R.anim.righttoleft)
            9 -> AnimationUtils.loadAnimation(context, R.anim.rotate)
            10 -> AnimationUtils.loadAnimation(context, R.anim.rotation)
            11 -> AnimationUtils.loadAnimation(context, R.anim.sample_anim)
            12 -> AnimationUtils.loadAnimation(context, R.anim.zoomin)
            13 -> AnimationUtils.loadAnimation(context, R.anim.zoomout)
            else -> AnimationUtils.loadAnimation(context, R.anim.blink_anim)
        }
    }

    fun rightToLeft(): Animation {
        return AnimationUtils.loadAnimation(context, R.anim.righttoleft)
    }

    fun getSelectedAnim(i: Int): Animation {
        return AnimationUtils.loadAnimation(context.applicationContext, getAnimation(i))
    }

    fun smallAnimations(): Int {
        return when (Random().nextInt(4)) {
            0 -> R.anim.blink_anim
            1 -> R.anim.bounce
            2 -> R.anim.fadein
            3 -> R.anim.lefttoright
            else -> R.anim.righttoleft
        }
    }

    fun randomAnimations(): Animation {
        return AnimationUtils.loadAnimation(context.applicationContext, smallAnimations())
    }
}

fun Bitmap.rotateBitmap(degrees: Float = 90f): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}