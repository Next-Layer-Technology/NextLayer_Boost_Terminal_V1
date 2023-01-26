package com.sis.clighteningboost.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.sis.clighteningboost.RPC.CreateInvoice
import com.sis.clighteningboost.RPC.Invoice
import com.sis.clighteningboost.utils.AppConstants
import com.sis.clighteningboost.utils.GlobalState.Companion.instance
import com.sis.clighteningboost.utils.SharedPreference
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*

open class BaseActivity : AppCompatActivity() {
    @JvmField
    var TAG = "CLightBetaLog"

    open var sp: SharedPreference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        sp = SharedPreference(this, "local_data")
        //   startService(new Intent(this, MyService.class));
    }

    fun getBitMapFromHex(hex: String?): Bitmap {
        val multiFormatWriter = MultiFormatWriter()
        var bitMatrix: BitMatrix? = null
        try {
            bitMatrix = multiFormatWriter.encode(hex, BarcodeFormat.QR_CODE, 600, 600)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }

    fun parseJSONForCreatInvocie(jsonString: String): CreateInvoice {
        val response = jsonString
        val gson = Gson()
        val type = object : TypeToken<CreateInvoice?>() {}.type
        val createInvoice = gson.fromJson<CreateInvoice>(jsonString, type)
        instance!!.createInvoice = createInvoice
        return createInvoice
    }

    fun parseJSONForConfirmPayment(jsonString: String): Invoice {
        val gson = Gson()
        val type = object : TypeToken<Invoice?>() {}.type
        val invoice = gson.fromJson<Invoice>(jsonString, type)
        instance!!.invoice = invoice
        return invoice
    }

    fun hoverEffect(view: View?) {
//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view,"alpha",1.0f,0.1f);
//        fadeOut.setDuration(80);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view,"alpha",0.1f,1.0f);
//        fadeIn.setDuration(80);
//        animatorSet.play(fadeIn).after(fadeOut);
//        animatorSet.start();
    }

    fun openActivity(c: Class<*>?) {
        startActivity(Intent(this, c))
    }

    fun getUsdFromBtc(btc: Double): Double {
        var ret = 0.0
        ret = if (instance!!.currentAllRate != null) {
            Log.e("btcbefore", btc.toString())
            val btcRate = instance!!.currentAllRate!!.uSD!!.last!!
            val priceInUSD = btcRate * btc
            Log.e("btcaftertousd", priceInUSD.toString())
            priceInUSD
        } else {
            0.0
        }
        return ret
    }

    fun getBtcFromUsd(usd: Double): Double {
        var ret = 0.0
        ret = if (instance!!.currentAllRate != null) {
            Log.e("usdbefore", usd.toString())
            val btcRatePerDollar = 1 / instance!!.currentAllRate!!.uSD!!.last!!
            val priceInBTC = btcRatePerDollar * usd
            Log.e("usdaftertobtc", priceInBTC.toString())
            priceInBTC
        } else {
            0.0
        }
        return ret
    }

    fun getTaxOfBTC(btc: Double): Double {
        var taxamount = 0.0
        if (instance!!.tax != null) {
            val t = instance!!.tax
            var taxprcntBTC = instance!!.tax!!.taxpercent / 100
            taxprcntBTC = taxprcntBTC * btc
            //            double taxprcntUSD=GlobalState.Companion.getInstance().getTax().getTaxpercent()/100;
//            taxprcntUSD=1*taxprcntUSD;
            taxamount = taxprcntBTC
        } else {
            taxamount = 0.0
        }
        return taxamount
    }

    fun getTaxOfUSD(usd: Double): Double {
        var taxamount = 0.0
        if (instance!!.tax != null) {
            var taxprcntUSD = instance!!.tax!!.taxpercent / 100
            taxprcntUSD = usd * taxprcntUSD
            taxamount = taxprcntUSD
        } else {
            taxamount = 0.0
        }
        return taxamount
    }

    fun mSatoshoToBtc(msatoshhi: Double): Double {
        val msatoshiToSatoshi = msatoshhi / AppConstants.satoshiToMSathosi
        return msatoshiToSatoshi / AppConstants.btcToSathosi
    }

    open fun excatFigure(value: Double): String? {
        val d = BigDecimal(value.toString())
        return d.toPlainString()
    }

    val unixTimeStamp: String
        get() {
            val tsLong = System.currentTimeMillis() / 1000
            return tsLong.toString()
        }

    override fun onPause() {
        super.onPause()
        // Toast.makeText(this,"pause",Toast.LENGTH_SHORT).show();
    }

    override fun onResume() {
        super.onResume()
        // Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
    }

    fun hideSoftKeyBoard() {
        ViewCompat.getWindowInsetsController(window.decorView)!!.hide(WindowInsetsCompat.Type.ime())
    }

    companion object {
        fun roundDouble(value: Double, places: Int): Double {
            var value = value
            require(places >= 0)
            val factor = Math.pow(10.0, places.toDouble()).toLong()
            value = value * factor
            val tmp = Math.round(value)
            return tmp.toDouble() / factor
        }

        @JvmStatic
        fun drawableToBitmap(drawable: Drawable): Bitmap? {
            var bitmap: Bitmap? = null
            if (drawable is BitmapDrawable) {
                val bitmapDrawable = drawable
                if (bitmapDrawable.bitmap != null) {
                    return bitmapDrawable.bitmap
                }
            }
            bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
                Bitmap.createBitmap(
                    1,
                    1,
                    Bitmap.Config.ARGB_8888
                ) // Single color bitmap will be created of 1x1 pixel
            } else {
                Bitmap.createBitmap(
                    drawable.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
            }
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }

        @JvmStatic
        fun toBooleanDefaultIfNull(bool: Boolean?): Boolean {
            return if (bool == null) false else bool
        }

        @JvmStatic
        fun round(value: Double, places: Int): Double {
            var value = value
            require(places >= 0)
            val factor = Math.pow(10.0, places.toDouble()).toLong()
            value = value * factor
            val tmp = Math.round(value)
            return tmp.toDouble() / factor
        }

        fun roundUSD(value: Double, places: Int): Double {
            val deci = "%." + places + "f"
            return String.format(deci, value).toDouble()
        }

        @JvmStatic
        fun isValidEmail(target: CharSequence?): Boolean {
            return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
        }

        @JvmStatic
        fun getDateFromUTCTimestamp2(mTimestamp: Long, mDateFormate: String?): String? {
            var date: String? = null
            try {
                val cal = Calendar.getInstance(TimeZone.getTimeZone("CST"))
                cal.timeInMillis = mTimestamp * 1000L
                date = DateFormat.format(mDateFormate, cal.timeInMillis).toString()
                val formatter = SimpleDateFormat(mDateFormate)
                formatter.timeZone = TimeZone.getTimeZone("CST")
                val value = formatter.parse(date)
                val dateFormatter = SimpleDateFormat(mDateFormate)
                dateFormatter.timeZone = TimeZone.getDefault()
                date = dateFormatter.format(value)
                return date
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date
        }
    }
}