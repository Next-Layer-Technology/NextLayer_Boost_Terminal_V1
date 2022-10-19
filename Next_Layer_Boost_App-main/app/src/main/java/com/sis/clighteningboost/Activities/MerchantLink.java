 package com.sis.clighteningboost.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sis.clighteningboost.Api.ApiClient;
import com.sis.clighteningboost.Api.ApiInterface;
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;
import com.sis.clighteningboost.Models.REST.MerchantData;
import com.sis.clighteningboost.Models.REST.MerchantLoginResp;
import com.sis.clighteningboost.R;
import com.sis.clighteningboost.Utills.GlobalState;
import com.sis.clighteningboost.Utills.SharedPreference;
import com.sis.clighteningboost.Utills.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.gusavila92.websocketclient.WebSocketClient;

 public class MerchantLink extends BaseActivity {

    ProgressDialog progressDialog;
    StaticClass st;
    SharedPreference sp;
    EditText tv_merchant_link,tv_merchant_link_pass;
    int mode=0;
    String merchantId="";
    boolean isConfirmMerchant=false;
     WebSocketClient webSocketClient;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_link_layout2);
        //createWebSocketClient();
        //String temp=String.format("%.2f", round(0.5,2));
        //Log.e("SHora1",temp);
        st = new StaticClass(this);
        progressDialog = new ProgressDialog(this);
        //progressDialog.setTitle("Finding Merchant");
        progressDialog.setMessage("Connecting....");
        progressDialog.setCancelable(false);
        tv_merchant_link = findViewById(R.id.tv_merchant_link);
        tv_merchant_link_pass = findViewById(R.id.tv_merchant_pass_link);
        sp = new SharedPreference(this, "local_data");
        //        if (sp.containKey("client_id")) {
//            openActivity(MerchantBoostTerminal.class);
//            finish();
//        }
//
//        if (sp.containKey("merchant_id")) {
//            tv_merchant_link.setText(sp.getStringValue("merchant_id"));
//        }
        findViewById(R.id.btn_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(view);
                String id = tv_merchant_link.getText().toString();
                String pass = tv_merchant_link_pass.getText().toString();
                if (id.length() == 0) {
                    goAlertDialogwithOneBTn(1,"","Please enter merchant id","Ok","");
                   // st.toast("Please enter merchant id");
                    return;
                }else if (pass.length() == 0){
                    goAlertDialogwithOneBTn(1,"","Please enter merchant password","Ok","");
                    // st.toast("Please enter merchant id");
                    return;

                }
                try {
                    findMerchant(id,pass);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        findViewById(R.id.btn_qr_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerIntentMerchantID();
            }
        });

        if(sp.getStringValue("merchant_id") != null && !sp.getStringValue("merchant_id").equalsIgnoreCase("") && sp.getStringValue("merchant_password") != null && !sp.getStringValue("merchant_password").equalsIgnoreCase("")){
            try {
                findMerchant(sp.getStringValue("merchant_id"),sp.getStringValue("merchant_password"));
            } catch (JSONException e) {
                progressDialog.dismiss();
                e.printStackTrace();
            }
        }

        createWebSocketClient();





    }
    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
//            uri = new URI("ws://98.226.215.246:5000");
            uri = new URI("wss://ws.bitstamp.net/");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                String json = getResources().getString(R.string.channel);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    webSocketClient.send(jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            Log.i("WebSocket", message);
                            //TextView textView = findViewById(R.id.animalSound);
                            //textView.setText(message);
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
            }
            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    private void scannerIntentMerchantID() {
        mode=1;
        IntentIntegrator qrScan;
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        String prompt = "Scan Client  ID";
        qrScan.setPrompt(prompt);
        qrScan.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else
            {
                if(mode==1){
                    merchantId=result.getContents();
                    if(merchantId.length()==0){st.toast("Please enter client id"); return;}
                    tv_merchant_link.setText(merchantId);
                    //findMerchant(merchantId);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void  findMerchant(final String id,final String password) throws JSONException {
        progressDialog.show();
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("user_id", id);
        paramObject.addProperty("password", password);
        Call<MerchantLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_Loging(paramObject);
        //Call<MerchantLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_Loging(id,password);
        call.enqueue(new Callback<MerchantLoginResp>() {
            @Override
            public void onResponse(Call<MerchantLoginResp> call, Response<MerchantLoginResp> response) {
                progressDialog.dismiss();
                if(response.body()!=null){
                    if (response.body().getMessage().equals("Successfully done")) {
                        MerchantData merchantData=new MerchantData();
                        merchantData=response.body().getMerchantData();
                        GlobalState.getInstance().setMainMerchantData(merchantData);
                        sp.saveStringValue("merchant_id", id);
                        sp.saveStringValue("accessToken", merchantData.getAccessToken());
                        sp.saveStringValue("refreshToken", merchantData.getRefreshToken());
                        sp.saveStringValue("merchant_password",password);
                        //openActivity(MainActivity.class);
                        //Remove2fa
                        //goTo2FaPasswordDialog( id,merchantData);
                        //sp.saveStringValue("merchant_id", merchantId);
                        openActivity(MainActivity.class);
                    }else {
                        progressDialog.dismiss();
                        goAlertDialogwithOneBTn(1,"","Invalid Merchant ID","Retry","");
                    }
                }
                else {
                    progressDialog.dismiss();
                    Log.e("Error:",response.toString());
                    goAlertDialogwithOneBTn(1,"","Client id or marchant id is wrong", "Retry","");
                }
            }

            @Override
            public void onFailure(Call<MerchantLoginResp> call, Throwable t) {
                progressDialog.dismiss();
                goAlertDialogwithOneBTn(1,"","Server Error","Retry","");
            }
        });

    }
    private void goTo2FaPasswordDialog(String merchantId, MerchantData merchantData) {
        final MerchantData merchantDatafinal=merchantData;
        final Dialog enter2FaPassDialog;
        enter2FaPassDialog=new Dialog(MerchantLink.this);
        enter2FaPassDialog.setContentView(R.layout.merchat_twofa_pass_lay);
        Objects.requireNonNull(enter2FaPassDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        enter2FaPassDialog.setCancelable(false);
        final EditText et_2Fa_pass=enter2FaPassDialog.findViewById(R.id.taskEditText);
        final Button btn_confirm=enter2FaPassDialog.findViewById(R.id.btn_confirm);
        final Button btn_cancel=enter2FaPassDialog.findViewById(R.id.btn_cancel);
        final ImageView iv_back=enter2FaPassDialog.findViewById(R.id.iv_back);
        et_2Fa_pass.setHint(R.string.entertwofapassword);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter2FaPassDialog.dismiss();

            }
        });
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clodeSoftKeyBoard();
                String task = String.valueOf(et_2Fa_pass.getText());
                if(task.isEmpty()){
                    goAlertDialogwithOneBTn(1,"","Enter 2FA Password","Ok","");

                }else {
                    if(task.equals(merchantDatafinal.getPassword())){
                        enter2FaPassDialog.dismiss();
                        isConfirmMerchant=true;
                       // GlobalState.getInstance().setMerchantConfirm(true);
                        final Dialog goAlertDialogwithOneBTnDialog;
                        goAlertDialogwithOneBTnDialog=new Dialog(MerchantLink.this);
                        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout);
                        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        goAlertDialogwithOneBTnDialog.setCancelable(false);
                        final TextView alertTitle_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertTitle);
                        final TextView alertMessage_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertMessage);
                        final Button yesbtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.yesbtn);
                        final Button nobtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.nobtn);
                        yesbtn.setText("Next");
                        nobtn.setText("");
                        alertTitle_tv.setText("");
                        alertMessage_tv.setText("Merchant Id Confirmed");
                            nobtn.setVisibility(View.GONE);
                            alertTitle_tv.setVisibility(View.GONE);
                        yesbtn.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View v) {
                                goAlertDialogwithOneBTnDialog.dismiss();
                                sp.saveStringValue("merchant_id", merchantId);
                                openActivity(MainActivity.class);
                            }
                        });
                        nobtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goAlertDialogwithOneBTnDialog.dismiss();
                            }
                        });
                        goAlertDialogwithOneBTnDialog.show();


                    }else {
                        goAlertDialogwithOneBTn(1,"","Incorrect Password","Retry","");

                    }
                }

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enter2FaPassDialog.dismiss();
            }
        });
        enter2FaPassDialog.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //statusCheck();
    }
    public void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        } else {
            if (ContextCompat.checkSelfPermission(MerchantLink.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MerchantLink.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                getCurrentLocation();
            }
            //st.toast("Enabled");

        }
    }
    private void buildAlertMessageNoGps() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, You Have To Enable It...")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                }).show();

    }
    public void getCurrentLocation() {

        final LocationRequest locationRequest = new LocationRequest();

        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(MerchantLink.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {

                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MerchantLink.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latestlocationIndex = locationResult.getLocations().size() - 1;
                            double latitude = locationResult.getLocations().get(latestlocationIndex).getLatitude();
                            double longitude = locationResult.getLocations().get(latestlocationIndex).getLongitude();
                            sp.saveStringValue("latitude", String.valueOf(latitude));
                            sp.saveStringValue("longitude", String.valueOf(longitude));
                        }

                    }
                }, Looper.myLooper());

    }
    private void getBitCoinValue() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Call<CurrentAllRate> call = retrofit.create(ApiInterface.class).getBitCoin();

        call.enqueue(new Callback<CurrentAllRate>() {
            @Override
            public void onResponse(Call<CurrentAllRate> call, Response<CurrentAllRate> response) {

                CurrentAllRate bitRate2=new CurrentAllRate();
                bitRate2=response.body();
                GlobalState.getInstance().setCurrentAllRate(bitRate2);
                CurrentAllRate tem=GlobalState.getInstance().getCurrentAllRate();

            }

            @Override
            public void onFailure(Call<CurrentAllRate> call, Throwable t) {
                st.toast("bitcoin fail: " + t.getMessage());
            }
        });
    }
    @Override
    public void onBackPressed() {



        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog=new Dialog(MerchantLink.this);
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout);
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        goAlertDialogwithOneBTnDialog.setCancelable(false);
        final TextView alertTitle_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertTitle);
        final TextView alertMessage_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertMessage);
        final Button yesbtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.yesbtn);
        final Button nobtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.nobtn);
        yesbtn.setText("Yes");
        nobtn.setText("No");
        alertTitle_tv.setText("");
        alertTitle_tv.setVisibility(View.GONE);
        alertMessage_tv.setText("Are you sure you want to exit?");
        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAlertDialogwithOneBTnDialog.dismiss();
                finishAffinity();
                finish();
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAlertDialogwithOneBTnDialog.dismiss();
            }
        });
        goAlertDialogwithOneBTnDialog.show();
    }
    public void clodeSoftKeyBoard(){
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
    private void goAlertDialogwithOneBTn(int i, String alertTitleMessage,String alertMessage,String alertBtn1Message,String alertBtn2Message) {
        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog=new Dialog(MerchantLink.this);
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout);
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        goAlertDialogwithOneBTnDialog.setCancelable(false);
        final TextView alertTitle_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertTitle);
        final TextView alertMessage_tv=goAlertDialogwithOneBTnDialog.findViewById(R.id.alertMessage);
        final Button yesbtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.yesbtn);
        final Button nobtn=goAlertDialogwithOneBTnDialog.findViewById(R.id.nobtn);
        yesbtn.setText(alertBtn1Message);
        nobtn.setText(alertBtn2Message);
        alertTitle_tv.setText(alertTitleMessage);
        alertMessage_tv.setText(alertMessage);
        if(i==1){
            nobtn.setVisibility(View.GONE);
            alertTitle_tv.setVisibility(View.GONE);
        }else {

        }

        yesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAlertDialogwithOneBTnDialog.dismiss();
            }
        });
        nobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAlertDialogwithOneBTnDialog.dismiss();
            }
        });
        goAlertDialogwithOneBTnDialog.show();

    }
}
