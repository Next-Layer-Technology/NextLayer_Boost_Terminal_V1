package com.sis.clighteningboost.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Patterns;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sis.clighteningboost.Interface.TimeOutListener;
import com.sis.clighteningboost.MyApp;
import com.sis.clighteningboost.RPC.CreateInvoice;
import com.sis.clighteningboost.Utills.GlobalState;
import com.sis.clighteningboost.RPC.Invoice;
import com.sis.clighteningboost.RPC.Tax;
import com.sis.clighteningboost.Utills.AppConstants;
import com.sis.clighteningboost.Utills.SharedPreference;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
public class BaseActivity extends AppCompatActivity  {
    String TAG="CLightBetaLog";
    public SharedPreference sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        sp = new SharedPreference(this,"local_data");
     //   startService(new Intent(this, MyService.class));
    }
    public Bitmap getBitMapFromHex(String hex) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        BitMatrix bitMatrix = null;
        try {
            bitMatrix = multiFormatWriter.encode(hex, BarcodeFormat.QR_CODE, 600, 600);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
        return  bitmap;

    }
    public CreateInvoice parseJSONForCreatInvocie(String jsonString) {
        String response=jsonString;
        Gson gson = new Gson();
        Type type = new TypeToken<CreateInvoice>(){}.getType();
        CreateInvoice createInvoice = gson.fromJson(jsonString, type);
        GlobalState.getInstance().setCreateInvoice(createInvoice);
        return createInvoice;
    }
    public Invoice parseJSONForConfirmPayment(String jsonString) {
        String response=jsonString;
        Gson gson = new Gson();
        Type type = new TypeToken<Invoice>(){}.getType();
        Invoice invoice = gson.fromJson(response, type);
        GlobalState.getInstance().setInvoice(invoice);
        return invoice;
    }
    public static double roundDouble(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
    public void hoverEffect(View view){
//        AnimatorSet animatorSet = new AnimatorSet();
//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(view,"alpha",1.0f,0.1f);
//        fadeOut.setDuration(80);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view,"alpha",0.1f,1.0f);
//        fadeIn.setDuration(80);
//        animatorSet.play(fadeIn).after(fadeOut);
//        animatorSet.start();
    }
    public  void openActivity(Class c){
        startActivity(new Intent(this,c));
    }
    public static boolean toBooleanDefaultIfNull(Boolean bool) {
        if (bool == null)
            return false;
        else return bool.booleanValue();
    }
    public  double getUsdFromBtc(double btc) {
        double ret=0.0;
        if(GlobalState.getInstance().getCurrentAllRate()!=null)
        {
            Log.e("btcbefore",String.valueOf(btc));
            double btcRate=GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
            double  priceInUSD=btcRate*btc;
            Log.e("btcaftertousd",String.valueOf(priceInUSD));
            ret=priceInUSD;
        }
        else
        {
            ret=0.0;
        }

        return  ret;
    }
    public  double getBtcFromUsd(double usd) {
        double ret=0.0;
        if(GlobalState.getInstance().getCurrentAllRate()!=null)
        {
            Log.e("usdbefore",String.valueOf(usd));
            double btcRatePerDollar=1/GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
            double  priceInBTC=btcRatePerDollar*usd;
            Log.e("usdaftertobtc",String.valueOf(priceInBTC));
            ret=priceInBTC;
        }
        else
        {
            ret=0.0;
        }

        return  ret;
    }
    public  double getTaxOfBTC(double btc) {
        double taxamount=0.0;

        if(GlobalState.getInstance().getTax()!=null)
        {

            Tax t=GlobalState.getInstance().getTax();

            double taxprcntBTC=GlobalState.getInstance().getTax().getTaxpercent()/100;
            taxprcntBTC=taxprcntBTC*btc;
//            double taxprcntUSD=GlobalState.getInstance().getTax().getTaxpercent()/100;
//            taxprcntUSD=1*taxprcntUSD;
            taxamount=taxprcntBTC;
        }
        else
        {
            taxamount=0.0;
        }

        return  taxamount;
    }
    public  double getTaxOfUSD(double usd) {
        double taxamount=0.0;

        if(GlobalState.getInstance().getTax()!=null)
        {



            double taxprcntUSD=GlobalState.getInstance().getTax().getTaxpercent()/100;
            taxprcntUSD=usd*taxprcntUSD;
            taxamount=taxprcntUSD;
        }
        else
        {
            taxamount=0.0;
        }

        return  taxamount;
    }
    public double mSatoshoToBtc(double msatoshhi) {
        double msatoshiToSatoshi=msatoshhi/ AppConstants.satoshiToMSathosi;
        double satoshiToBtc=msatoshiToSatoshi/AppConstants.btcToSathosi;
        return satoshiToBtc;
    }
    public String excatFigure(double value) {
        BigDecimal d = new BigDecimal(String.valueOf(value));

        return  d.toPlainString();
    }
    public  String getUnixTimeStamp() {
        Long tsLong = System.currentTimeMillis()/1000;
        String uNixtimeStamp=tsLong.toString();
        return  uNixtimeStamp;
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public static double roundUSD(double value, int places) {
        String deci="%."+places+"f";
        double tem=Double.parseDouble(String.format(deci,value));
        return tem;
    }
    @Override
    protected void onPause() {
        super.onPause();
       // Toast.makeText(this,"pause",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();

    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static String getDateFromUTCTimestamp2(long mTimestamp, String mDateFormate) {
        String date = null;
        try {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("CST"));
            cal.setTimeInMillis(mTimestamp * 1000L);
            date = DateFormat.format(mDateFormate, cal.getTimeInMillis()).toString();

            SimpleDateFormat formatter = new SimpleDateFormat(mDateFormate);
            formatter.setTimeZone(TimeZone.getTimeZone("CST"));
            Date value = formatter.parse(date);

            SimpleDateFormat dateFormatter = new SimpleDateFormat(mDateFormate);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            date = dateFormatter.format(value);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }


    public void hideSoftKeyBoard(){
        ViewCompat.getWindowInsetsController(getWindow().getDecorView()).hide(WindowInsetsCompat.Type.ime());
    }


}
