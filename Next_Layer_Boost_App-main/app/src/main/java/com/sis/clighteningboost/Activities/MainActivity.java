package com.sis.clighteningboost.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sis.clighteningboost.Api.ApiClient;
import com.sis.clighteningboost.Api.ApiInterface;
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;
import com.sis.clighteningboost.Dialog.BoostNodeDialog;
import com.sis.clighteningboost.Models.REST.ClientData;
import com.sis.clighteningboost.Models.REST.ClientLoginResp;
import com.sis.clighteningboost.Models.REST.MerchantData;
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientResp;
import com.sis.clighteningboost.Models.TradeSocketResponse;
import com.sis.clighteningboost.R;
import com.sis.clighteningboost.Utills.GlobalState;
import com.sis.clighteningboost.Utills.SharedPreference;
import com.sis.clighteningboost.Utills.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends BaseActivity {

    ProgressDialog progressDialog;
    StaticClass st;
    SharedPreference sp;
    EditText tv_client_id;
    Button scanClientIDQr;
    MerchantData merchantData;
    TextView btcprice,tv_not_a_sovereign_partner_yet;
    int mode=0;
    String clientID="";
    Socket mSocket;
    boolean mOnMsgReceived=false;
    String mReceivingNodeId="";

    private WebSocketClient webSocketClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        btcprice=findViewById(R.id.btcprice);
        tv_not_a_sovereign_partner_yet=findViewById(R.id.tv_not_a_sovereign_partner_yet);
        getBitCoinValue();
        String udata="Not a sovereign partner yet?";
//        SpannableString content = new SpannableString(udata);
//        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
 //       tv_not_a_sovereign_partner_yet.setText(content);
//        tv_not_a_sovereign_partner_yet.setPaintFlags(tv_not_a_sovereign_partner_yet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tv_not_a_sovereign_partner_yet.setText(R.string.notasovereignpartneryet);
//        String htmlString="<u>Not a sovereign partner yet?</u>";
//        tv_not_a_sovereign_partner_yet.setText(Html.fromHtml(htmlString));
        scanClientIDQr=findViewById(R.id.btn_qr_scan_on_client_screen);
        tv_client_id = findViewById(R.id.tv_client_id);
       //TODO:Testing Pupose
        merchantData=GlobalState.getInstance().getMainMerchantData();
//        tv_client_id.setText(merchantData.getMerchant_maxboost());
        st = new StaticClass(this);
        findViewById(R.id.flashpay_btn).setOnClickListener(view -> {
                findNearbyClientforMerchant();
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connecting....");
        progressDialog.setCancelable(false);
        sp = new SharedPreference(this,"local_data");
        createSocketIOInstance();

        findViewById(R.id.btn_connect).setOnClickListener(view -> {
            hoverEffect(view);
            String id = tv_client_id.getText().toString();
            if(id.length()==0){
                st.toast("Please enter client id");
                return;}
            findClient(id);
        });

        findViewById(R.id.btn_register).setOnClickListener(view -> {
            hoverEffect(view);
            openActivity(Registration.class);
        });
        findViewById(R.id.btn_qr_scan_on_client_screen).setOnClickListener(view -> {
            hoverEffect(view);
           // String id = tv_client_id.getText().toString();
            scannerIntentClientID();
});

        createWebSocketClient();
    }

    public void findNearbyClientforMerchant() {
        progressDialog.show();
        String accessToken=sp.getStringValue("accessToken");
        String token="Bearer"+" "+accessToken;

        Log.d("Socket",mSocket.connected()+" "+mSocket.id());

        Call<MerchantNearbyClientResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_nearby_clients(token);
        call.enqueue(new Callback<MerchantNearbyClientResp>() {
            @Override
            public void onResponse(@NonNull Call<MerchantNearbyClientResp> call, @NonNull Response<MerchantNearbyClientResp> response) {

                if(response.isSuccessful()){
                    MerchantNearbyClientResp merchantNearbyClientResp = response.body();
                    if(merchantNearbyClientResp!=null && !merchantNearbyClientResp.getMerchantDataList().isEmpty()){
                        showPopup(new BoostNodeDialog(MainActivity.this,true,merchantNearbyClientResp.getMerchantDataList(),mSocket));
                    }else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(MainActivity.this,"Token Expired", Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MerchantNearbyClientResp> call, @NonNull Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void showPopup(BoostNodeDialog nodeDialog) {
        nodeDialog.isCancelable();
        FragmentManager fragmentManager = getSupportFragmentManager();
        nodeDialog.show(fragmentManager, TAG);
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
                            Gson gson = new Gson();
                            TradeSocketResponse tradeSocketResponse = gson.fromJson(message,TradeSocketResponse.class);
                            if(tradeSocketResponse != null && tradeSocketResponse.tradeData != null && tradeSocketResponse.tradeData.price > 0) {
                                btcprice.setText("$ " + String.valueOf(tradeSocketResponse.tradeData.price));
                            }
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

    private void scannerIntentClientID() {
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
                    clientID=result.getContents();
                    if(clientID.length()==0){st.toast("Please enter client id"); return;}
                    findClient(clientID);
                }
                }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }


    }

    private void findClient(final String id){

        progressDialog.show();
        String accessToken=sp.getStringValue("accessToken");
        String token="Bearer"+" "+accessToken;

        Call<ClientLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).client_Loging(token,id);

        call.enqueue(new Callback<ClientLoginResp>() {
            @Override
            public void onResponse(Call<ClientLoginResp> call, Response<ClientLoginResp> response) {
                if(response.body()!=null){
                    if(response.body().getMessage().equals("Successfully done")&&response.body().getClientData()!=null){
                        ClientData clientData=new ClientData();
                        clientData=response.body().getClientData();
                        GlobalState.getInstance().setMainClientData(clientData);
                        sp.saveStringValue("client_id",id);
                        sp.saveStringValue("client_name",clientData.getClient_name());
                        progressDialog.dismiss();
                        openActivity(MerchantBoostTerminal.class);
//                    finish();
                    }else{
                        goAlertDialogwithOneBTn(1,"","This Client ID has not been activated","Retry","");
                        progressDialog.dismiss();
                    }
                }else {
                    Log.e("Error:",response.toString());
                    st.toast(response.toString());
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ClientLoginResp> call, Throwable t) {
                progressDialog.dismiss();
                st.toast("Error: "+t.getMessage());
            }
        });


    }

    private void getBitCoinValue() {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://blockchain.info/").addConverterFactory(GsonConverterFactory.create()).build();
        Call<CurrentAllRate> call = retrofit.create(ApiInterface.class).getBitCoin();

        call.enqueue(new Callback<CurrentAllRate>() {
            @Override
            public void onResponse(Call<CurrentAllRate> call, Response<CurrentAllRate> response) {

                CurrentAllRate bitRate2=new CurrentAllRate();
                bitRate2=response.body();
                GlobalState.getInstance().setCurrentAllRate(bitRate2);
                CurrentAllRate tem=GlobalState.getInstance().getCurrentAllRate();
                if(tem!=null){
                    if(tem.getUSD()!=null){
                        if(tem.getUSD().getLast()!=null){
                            btcprice.setText("$ "+String.valueOf(tem.getUSD().getLast()));

                        }else {
                            btcprice.setText("Not Found");
                        }
                    }else {
                        btcprice.setText("Not Found");
                    }
                }else {
                    btcprice.setText("Not Found");
                }

            }

            @Override
            public void onFailure(Call<CurrentAllRate> call, Throwable t) {

                    btcprice.setText("Not Found");

                st.toast("bitcoin fail: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {

        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog=new Dialog(MainActivity.this);
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
                sp.clearAll();
                openActivity(MerchantLink.class);
                finishAffinity();
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
    private void goAlertDialogwithOneBTn(int i, String alertTitleMessage,String alertMessage,String alertBtn1Message,String alertBtn2Message) {
        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog=new Dialog(MainActivity.this);
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
    private Emitter.Listener onServerConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e("Response", "onServerConnected");
        }
    };

    private void createSocketIOInstance() {
        String accessToken=sp.getStringValue("accessToken");

        IO.Options options = new IO.Options();
        Map<String, List<String>> headers = new HashMap<>();
        String bearer = "Bearer "+accessToken;
        headers.put("Authorization", Arrays.asList(bearer));
        options.extraHeaders = headers;

        try {
            mSocket = IO.socket("https://realtime.nextlayer.live",options);
            mSocket.connect();
       } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
