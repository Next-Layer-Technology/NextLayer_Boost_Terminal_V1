package com.sis.clighteningboost.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.sis.clighteningboost.Adapter.MerchantItemAdapter;
import com.sis.clighteningboost.Api.ApiClient;
import com.sis.clighteningboost.Api.ApiClientForNode;
import com.sis.clighteningboost.Api.ApiInterface;
import com.sis.clighteningboost.Api.ApiInterfaceForNodes;
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;
import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse;
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse;
import com.sis.clighteningboost.Models.NodesDataWithExecuteResponse;
import com.sis.clighteningboost.Models.REST.ClientData;
import com.sis.clighteningboost.Models.REST.ClientUpdateResp;
import com.sis.clighteningboost.Models.REST.FundingNode;
import com.sis.clighteningboost.Models.REST.FundingNodeListResp;
import com.sis.clighteningboost.Models.REST.MerchantData;
import com.sis.clighteningboost.Models.REST.MerchantListResp;
import com.sis.clighteningboost.Models.REST.MerchantUpdateResp;
import com.sis.clighteningboost.Models.REST.RoutingNode;
import com.sis.clighteningboost.Models.REST.RoutingNodeListResp;
import com.sis.clighteningboost.Models.REST.TransactionInfo;
import com.sis.clighteningboost.Models.REST.TransactionResp;
import com.sis.clighteningboost.Models.RPC.DecodePayBolt11;
import com.sis.clighteningboost.Models.RPC.InvoiceForPrint;
import com.sis.clighteningboost.Models.RPC.NodeLineInfo;
import com.sis.clighteningboost.Models.RPC.Pay;
import com.sis.clighteningboost.R;
import com.sis.clighteningboost.Utills.DateUtils;
import com.sis.clighteningboost.Utills.GlobalState;
import com.sis.clighteningboost.RPC.NetworkManager;
import com.sis.clighteningboost.Utills.AppConstants;
import com.sis.clighteningboost.Utills.JavaMailAPI;
import com.sis.clighteningboost.Utills.Print.PrintPic;
import com.sis.clighteningboost.Utills.Print.PrinterCommands;
import com.sis.clighteningboost.Utills.SharedPreference;
import com.sis.clighteningboost.Utills.StaticClass;
import com.sis.clighteningboost.Utills.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import tech.gusavila92.websocketclient.WebSocketClient;

public class MerchantBoostTerminal extends BaseActivity {
    boolean t = false;
    SharedPreference sp;
    TextView merchant_id_tv, client_id_tv;
    StaticClass st;
    EditText clientnodeid;
    ProgressDialog progressDialog;
    ArrayList<RoutingNode> routingNodeArrayList = new ArrayList<>();
    private ArrayList<MerchantData> allMerchantDataList = new ArrayList<>();
    String clientNodeId = "";
    RecyclerView recyclerView;
    MerchantItemAdapter merchantItemAdapter;
    boolean isGetNodeLine1MaxBoost = false;
    boolean isGetNodeLine2MaxBoost = false;
    boolean isGetNodeLine3MaxBoost = false;
    boolean isGetNodeLine4MaxBoost = false;
    Dialog pop2Dialog;
    final static int ADMINROLE = 0;
    final static int MERCHANTROLE = 1000;
    final static int CHECKOUT = 2000;
    TextView line1maxboostamount, line1maxboostamountBTC, line2maxboostamount, line2maxboostamountBTC, line3maxboostamount, line3maxboostamountBTC, line4maxboostamount, line4maxboostamountBTC, clientMaxBoostval, merchantMAxBoostval;
    int count = 0;
    private static double bitCoinValue = 0.0;
    private static double merhantMaxBoost = 200;
    private static double clientMaxBoost = 100;
    private MerchantData merchantData;
    private ClientData clientData;
    private double AMOUNT_BTC = 0;
    private double AMOUNT_USD = 0;
    private double BOOST_CHARG_BTC = 0;
    private double BOOST_CHARG_USD = 0;
    private double TOTAL_AMOUNT_BTC = 0;
    private double TOTAL_AMOUNT_USD = 0;
    private double USD_TO_BTC_RATE = 0;
    private String TRANSACTION_LABEL = "";
    private double LINE1_MAX_BOOST = 0;
    private double LINE2_MAX_BOOST = 0;
    private double LINE3_MAX_BOOST = 0;
    private double LINE4_MAX_BOOST = 0;
    private String merchant_IDd = "";
    private long timeStampOfPause = 0;
    //listchannels null [node id]
    //BOLT 11 Qr Scan Stuff
    int scannerID = 0;
    Dialog commandeerRefundDialog, commandeerRefundDialogstep2;
    String bolt11fromqr = "";
    ProgressDialog decodePayBolt11ProgressDialog, payOtherProgressDialog;
    int mode = 0;
    ImageView clientImage;
    String clientNodeID222 = "";
    String merchantId = "";

    // Print Related
    private static final int REQUEST_ENABLE_BT = 2;
    BluetoothAdapter mBluetoothAdapter;
    private UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothSocket mBluetoothSocket;
    BluetoothDevice mBluetoothDevice;
    int printstat;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private static OutputStream btoutputstream;
    ProgressDialog printingProgressBar;
    Dialog blutoothDevicesDialog;
    TextView setTextWithSpan;
    RelativeLayout rl_enterNodeIdLayer, rl_lineNodeInfo;


    private String authLevel1;
    private String authLevel2;
    private String invoiceId;
    private String lockTimeStamp;


    private String receivingNodeId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lockTimeStamp = (new Date().getTime()) / 1000 + "";
        setContentView(R.layout.activity_merchant_boost_terminal2);
        sp = new SharedPreference(this, "local_data");
        st = new StaticClass(this);
        printingProgressBar = new ProgressDialog(MerchantBoostTerminal.this);

        merchant_id_tv = findViewById(R.id.merchant_id_tv);
        client_id_tv = findViewById(R.id.client_id_tv);

        rl_lineNodeInfo = findViewById(R.id.rl_lineNodeInfo);
        rl_enterNodeIdLayer = findViewById(R.id.rl_enterNodeIdLayer);

        line1maxboostamount = findViewById(R.id.line1maxboostamount);
        line2maxboostamount = findViewById(R.id.line2maxboostamount);
        line3maxboostamount = findViewById(R.id.line3maxboostamount);
        line4maxboostamount = findViewById(R.id.line4maxboostamount);
        line1maxboostamountBTC = findViewById(R.id.line1maxboostamountBTC);
        line2maxboostamountBTC = findViewById(R.id.line2maxboostamountBTC);
        line3maxboostamountBTC = findViewById(R.id.line3maxboostamountBTC);
        line4maxboostamountBTC = findViewById(R.id.line4maxboostamountBTC);
        clientMaxBoostval = findViewById(R.id.client_maxboost);
        merchantMAxBoostval = findViewById(R.id.merchant_maxboost);
        clientImage = findViewById(R.id.iv_client);
        merchantId = sp.getStringValue("merchant_id");
        merchant_id_tv.setText(sp.getStringValue("merchant_id"));
        client_id_tv.setText(sp.getStringValue("client_name"));
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        decodePayBolt11ProgressDialog = new ProgressDialog(this);
        decodePayBolt11ProgressDialog.setMessage("Loading");
        payOtherProgressDialog = new ProgressDialog(this);
        payOtherProgressDialog.setMessage("Loading");
        getAllRountingNodeList();
        getFundingNodeInfor();
        merchantData = GlobalState.getInstance().getMainMerchantData();
        clientData = GlobalState.getInstance().getMainClientData();
        if (merchantData != null) {
            Double t = Double.parseDouble(merchantData.getRemaining_daily_boost());
            merchantMAxBoostval.setText("$" + String.format("%.2f", round(t, 2)));
        } else {

            merchantMAxBoostval.setText("$0");
        }
        if (clientData != null) {
            Double tt = Double.parseDouble(clientData.getRemaining_daily_boost());
            clientMaxBoostval.setText("$" + String.format("%.2f", round(tt, 2)));
            if (clientData.getClient_image_id() != null) {
                if (!clientData.getClient_image_id().isEmpty()) {
                    final String mImageUrlString = AppConstants.CLIENT_IMAGE_BASE_URL + clientData.getClient_image_id();
                    // Load the image into image view

                    Glide.with(this).load(mImageUrlString).into(clientImage);
//                    Glide.with(this)
//                            //.load(mImageUri) // Load image from assets
//                            .load(mImageUrlString) // Image URL
//                            .into((ImageView) findViewById(R.id.iv_client)); // ImageView to display image
                } else {

                }

            } else {

            }
        } else {

        }
        if (getIntent().getStringExtra("node_id") != null && !getIntent().getStringExtra("node_id").isEmpty())
            receivingNodeId = getIntent().getStringExtra("node_id");

        EditText et_clientnodeid = findViewById(R.id.et_clientnodeid);
        et_clientnodeid.setText(receivingNodeId);

        Button proceed = findViewById(R.id.btn_proceed_for_node);
        proceed.performClick();


        findViewById(R.id.btn_logout).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.clearAll();
                openActivity(MerchantLink.class);
                finish();
                //GlobalState.getInstance().setNodeLineInfoArrayList(new ArrayList<>());
                //show2ndPopUp("2323");
                //show2ndPopUp("100");
                //Pay pay=new Pay();
                //pay.setCreated_at(1605006049);
                //addInTransactionLog(10,0.00065,pay);
                //clientData.setClient_maxboost("50");
                //merchantData.setMerchant_maxboost("50");
                //GlobalState.getInstance().setMainMerchantData(merchantData);
                //GlobalState.getInstance().setMainClientData(clientData);
                //updateMerchantClientMaxBoost();
            }
        });

        findViewById(R.id.Line1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetNodeLine1MaxBoost) {
                    showFirstDialog(1);
                } else {
                    //TODO:For Testing Purpose Only
//                    double clientMx=Integer.parseInt(clientData.getMaxboost_limit());
//                    double merchantMx=Integer.parseInt(merchantData.getMaxboost_limit());
//                    if(clientMx<1 || merchantMx<1){
//                        new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
//                                .setMessage("Client and Merchant Limit Exceed")
//                                .setPositiveButton("Try Again Tommorow", null)
//                                .show();
//                    }
                }
            }
        });
        findViewById(R.id.line2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetNodeLine2MaxBoost) {
                    showFirstDialog(2);
                }
            }
        });
        findViewById(R.id.line3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetNodeLine3MaxBoost) {
                    showFirstDialog(3);
                }
            }
        });
        findViewById(R.id.line4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isGetNodeLine4MaxBoost) {
                    showFirstDialog(4);
                }
            }
        });
        findViewById(R.id.btn_scan_for_node).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scannerIntent();
                //String bolt11="lnbc66660p1psmehhepp5tphydr3ngwpzkhgdjrfw28pduc0exmypw9r3t8am5kh8wpq3wycqdqvdfkkgen0v34sxqyjw5qcqpjsp5m2fvdd0st23sp749nysze2a32mrt5m4wxkwx4zpwtlyqw4crcyeq9qyyssqcp2kvswqn6auxnuqztzg6e866v2pr57y05dqzkhtutfffun8gxg5u7m275ssfaa42ct3q0y67xqfhmtv5wanpdpr5jkzhurx74dcpsgp0566m9";
                //decodeBolt1122(bolt11);
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_clientnodeid = findViewById(R.id.et_clientnodeid);
                String et_clientnodeid_val = et_clientnodeid.getText().toString();
                if (et_clientnodeid_val != null) {
                    if (!et_clientnodeid_val.isEmpty()) {
                        clientNodeID222 = et_clientnodeid_val;
                        routingNodeExecute(et_clientnodeid_val);
                    } else {
                        Toast.makeText(getApplication(), "Result Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplication(), "Result Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        getBitCoinValue();
        final Handler ha = new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {
                //call function
                getBitCoinValue();
                ha.postDelayed(this, AppConstants.getLatestRateDelayTime);
            }
        }, AppConstants.getLatestRateDelayTime);
        createWebSocketClient();
    }

    private WebSocketClient webSocketClient;

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("wss://ws.bitstamp.net/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("WebSocket", message);
                            //TextView textView = findViewById(R.id.animalSound);
                            //textView.setText(message);
                        } catch (Exception e) {
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

    private void routingNodeExecute(String clientNodeId2) {
        ArrayList<NodeLineInfo> temp = new ArrayList<>();
        GlobalState.getInstance().setNodeLineInfoArrayList(temp);
        clientNodeId = clientNodeId2;
        count = 0;
        routingNodeArrayList = new ArrayList<>();
        routingNodeArrayList = GlobalState.getInstance().getNodeArrayList();
        if (routingNodeArrayList != null) {
            if (routingNodeArrayList.size() > 0) {
                String soverignLink = routingNodeArrayList.get(0).getIp() + ":" + routingNodeArrayList.get(0).getPort();
                String user = routingNodeArrayList.get(0).getUsername();
                String pass = routingNodeArrayList.get(0).getPassword();
                //executeRoutingNodeCals(soverignLink,user,pass,clientNodeId);
                // getRoutingNodesData("onoff","abc123","123","listpeers "+clientNodeId+" ");
                getNodeData2(merchantId, soverignLink, pass, "listpeers " + clientNodeId + " ");
                Log.e("Testphase", ":End");
            }
        }
    }

    //DevelopByNaeem//
    private void getNodeData2(String merchantId, String url, String pass, String clientNodeId2) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new ChuckerInterceptor.Builder(this)
                                .build()
                )
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("merchantBackendPassword", "abc123");
            requestBody.put("boost2faPassword", "123456");
            requestBody.put("command", clientNodeId2);

            //quoc testing
            getARoutingAPIAuth1(merchantId, "abc123", url, clientNodeId2);
            if (authLevel2 == null) {
                getARoutingAPIAuth2(merchantId, "123456", url, clientNodeId2);
            }
//                getARoutingAPIAuth2(merchantId,"123456");
            //quoc testing


//                Call<Object> call=apiInterface.getNodes(requestBody);
//                call.enqueue(new Callback<Object>() {
//                    @Override
//                    public void onResponse(Call<Object> call, Response<Object> response) {
//                        try {
//                            JSONObject object=new JSONObject(new Gson().toJson(response.body()));
//                            Iterator<String> keys = object.keys();
//                            JSONObject object1=null;
//                            HashMap<String, Object> map = new HashMap<>();
//                            if( keys.hasNext() ){
//                                String key = (String)keys.next();
//                                Object object22=object.get(key);
//                                map.put(key, object.get(key));
//                            }
//                            String firstName = (String) map.get("output");
//                            firstName = firstName.replaceAll("\\s", "");
//                            firstName= firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
//                            firstName=firstName.substring(2);
//                            firstName= firstName.replaceAll("b'", "");
//                            firstName= firstName.replaceAll("',", "");
//                            firstName= firstName.replaceAll("'", "");
//                            firstName = firstName.substring(0,firstName.length() -1);
//                            if (!firstName.contains("code")){
//
//                            }else {
//                                firstName= firstName.replaceAll("\\\\", "");
//                                JSONObject object2=new JSONObject(firstName);
//                                String message=object2.getString("message");
//                                st.toast(message);
//                                progressDialog.dismiss();
//
//                            }
//
//                            JSONObject object2=new JSONObject(firstName);
//                            JSONArray  ja_data=null;
//                            try {
//                                ja_data = object2.getJSONArray("peers");
//                            } catch (JSONException e) {
//                                progressDialog.dismiss();
//                                //e.printStackTrace();
//                                Log.e("Error",e.getMessage());
//                            }
//                            if(ja_data.length()>0){
//                                JSONObject finalJSONobject=null;
//                                boolean sta=false;
//                                try {
//                                    finalJSONobject=new JSONObject(String.valueOf(ja_data.getJSONObject(0)));
//                                    sta=true;
//                                } catch (JSONException e) {
//                                    progressDialog.dismiss();
//                                    sta=false;
//                                    //e.printStackTrace();
//                                    Log.e("error4",e.getMessage());
//                                }
//                                if(sta)
//                                {
//                                    String temp1=finalJSONobject.toString();
//                                    Log.e("finalJSONobject",finalJSONobject.toString());
//                                    NodeLineInfo nodeLineInfo=new NodeLineInfo();
//                                    Gson gson = new Gson();
//                                    boolean failed = false;
//                                    try
//                                    {
//                                        nodeLineInfo= gson.fromJson(finalJSONobject.toString(),NodeLineInfo .class);
//                                        //...
//                                    }
//                                    catch (IllegalStateException | JsonSyntaxException exception)
//                                    {
//
//                                        failed = true;
//                                        //...
//                                    }
//                                    if (failed)
//                                    {
//                                        //progressDialog.dismiss();
//                                    }else {
//                                       // progressDialog.dismiss();
//                                        nodeLineInfo.setOn(true);
//                                        GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
//                                        ArrayList<NodeLineInfo> tempList=GlobalState.getInstance().getNodeLineInfoArrayList();
//                                        Log.e("Test1","test");
//                                    }
//                                    count=count+1;
//                                    if(routingNodeArrayList!=null){
//                                        if(routingNodeArrayList.size()>=count+1){
//                                            String soverignLink=routingNodeArrayList.get(count).getIp()+":"+routingNodeArrayList.get(count).getPort();
//                                            String user=routingNodeArrayList.get(count).getUsername();
//                                            String pass=routingNodeArrayList.get(count).getPassword();
//                                            getNodeData2(merchantId,soverignLink,pass,"listpeers "+clientNodeId+" ");
//                                        }
//                                        else
//                                        {
//                                            progressDialog.dismiss();
//                                            updatetheNodeList();
//                                        }
//                                    }else {
//                                        progressDialog.dismiss();
//                                    }
//                                }
//                                else{
//                                    progressDialog.dismiss();
//                                    NodeLineInfo nodeLineInfo=new NodeLineInfo();
//                                    nodeLineInfo.setOn(false);
//                                    GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
//                                    ArrayList<NodeLineInfo> tempList=GlobalState.getInstance().getNodeLineInfoArrayList();
//                                    Log.e("Test1","test");
//                                }
//                            }else if (ja_data.equals("[]")){
//                                count=count+1;
//
//                                if(routingNodeArrayList!=null){
//                                    if(routingNodeArrayList.size()>=count+1){
//                                        String soverignLink=routingNodeArrayList.get(count).getIp()+":"+routingNodeArrayList.get(count).getPort();
//                                        String user=routingNodeArrayList.get(count).getUsername();
//                                        String pass=routingNodeArrayList.get(count).getPassword();
//                                        getNodeData2(merchantId,soverignLink,pass,"listpeers "+clientNodeId+" ");
//                                    }
//                                    else
//                                    {
//                                        progressDialog.dismiss();
//                                        updatetheNodeList();
//                                    }
//                                }else {
//                                    progressDialog.dismiss();
//                                }
//
//                            }
//                            else{
//                               // progressDialog.dismiss();
//                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
//                                count=count+1;
//
//                                if(routingNodeArrayList!=null){
//                                    if(routingNodeArrayList.size()>=count+1){
//                                        String soverignLink=routingNodeArrayList.get(count).getIp()+":"+routingNodeArrayList.get(count).getPort();
//                                        String user=routingNodeArrayList.get(count).getUsername();
//                                        String pass=routingNodeArrayList.get(count).getPassword();
//                                        getNodeData2(merchantId,soverignLink,pass,"listpeers "+clientNodeId+" ");
//                                    }
//                                    else
//                                    {
//                                        progressDialog.dismiss();
//                                        updatetheNodeList();
//                                    }
//                                }else {
//                                    progressDialog.dismiss();
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progressDialog.dismiss();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<Object> call, Throwable t) {
//                        Log.e("TAG", "onResponse: "+t.getMessage().toString() );
//                        progressDialog.dismiss();
//                    }
//                });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void getRoutingNodesData(String merchantId, String mBPass, String pass, String clientNodeId2) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        Call<ResponseBody> call = ApiClientForNode.getRetrofit(this).create(ApiInterfaceForNodes.class).getNodesData(merchantId, mBPass, pass, clientNodeId2);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.body() != null) {
                    String result = response.body().toString();
                    String[] resaray = result.split(",");
                    if (result.contains("peers")) {
                        if (resaray[0].contains("resp") && resaray[1].contains("status") && resaray[2].contains("rpc-cmd") && resaray[3].contains("cli-node")) {
                            String jsonresponse = "";
                            for (int i = 4; i < resaray.length; i++) {
                                jsonresponse += "," + resaray[i];
                            }
                            jsonresponse = jsonresponse.substring(1);
                            parseJSONForNodeLineInfor(jsonresponse);
                        } else {
                            parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                        }

                    } else {
                        parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                    }
                }
                progressDialog.dismiss();
                //getAllMerchantList();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                st.toast("Network Error");
                //getAllRountingNodeList();
                //getAllMerchantList();
            }
        });
    }

    private void executeRoutingNodeCals(String soverignLink, String user, String pass, String clientNodeId2) {
        GetInfoOFLineNode getInfoOFLineNode = new GetInfoOFLineNode(MerchantBoostTerminal.this);
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            getInfoOFLineNode.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{new String(soverignLink), new String(user), new String(pass), new String(clientNodeId2)});
        } else {
            getInfoOFLineNode.execute(new String[]{new String(soverignLink), new String(user), new String(pass), new String(clientNodeId2)});
        }
    }

    private void showFirstDialog(int position) {

        switch (position) {
            case 1:
                if (clientData != null && merchantData != null) {
                    if (clientData.getClient_maxboost() != null && merchantData.getMerchant_maxboost() != null) {
                        double clientMax = Double.parseDouble(clientData.getClient_maxboost());
                        double merchantMax = Double.parseDouble(merchantData.getMerchant_maxboost());
                        if (clientMax > 0 && merchantMax > 0 && LINE1_MAX_BOOST > 0) {
                            showFirstPopUp(position);
                        } else {
                            String reason = "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded";
                            new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                                    .setMessage(reason)
                                    .setPositiveButton("Try Again Tommorrow", null)
                                    .show();
                            return;
                        }
                    }
                }

                break;
            case 2:
                if (clientData != null && merchantData != null) {
                    if (clientData.getClient_maxboost() != null && merchantData.getMerchant_maxboost() != null) {
                        double clientMax = Double.parseDouble(clientData.getClient_maxboost());
                        double merchantMax = Double.parseDouble(merchantData.getMerchant_maxboost());
                        if (clientMax > 0 && merchantMax > 0 && LINE2_MAX_BOOST > 0) {
                            showFirstPopUp(position);
                        } else {
                            String reason = "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded";

                            new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                                    .setMessage(reason)
                                    .setPositiveButton("Try Again Tommorrow", null)
                                    .show();
                            return;
                        }
                    }
                }
                //showFirstDialogLine2();
                break;
            case 3:
                if (clientData != null && merchantData != null) {
                    if (clientData.getClient_maxboost() != null && merchantData.getMerchant_maxboost() != null) {
                        double clientMax = Double.parseDouble(clientData.getClient_maxboost());
                        double merchantMax = Double.parseDouble(merchantData.getMerchant_maxboost());
                        if (clientMax > 0 && merchantMax > 0 && LINE3_MAX_BOOST > 0) {
                            showFirstPopUp(position);
                        } else {
                            String reason = "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded";
                            new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                                    .setMessage(reason)
                                    .setPositiveButton("Try Again Tommorrow", null)
                                    .show();
                            return;
                        }
                    }
                }
                // showFirstDialogLine3();
                break;
            case 4:
                if (clientData != null && merchantData != null) {
                    if (clientData.getClient_maxboost() != null && merchantData.getMerchant_maxboost() != null) {
                        double clientMax = Double.parseDouble(clientData.getClient_maxboost());
                        double merchantMax = Double.parseDouble(merchantData.getMerchant_maxboost());
                        if (clientMax > 0 && merchantMax > 0 && LINE4_MAX_BOOST > 0) {
                            showFirstPopUp(position);
                        } else {
                            String reason = "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded";

                            new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                                    .setMessage(reason)
                                    .setPositiveButton("Try Again Tommorrow", null)
                                    .show();
                            return;
                        }
                    }
                }
                //showFirstDialogLine4();
                break;
        }
    }

    private void showFirstPopUp(int lineNo) {
        double temBosstLimitOfChanel = 0;
        switch (lineNo) {
            case 1:
                temBosstLimitOfChanel = LINE1_MAX_BOOST;
                break;
            case 2:
                temBosstLimitOfChanel = LINE2_MAX_BOOST;
                break;
            case 3:
                temBosstLimitOfChanel = LINE3_MAX_BOOST;
                break;
            case 4:
                temBosstLimitOfChanel = LINE4_MAX_BOOST;
                break;
            default:
                temBosstLimitOfChanel = 0;
                break;
        }
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        final Dialog pop1Dialog;
        pop1Dialog = new Dialog(this);
        pop1Dialog.setContentView(R.layout.firstpopuplayout2);
        Objects.requireNonNull(pop1Dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //op1Dialog.getWindow().setLayout((int) (width/1.2), (int) (height/1.5));
        pop1Dialog.setCancelable(false);
        final EditText et_amount = pop1Dialog.findViewById(R.id.et_amount_of_boost);
        final ImageView ivBack = pop1Dialog.findViewById(R.id.iv_back);
        Button proceed = pop1Dialog.findViewById(R.id.btn_next);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop1Dialog.dismiss();
            }
        });
        double finalTemBosstLimitOfChanel = temBosstLimitOfChanel;
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountBoostVal = et_amount.getText().toString();
                if (amountBoostVal.isEmpty()) {
                    showToast("Enter Boost Amount");
                } else {
                    double clientMaxBoost = 0;
                    double merchantMaxBoost = 0;
                    double enteredAmount = 0;
                    clientMaxBoost = Double.parseDouble(clientData.getClient_maxboost());
                    merchantMaxBoost = Double.parseDouble(merchantData.getMerchant_maxboost());
                    enteredAmount = Double.parseDouble(amountBoostVal);
                    String reason = "";
                    if (enteredAmount <= clientMaxBoost && enteredAmount <= merchantMaxBoost && enteredAmount <= finalTemBosstLimitOfChanel) {
                        show2ndPopUp(amountBoostVal);
                        pop1Dialog.dismiss();
                    } else {
                        reason = "";
                        if (enteredAmount > clientMaxBoost && enteredAmount > merchantMaxBoost && enteredAmount > finalTemBosstLimitOfChanel) {
                            reason = "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded";
                        } else {
                            if (enteredAmount > clientMaxBoost) {
                                reason = reason + "Client Recharge Exceeded\n";
                            }
                            if (enteredAmount > merchantMaxBoost) {
                                reason = reason + "Merchant Max Boost Exceeded\n";
                            }
                            if (enteredAmount > finalTemBosstLimitOfChanel) {
                                reason = reason + "Line Recharge Exceeded\n";
                            }
                        }
                        et_amount.setText("");
                        new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                                .setMessage(reason)
                                .setPositiveButton("Try Again Tommorrow", null)
                                .show();
                        return;
                    }
                }
            }
        });
        pop1Dialog.show();
    }

    private void show2ndPopUp(String amountBoostVal) {
        lockTimeStamp = ((new Date().getTime()) / 1000) + "";

        FundingNode fundingNode = GlobalState.getInstance().getFundingNode();
        double feeBoostPercntage = 4.49;
        if (fundingNode != null) {
            if (fundingNode.getLightning_boost_fee() != null) {
                feeBoostPercntage = Double.parseDouble(fundingNode.getLightning_boost_fee());
            }
        }
        Log.e("Amount", amountBoostVal);
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;

        pop2Dialog = new Dialog(this);
        pop2Dialog.setContentView(R.layout.secondpopup);
        Objects.requireNonNull(pop2Dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // pop2Dialog.getWindow().setLayout((int) (width/1.2), (int) (height));
        pop2Dialog.setCancelable(false);
        Button getinvocie = pop2Dialog.findViewById(R.id.getinvocie);
        getinvocie.setVisibility(View.INVISIBLE);
        final TextView amount_usd = pop2Dialog.findViewById(R.id.amount_usd);
        final TextView boostFee_usd = pop2Dialog.findViewById(R.id.fees_usd);
        final TextView totalAmount_usd = pop2Dialog.findViewById(R.id.total_usd);
        final TextView collectTotalAmount = pop2Dialog.findViewById(R.id.collect_amount);
        final CheckBox confirmCheckBox = pop2Dialog.findViewById(R.id.checkBox1);
        confirmCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (confirmCheckBox.isChecked()) {
                    // true,do the task
                    getinvocie.setVisibility(View.VISIBLE);
                } else {
                    getinvocie.setVisibility(View.INVISIBLE);
                }
            }
        });

        double amountt = Double.parseDouble(amountBoostVal);

        AMOUNT_BTC = round(getBtcFromUsd(amountt), 9);
        AMOUNT_USD = amountt;
        double usd_fees_from_prcnt = feeBoostPercntage / 100;
        usd_fees_from_prcnt = usd_fees_from_prcnt * AMOUNT_USD;
        double total = usd_fees_from_prcnt + amountt;
        BOOST_CHARG_BTC = round(getBtcFromUsd(usd_fees_from_prcnt), 9);
        BOOST_CHARG_USD = usd_fees_from_prcnt;
        TOTAL_AMOUNT_BTC = round(getBtcFromUsd(total), 9);
        TOTAL_AMOUNT_USD = total;
        USD_TO_BTC_RATE = AMOUNT_USD / AMOUNT_BTC;
        String temp = String.format("%.2f", round(AMOUNT_USD, 2));
        amount_usd.setText("$" + temp + " / " + excatFigure(round(AMOUNT_BTC, 9)) + " BTC");
        boostFee_usd.setText("$" + String.format("%.2f", round(BOOST_CHARG_USD, 2)) + " / " + excatFigure(round(BOOST_CHARG_BTC, 9)) + " BTC");
        totalAmount_usd.setText("$" + String.format("%.2f", round(TOTAL_AMOUNT_USD, 2)) + " / " + excatFigure(round(TOTAL_AMOUNT_BTC, 9)) + " BTC");
        collectTotalAmount.setText("$" + String.format("%.2f", round(TOTAL_AMOUNT_USD, 2)));
        final ImageView ivBack = pop2Dialog.findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop2Dialog.dismiss();
            }
        });

        getinvocie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //quoc testing comment code
//                Context c=MerchantBoostTerminal.this;
//                final Dialog enter2FaPassDialog;
//                enter2FaPassDialog=new Dialog(c);
//                enter2FaPassDialog.setContentView(R.layout.merchat_twofa_pass_lay);
//                Objects.requireNonNull(enter2FaPassDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                enter2FaPassDialog.setCancelable(false);
//                final EditText et_2Fa_pass=enter2FaPassDialog.findViewById(R.id.taskEditText);
//                final Button btn_confirm=enter2FaPassDialog.findViewById(R.id.btn_confirm);
//                final Button btn_cancel=enter2FaPassDialog.findViewById(R.id.btn_cancel);
//                final ImageView iv_back=enter2FaPassDialog.findViewById(R.id.iv_back);
//                et_2Fa_pass.setHint(R.string.enterboostpassword);
//
//                iv_back.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        enter2FaPassDialog.dismiss();
//                    }
//                });
//                btn_confirm.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        String task = String.valueOf(et_2Fa_pass.getText());
//                        if(task.isEmpty()){
//                            goAlertDialogwithOneBTn(1,"","Enter 2FA Password","Ok","");
//                        }else {
//                            if(task.equals(merchantData.getPassword())){
//                                dialogBoxForRefundCommandeer(task);
//                                enter2FaPassDialog.dismiss();
//                            }else {
//                                goAlertDialogwithOneBTn(1,"","Incorrect Password","Retry","");
//                            }
//                        }
//                    }
//                });
//                btn_cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        enter2FaPassDialog.dismiss();
//                    }
//                });
//                enter2FaPassDialog.show();
                //quoc testing

                //quoc testing add to temp remove dialog
                dialogBoxForRefundCommandeer("123456");
            }
        });
        pop2Dialog.show();
    }

    //Pop3 Scan BOLT11  QR
    private void dialogBoxForRefundCommandeer(String fa2pass) {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        commandeerRefundDialog = new Dialog(MerchantBoostTerminal.this);
        commandeerRefundDialog.setContentView(R.layout.dialoglayoutrefundcommandeer2);
        Objects.requireNonNull(commandeerRefundDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //commandeerRefundDialog.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        commandeerRefundDialog.setCancelable(false);
        final EditText bolt11 = commandeerRefundDialog.findViewById(R.id.bolt11val);
        final ImageView ivBack = commandeerRefundDialog.findViewById(R.id.iv_back);
        final TextView tv_amountshown = commandeerRefundDialog.findViewById(R.id.amount);
        tv_amountshown.setText(excatFigure(round(AMOUNT_BTC, 9)) + " BTC");
        Button btnNext = commandeerRefundDialog.findViewById(R.id.btn_next);
        Button btnscanQr = commandeerRefundDialog.findViewById(R.id.btn_scanQR);
        // progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandeerRefundDialog.dismiss();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bolt11value = bolt11.getText().toString();
                if (bolt11value.isEmpty()) {
                    new androidx.appcompat.app.AlertDialog.Builder(MerchantBoostTerminal.this)
                            .setMessage("Bolt 11 Empty")
                            .setPositiveButton("Try Again", null)
                            .show();
                    return;
                } else {

                    //quoc testing temp comment
//                    commandeerRefundDialog.dismiss();
                    //quoc testing temp comment
                    bolt11fromqr = bolt11value;
                    decodeBolt1122(bolt11value, fa2pass);
                    //decodeBolt11(bolt11value);
                }
            }
        });
        btnscanQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //qrScan.forSupportFragment(AdminFragment1.this).initiateScan();
                scannerIntent2();
            }
        });
        commandeerRefundDialog.show();
    }

    private void decodeBolt1122(String bolt11fromqr1, String fa2pass) {
        decodePayBolt11ProgressDialog.show();
        decodePayBolt11ProgressDialog.setCancelable(false);
        decodePayBolt11ProgressDialog.setCanceledOnTouchOutside(false);
        FundingNode node = GlobalState.getInstance().getFundingNode();
        if (node != null) {
            String ip = GlobalState.getInstance().getFundingNode().getIp();
            int port = Integer.parseInt(GlobalState.getInstance().getFundingNode().getPort());
            String userNAme = GlobalState.getInstance().getFundingNode().getUsername();
            String pass = GlobalState.getInstance().getFundingNode().getPassword();
            String url = ip + ":" + GlobalState.getInstance().getFundingNode().getPort();
            //String bolt11="lnbc66660p1psmehhepp5tphydr3ngwpzkhgdjrfw28pduc0exmypw9r3t8am5kh8wpq3wycqdqvdfkkgen0v34sxqyjw5qcqpjsp5m2fvdd0st23sp749nysze2a32mrt5m4wxkwx4zpwtlyqw4crcyeq9qyyssqcp2kvswqn6auxnuqztzg6e866v2pr57y05dqzkhtutfffun8gxg5u7m275ssfaa42ct3q0y67xqfhmtv5wanpdpr5jkzhurx74dcpsgp0566m9";
            String bolt11 = bolt11fromqr1;
            invoiceId = bolt11;
            bolt11fromqr = bolt11;
            decodeBolt112(merchantId, url, pass, " decodepay " + bolt11 + "", fa2pass);

            //quoc testing
            decodeBolt112Execute(merchantId, url, pass, " decodepay " + bolt11 + "", fa2pass);
            //quoc testing
        } else {

        }

    }

    private void decodeBolt112Execute(String merchantId, String url, String pass, String clientNodeId2, String fa2pass) {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new ChuckerInterceptor.Builder(this)
                                .build()
                )
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("cmd", clientNodeId2.trim());
            Call<DecodeBolt112WithExecuteResponse> call = apiInterface.getDecodeBolt112WithExecute("Bearer " + authLevel1, requestBody);
            call.enqueue(new Callback<DecodeBolt112WithExecuteResponse>() {
                @Override
                public void onResponse(Call<DecodeBolt112WithExecuteResponse> call, Response<DecodeBolt112WithExecuteResponse> response) {
                    try {
                        if (response.body().success && response.body().decodeBolt112WithExecuteData != null) {
                            JSONObject object2 = new JSONObject(response.body().decodeBolt112WithExecuteData.stdout);

                            if (object2.length() > 0) {
                                if (response.body().decodeBolt112WithExecuteData.stdout.contains("msatoshi")) {
                                    DecodePayBolt11 decodePayBolt11 = null;

                                    JSONObject jsonObj = null;
                                    jsonObj = object2;

                                    if (jsonObj != null) {
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<DecodePayBolt11>() {
                                            }.getType();
                                            decodePayBolt11 = gson.fromJson(jsonObj.toString(), type);
                                            GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                        } catch (Exception e) {
                                            decodePayBolt11ProgressDialog.dismiss();
                                            Log.e("Error", e.getMessage());
                                        }
                                    }
                                    GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                    if (decodePayBolt11 != null) {
                                        double btc = mSatoshoToBtc(Double.valueOf(String.valueOf(decodePayBolt11.getMsatoshi())));
                                        double priceInBTC = GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
                                        double usd = priceInBTC * btc;
                                        usd = round(usd, 2);

                                        //quoc testing
                                        if (usd != round(AMOUNT_USD, 2)) {
                                            goAlertDialogwithOneBTn(1, "Error", getResources().getString(R.string.invoice_made_for_incorrect_value), "Ok", "");
                                            return;
                                        } else {
                                            //quoc testing

                                            decodePayBolt11ProgressDialog.dismiss();
                                            dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), url, pass, fa2pass);
                                        }
                                    } else {
                                        DecodePayBolt11 decode2PayBolt11 = new DecodePayBolt11();
                                        decode2PayBolt11.setMsatoshi(0);
                                        GlobalState.getInstance().setCurrentDecodePayBolt11(decode2PayBolt11);
                                        decodePayBolt11ProgressDialog.dismiss();
                                        dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decode2PayBolt11.getMsatoshi()), url, pass, fa2pass);
                                    }
                                } else {
                                    DecodePayBolt11 decodePayBolt11 = new DecodePayBolt11();
                                    decodePayBolt11.setMsatoshi(0);
                                    GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                    decodePayBolt11ProgressDialog.dismiss();
                                    dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), url, pass, fa2pass);
                                }
                            } else {
                                decodePayBolt11ProgressDialog.dismiss();
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }
                        } else {
                            if (response.body() != null && response.body().message != null) {
                                goAlertDialogwithOneBTn(1, "", response.body().message, "Ok", "");
                            } else {
                                goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "");
                            }
                            decodePayBolt11ProgressDialog.dismiss();
                        }
                    } catch (Exception e) {
                        if (response.body() != null && response.body().message != null) {
                            goAlertDialogwithOneBTn(1, "", response.body().message, "Ok", "");
                        } else if (response != null && response.message() != null) {
                            goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "");
                        } else {
                            goAlertDialogwithOneBTn(1, "", e.getMessage(), "Ok", "");
                        }
                        decodePayBolt11ProgressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DecodeBolt112WithExecuteResponse> call, Throwable t) {
                    decodePayBolt11ProgressDialog.dismiss();
                    Log.e("TAG", "onResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            decodePayBolt11ProgressDialog.dismiss();
            e.printStackTrace();
        }
    }


    private void decodeBolt112(String merchantId, String url, String pass, String clientNodeId2, String fa2pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("merchantBackendPassword", pass);
            requestBody.put("boost2faPassword", fa2pass);
            requestBody.put("command", clientNodeId2);
            Call<Object> call = apiInterface.getNodes(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        Iterator<String> keys = object.keys();
                        JSONObject object1 = null;
                        HashMap<String, Object> map = new HashMap<>();
                        if (keys.hasNext()) {
                            String key = (String) keys.next();
                            Object object22 = object.get(key);
                            map.put(key, object.get(key));
                        }

                        String firstName = (String) map.get("output");
                        firstName = firstName.replaceAll("\\s", "");
                        firstName = firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
                        firstName = firstName.substring(2);
                        firstName = firstName.replaceAll("b'", "");
                        firstName = firstName.replaceAll("',", "");
                        firstName = firstName.replaceAll("'", "");
                        firstName = firstName.substring(0, firstName.length() - 1);
                        if (!firstName.contains("code")) {
                            JSONObject object2 = new JSONObject(firstName);

                            if (object2.length() > 0) {
                                if (firstName.contains("msatoshi")) {
                                    DecodePayBolt11 decodePayBolt11 = null;

                                    JSONObject jsonObj = null;
                                    jsonObj = object2;

                                    if (jsonObj != null) {
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<DecodePayBolt11>() {
                                            }.getType();
                                            decodePayBolt11 = gson.fromJson(jsonObj.toString(), type);
                                            GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                        } catch (Exception e) {
                                            decodePayBolt11ProgressDialog.dismiss();
                                            Log.e("Error", e.getMessage());
                                        }
                                    }
                                    GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                    if (decodePayBolt11 != null) {
                                        decodePayBolt11ProgressDialog.dismiss();
                                        dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), url, pass, fa2pass);
                                    } else {
                                        DecodePayBolt11 decode2PayBolt11 = new DecodePayBolt11();
                                        decode2PayBolt11.setMsatoshi(0);
                                        GlobalState.getInstance().setCurrentDecodePayBolt11(decode2PayBolt11);
                                        decodePayBolt11ProgressDialog.dismiss();
                                        dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decode2PayBolt11.getMsatoshi()), url, pass, fa2pass);
                                    }
                                } else {
                                    DecodePayBolt11 decodePayBolt11 = new DecodePayBolt11();
                                    decodePayBolt11.setMsatoshi(0);
                                    GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                                    decodePayBolt11ProgressDialog.dismiss();
                                    dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), url, pass, fa2pass);
                                }
                            } else {
                                decodePayBolt11ProgressDialog.dismiss();
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }

                        } else {
                            firstName = firstName.replaceAll("\\\\", "");
                            JSONObject object2 = new JSONObject(firstName);
                            String message = object2.getString("message");
                            st.toast(message);
                            decodePayBolt11ProgressDialog.dismiss();
                        }

                    } catch (JSONException e) {
                        decodePayBolt11ProgressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    decodePayBolt11ProgressDialog.dismiss();
                    Log.e("TAG", "onResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            decodePayBolt11ProgressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void decodeBolt11(String bolt11fromqr) {
        DecodePayBolt11API decodePayBolt11API = new DecodePayBolt11API(MerchantBoostTerminal.this);
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            decodePayBolt11API.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{new String(bolt11fromqr)});
            decodePayBolt11ProgressDialog.show();
            decodePayBolt11ProgressDialog.setCancelable(false);
            decodePayBolt11ProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            decodePayBolt11API.execute(new String[]{new String(bolt11fromqr)});
            decodePayBolt11ProgressDialog.show();
            decodePayBolt11ProgressDialog.setCancelable(false);
            decodePayBolt11ProgressDialog.setCanceledOnTouchOutside(false);
        }
    }

    private class DecodePayBolt11API extends AsyncTask<String, Integer, String> {

        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;

        public DecodePayBolt11API(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
        }

        protected void onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            int count = urls.length;

            String bolt11 = urls[0];
            String response = "";

            String mlattitude = "0.0";
            if (GlobalState.getInstance().getLattitude() != null) {
                mlattitude = GlobalState.getInstance().getLattitude();
            }
            String mlongitude = "0.0";
            if (GlobalState.getInstance().getLongitude() != null) {
                mlongitude = GlobalState.getInstance().getLongitude();
            }
            //Bolt 11, msatoshi, label
            String decodePayBolt11Query = "rpc-cmd,cli-node," + mlattitude + "_" + mlongitude + "," + getUnixTimeStamp() + ",[ decodepay " + bolt11 + " ]";
            Log.e("DecodePayBolt11Query:", decodePayBolt11Query);
            FundingNode node = GlobalState.getInstance().getFundingNode();
            if (node != null) {
                String ip = GlobalState.getInstance().getFundingNode().getIp();
                int port = Integer.parseInt(GlobalState.getInstance().getFundingNode().getPort());
                String userNAme = GlobalState.getInstance().getFundingNode().getUsername();
                String pass = GlobalState.getInstance().getFundingNode().getPassword();
                Boolean status = Boolean.valueOf(NetworkManager.getInstance().connectClient(ip, port));
                if (status) {
                    int role = NetworkManager.getInstance().validateUser(userNAme, pass);
                    if (role == ADMINROLE) {
                        try {
                            NetworkManager.getInstance().sendToServer(decodePayBolt11Query);
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                        try {
                            // Try now
                            response = NetworkManager.getInstance().recvFromServer();
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    } else {
                        response = "fail";
                    }

                } else {
                    response = "fail";
                }
            } else {
                response = "fail";
            }

            Log.e(TAG, "DecodePayBolt11QueryResponse:" + response);
            return response;
        }

        protected void onProgressUpdate(Integer... progress) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);
            // isInApp=true;
            String response = result;

         /*
            resp,status,rpc-cmd,cli-node,[ {
           "currency": "bc",
           "created_at": 1596653872,
           "expiry": 604800,
           "payee": "02dc8590dd675b5bf89c6bdf9eeed767290b3d6056465e5b013756f65616d3d372",
           "msatoshi": 965000,
           "amount_msat": "965000msat",
           "description": "firstrefundtest",
           "min_final_cltv_expiry": 10,
           "payment_secret": "65b2f135147ee24e3c28d03fb733c406b3ab84ef6fb554beae1dc75d78e506a1",
           "features": "028200",
           "payment_hash": "8c8fb25e7a1851944f2f10974549fa0845fbd480dde33569e4382a99a2ccd59d",
           "signature": "30440220049f8ddd183ae01fb1f242459b1a0504eea05e1bcb9eab180b481ab1d61943f20220229d14b37c52443063a98e69e58e55750a5d2d3130be106913ff75558b0a6818"
            }
            ]
            */
            if (response.contains("msatoshi")) {
                String[] split = response.split(",");
                String invoiceReponse = "";
                for (int i = 4; i < split.length; i++) {
                    invoiceReponse += "," + split[i];
                }
                invoiceReponse = invoiceReponse.substring(1);

                DecodePayBolt11 decodePayBolt11 = parseJSONForDecodePayBolt11(invoiceReponse);
                GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                if (decodePayBolt11 != null) {
                    decodePayBolt11ProgressDialog.dismiss();
                    dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), "", "", "");
                } else {
                    DecodePayBolt11 decode2PayBolt11 = new DecodePayBolt11();
                    decode2PayBolt11.setMsatoshi(0);
                    GlobalState.getInstance().setCurrentDecodePayBolt11(decode2PayBolt11);
                    decodePayBolt11ProgressDialog.dismiss();
                    dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decode2PayBolt11.getMsatoshi()), "", "", "");
                }
            } else {
                DecodePayBolt11 decodePayBolt11 = new DecodePayBolt11();
                decodePayBolt11.setMsatoshi(0);
                GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
                decodePayBolt11ProgressDialog.dismiss();
                dialogBoxForRefundCommandeerStep2(bolt11fromqr, String.valueOf(decodePayBolt11.getMsatoshi()), "", "", "");
            }
            Log.e(TAG, "DecodePayBolt11QueryResponse:" + result);
        }
    }

    private void CommandRefundApi(String bolt11value, String labelval, String amountusd, String url, String pass, String fa2pass) {
        payOtherProgressDialog.show();
        payOtherProgressDialog.setCancelable(false);
        payOtherProgressDialog.setCanceledOnTouchOutside(false);
        double priceInBTC = 1 / GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
        priceInBTC = priceInBTC * Double.parseDouble(amountusd);
        double amountInMsatoshi = priceInBTC * AppConstants.btcToSathosi;
        amountInMsatoshi = amountInMsatoshi * AppConstants.satoshiToMSathosi;
        //msatoshi=excatFigure(amountInMsatoshi);
        NumberFormat formatter = new DecimalFormat("#0");
        String rMSatoshi = formatter.format(amountInMsatoshi);
        //showToast(bolt11value+"-"+labelval+"-"+rMSatoshi);
        Log.e("abcd", bolt11value + "-" + labelval + "-" + rMSatoshi);
        TRANSACTION_LABEL = labelval;
        String commad = "pay " + bolt11value + " " + "null" + " " + labelval + "";

//        CommandRefundApiRequest(merchantId,url,pass,commad,fa2pass);

        //quoc testing
        CommandRefundApiRequestWithExecute(merchantId, url, pass, commad, fa2pass);
        //quoc testing
    }

    private void CommandRefundApiRequestWithExecute(String merchantId, String url, String pass, String commad, String fa2pass) {
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new ChuckerInterceptor.Builder(this)
                                .build()
                )
                .addNetworkInterceptor(httpLoggingInterceptor)
                .readTimeout(180, TimeUnit.SECONDS)
                .connectTimeout(180, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
//            requestBody.put("cmd", commad);
//            Call<DecodeBolt112WithExecuteResponse> call=apiInterface.callRefundApiRequestWithExecute("Bearer " + authLevel2,requestBody);

            requestBody.put("invoice", invoiceId);
            requestBody.put("client_id", clientData.getClient_id());
            requestBody.put("invoiceLabel", TRANSACTION_LABEL);
            requestBody.put("lock_timestamp", lockTimeStamp);
            requestBody.put("lock_msatoshi", (long) (AMOUNT_BTC * 100000 * 1000000) + "");
            requestBody.put("lock_usd", AMOUNT_USD + "");
            Call<DecodeBolt112WithExecuteResponse> call = apiInterface.callPayApiRequestWithExecute("Bearer " + authLevel2, requestBody);
            call.enqueue(new Callback<DecodeBolt112WithExecuteResponse>() {
                @Override
                public void onResponse(Call<DecodeBolt112WithExecuteResponse> call, Response<DecodeBolt112WithExecuteResponse> response) {
                    try {

                        if (response.body().success) {
                            JSONObject object2 = new JSONObject(response.body().decodeBolt112WithExecuteData.stdout);
                            if (object2.length() > 0) {
                                if (response.body().decodeBolt112WithExecuteData.stdout.contains("payment_hash")) {
                                    Pay pay = null;
                                    JSONObject jsonObj = null;
                                    jsonObj = object2;
                                    if (jsonObj != null) {
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<Pay>() {
                                            }.getType();
                                            pay = gson.fromJson(jsonObj.toString(), type);
                                            // GlobalState.getInstance().setInvoice(pay);
                                        } catch (Exception e) {
                                            payOtherProgressDialog.dismiss();
                                            Log.e("Error", e.getMessage());
                                        }
                                    }
                                    if (pay != null) {
                                        if (pay.getStatus().equals("complete")) {
                                            //showToast("Succefully Pay");
                                            String body = "Amount:" + pay.getAmount_msat() + " Recived Succefully";
                                            //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                                            //quoc testing comment for new requirement
//                                            addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                                            //quoc testing comment for new requirement

                                            //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                                            //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                                            showCofirmationDialog(pay);
                                        } else {
                                            Pay payresponse1 = new Pay();
                                            payresponse1.setStatus("Not complete");
                                            showCofirmationDialog(payresponse1);
                                        }
                                    } else {
                                        Pay payresponse2 = new Pay();
                                        payresponse2.setStatus("Not complete");
                                        showCofirmationDialog(payresponse2);
                                    }
                                    payOtherProgressDialog.dismiss();
                                } else {
                                    Pay payresponse3 = new Pay();
                                    payresponse3.setStatus("Not complete");
                                    showCofirmationDialog(payresponse3);
                                    payOtherProgressDialog.dismiss();
                                }
                            } else {
                                payOtherProgressDialog.dismiss();
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }

                        } else {
                            Toast.makeText(MerchantBoostTerminal.this, "" + response.body().message, Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        payOtherProgressDialog.dismiss();
                        if (response.body() != null && response.body().message != null) {
                            goAlertDialogwithOneBTn(1, "", response.body().message, "Ok", "");
                        } else if (response != null && response.message() != null) {
                            goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "");
                        } else {
                            goAlertDialogwithOneBTn(1, "", e.getMessage(), "Ok", "");
                        }
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DecodeBolt112WithExecuteResponse> call, Throwable t) {
                    payOtherProgressDialog.dismiss();
                    Log.e("TAG", "onResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            payOtherProgressDialog.dismiss();
            e.printStackTrace();
        }
    }


    private void CommandRefundApiRequest(String merchantId, String url, String pass, String commad, String fa2pass) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("merchantBackendPassword", pass);
            requestBody.put("boost2faPassword", fa2pass);
            requestBody.put("command", commad);
            Call<Object> call = apiInterface.getNodes(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject object = new JSONObject(new Gson().toJson(response.body()));
                        Iterator<String> keys = object.keys();
                        JSONObject object1 = null;
                        HashMap<String, Object> map = new HashMap<>();
                        if (keys.hasNext()) {
                            String key = (String) keys.next();
                            Object object22 = object.get(key);
                            map.put(key, object.get(key));
                        }

                        String firstName = (String) map.get("output");
                        firstName = firstName.replaceAll("\\s", "");
                        firstName = firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
                        firstName = firstName.substring(2);
                        firstName = firstName.replaceAll("b'", "");
                        firstName = firstName.replaceAll("',", "");
                        firstName = firstName.replaceAll("'", "");
                        firstName = firstName.substring(0, firstName.length() - 1);
                        if (!firstName.contains("code")) {
                            JSONObject object2 = new JSONObject(firstName);
                            if (object2.length() > 0) {
                                if (firstName.contains("payment_hash")) {
                                    Pay pay = null;
                                    JSONObject jsonObj = null;
                                    jsonObj = object2;
                                    if (jsonObj != null) {
                                        try {
                                            Gson gson = new Gson();
                                            Type type = new TypeToken<Pay>() {
                                            }.getType();
                                            pay = gson.fromJson(jsonObj.toString(), type);
                                            // GlobalState.getInstance().setInvoice(pay);
                                        } catch (Exception e) {
                                            payOtherProgressDialog.dismiss();
                                            Log.e("Error", e.getMessage());
                                        }
                                    }
                                    if (pay != null) {
                                        if (pay.getStatus().equals("complete")) {
                                            //showToast("Succefully Pay");
                                            String body = "Amount:" + pay.getAmount_msat() + " Recived Succefully";
                                            //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                                            //quoc testing comment for new requirement
//                                            addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                                            //quoc testing comment for new requirement

                                            //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                                            //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                                            showCofirmationDialog(pay);
                                        } else {
                                            Pay payresponse1 = new Pay();
                                            payresponse1.setStatus("Not complete");
                                            showCofirmationDialog(payresponse1);
                                        }
                                    } else {
                                        Pay payresponse2 = new Pay();
                                        payresponse2.setStatus("Not complete");
                                        showCofirmationDialog(payresponse2);
                                    }
                                    payOtherProgressDialog.dismiss();
                                } else {
                                    Pay payresponse3 = new Pay();
                                    payresponse3.setStatus("Not complete");
                                    showCofirmationDialog(payresponse3);
                                    payOtherProgressDialog.dismiss();
                                }
                            } else {
                                payOtherProgressDialog.dismiss();
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }

                        } else {
                            firstName = firstName.replaceAll("\\\\", "");
                            JSONObject object2 = new JSONObject(firstName);
                            String message = object2.getString("message");
                            st.toast(message);
                            payOtherProgressDialog.dismiss();
                        }


                    } catch (JSONException e) {
                        payOtherProgressDialog.dismiss();
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    payOtherProgressDialog.dismiss();
                    Log.e("TAG", "onResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            payOtherProgressDialog.dismiss();
            e.printStackTrace();
        }
    }

    private void executeCommandeerRefundApi(String bolt11value, String labelval, String amountusd) {
        double priceInBTC = 1 / GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
        priceInBTC = priceInBTC * Double.parseDouble(amountusd);
        double amountInMsatoshi = priceInBTC * AppConstants.btcToSathosi;
        amountInMsatoshi = amountInMsatoshi * AppConstants.satoshiToMSathosi;
        //msatoshi=excatFigure(amountInMsatoshi);
        NumberFormat formatter = new DecimalFormat("#0");
        String rMSatoshi = formatter.format(amountInMsatoshi);
        //showToast(bolt11value+"-"+labelval+"-"+rMSatoshi);
        Log.e("abcd", bolt11value + "-" + labelval + "-" + rMSatoshi);
        TRANSACTION_LABEL = labelval;
        PayRequestToOtherFromServer payRequestToOtherFromServer = new PayRequestToOtherFromServer(MerchantBoostTerminal.this);
        if (Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            payRequestToOtherFromServer.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[]{new String(bolt11value), new String(rMSatoshi), new String(labelval)});
            payOtherProgressDialog.show();
            payOtherProgressDialog.setCancelable(false);
            payOtherProgressDialog.setCanceledOnTouchOutside(false);
        } else {
            payRequestToOtherFromServer.execute(new String[]{new String(bolt11value), new String(rMSatoshi), new String(labelval)});
            payOtherProgressDialog.show();
            payOtherProgressDialog.setCancelable(false);
            payOtherProgressDialog.setCanceledOnTouchOutside(false);
        }
    }

    private class PayRequestToOtherFromServer extends AsyncTask<String, Integer, String> {
        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;

        public PayRequestToOtherFromServer(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
        }

        protected void onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            int count = urls.length;
            String bolt11 = urls[0];
            String masatoshi = urls[1];
            String label = urls[2];
            String response = "";
            String mlattitude = "0.0";
            if (GlobalState.getInstance().getLattitude() != null) {
                mlattitude = GlobalState.getInstance().getLattitude();
            }
            String mlongitude = "0.0";
            if (GlobalState.getInstance().getLongitude() != null) {
                mlongitude = GlobalState.getInstance().getLongitude();
            }
            //Bolt 11, msatoshi, label
            String payToOtherQuery = "rpc-cmd,cli-node," + mlattitude + "_" + mlongitude + "," + getUnixTimeStamp() + ",[ pay " + bolt11 + " " + "null" + " " + label + " ]";
            Log.e("PayQuery:", payToOtherQuery);
            try {
                NetworkManager.getInstance().sendToServer(payToOtherQuery);
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            try {
                // Try now
                response = NetworkManager.getInstance().recvFromServer();
            } catch (Exception e) {
                Log.e(TAG, e.getLocalizedMessage());
            }
            //  int role= NetworkManager.getInstance().validateUser(tempdflUserId, tempdflPsswd);
            return response;
        }

        protected void onProgressUpdate(Integer... progress) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);
            String response = result;
          /*  resp,status,rpc-cmd,cli-node,[ {
                "destination": "02dc8590dd675b5bf89c6bdf9eeed767290b3d6056465e5b013756f65616d3d372",
                        "payment_hash": "8c8fb25e7a1851944f2f10974549fa0845fbd480dde33569e4382a99a2ccd59d",
                        "created_at": 1596655318.504,
                        "parts": 1,
                        "msatoshi": 965000,
                        "amount_msat": "965000msat",
                        "msatoshi_sent": 965000,
                        "amount_sent_msat": "965000msat",
                        "payment_preimage": "881d6ee425fcb0b670191b140742364d35a4fc51a831197709756886aed8e7d7",
                        "status": "complete"
            }
 ]*/
            Log.e(TAG, "PayOthers:" + result);
            if (response.contains("payment_hash")) {
                String[] split = response.split(",");
                String invoiceReponse = "";
                for (int i = 4; i < split.length; i++) {
                    invoiceReponse += "," + split[i];
                }
                invoiceReponse = invoiceReponse.substring(1);
                Pay payresponse = parseJSONForPayOthers(invoiceReponse);
                if (payresponse != null) {
                    if (payresponse.getStatus().equals("complete")) {
                        //showToast("Succefully Pay");
                        String body = "Amount:" + payresponse.getAmount_msat() + " Recived Succefully";
                        //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                        //quoc testing comment for new requirement
//                      addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                        //quoc testing comment for new requirement

                        //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                        //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                        showCofirmationDialog(payresponse);
                    } else {
                        Pay payresponse1 = new Pay();
//                        payresponse1.setCreated_at(239823892);
//                        payresponse1.setMsatoshi(12321212);
//                        payresponse1.setDestination("abasasasasasasasasasasasasasas");
//                        payresponse1.setPayment_preimage("asasasasasasasasasasasasasasas");
//                        payresponse1.setPayment_hash("asasasasasasasasasasasasasasasas");
//                        payresponse1.setStatus("complete");
                        payresponse1.setStatus("Not complete");
                        showCofirmationDialog(payresponse1);
                    }
                } else {
                    Pay payresponse2 = new Pay();
//                    payresponse2.setCreated_at(239823892);
//                    payresponse2.setMsatoshi(12321212);
//                    payresponse2.setDestination("abasasasasasasasasasasasasasas");
//                    payresponse2.setPayment_preimage("asasasasasasasasasasasasasasas");
//                    payresponse2.setPayment_hash("asasasasasasasasasasasasasasasas");
//                    payresponse2.setStatus("complete");
                    payresponse2.setStatus("Not complete");
                    showCofirmationDialog(payresponse2);
                }
                payOtherProgressDialog.dismiss();
            } else {
                Pay payresponse3 = new Pay();
//                payresponse3.setCreated_at(239823892);
//                payresponse3.setMsatoshi(12321212);
//                payresponse3.setDestination("abasasasasasasasasasasasasasas");
//                payresponse3.setPayment_preimage("asasasasasasasasasasasasasasas");
//                payresponse3.setPayment_hash("asasasasasasasasasasasasasasasas");
//                payresponse3.setStatus("complete");
                payresponse3.setStatus("Not complete");
                showCofirmationDialog(payresponse3);
                payOtherProgressDialog.dismiss();
            }
        }
    }

    private void updateMerchantMaxBoost(double amount_usd, double merchantAviableBoost) {
        double updatedMaxBoostMerchant = 0;
        updatedMaxBoostMerchant = merchantAviableBoost - amount_usd;
        //TODO:ABi merchcant id ko update krna he
        Call<MerchantUpdateResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).merchant_Update(merchantData.getId(), String.valueOf(updatedMaxBoostMerchant));
        call.enqueue(new Callback<MerchantUpdateResp>() {
            @Override
            public void onResponse(Call<MerchantUpdateResp> call, Response<MerchantUpdateResp> response) {
                if (response != null) {
                    if (response.body() != null) {
                        MerchantUpdateResp merchantUpdateResp = response.body();
                        if (merchantUpdateResp.getMerchantData() != null) {
                            merchantData = merchantUpdateResp.getMerchantData();
                            Double t = Double.parseDouble(merchantData.getMerchant_maxboost());
                            merchantMAxBoostval.setText("$" + String.format("%.2f", round(t, 2)));
                        }
                    }
                }
                Log.e("UpdateMerchantBoostLog", response.message());
            }

            @Override
            public void onFailure(Call<MerchantUpdateResp> call, Throwable t) {
                Log.e("UpdateMerchantBoostLog", t.getMessage().toString());
            }
        });
    }

    private void updateClientMaxBoost(double amount_usd, double clientAviableBoost) {
        double updatedMaxBoostClient = 0;
        updatedMaxBoostClient = clientAviableBoost - amount_usd;
        Call<ClientUpdateResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).client_Update(clientData.getId(), String.valueOf(updatedMaxBoostClient));
        call.enqueue(new Callback<ClientUpdateResp>() {
            @Override
            public void onResponse(Call<ClientUpdateResp> call, Response<ClientUpdateResp> response) {
                if (response != null) {
                    if (response.body() != null) {
                        ClientUpdateResp clientUpdateResp = response.body();
                        if (clientUpdateResp.getClientData() != null) {
                            clientData = clientUpdateResp.getClientData();
                            Double tt = Double.parseDouble(clientData.getClient_maxboost());
                            clientMaxBoostval.setText("$" + String.format("%.2f", round(tt, 2)));
                        }
                        Log.e("Test", "Test");
                    }
                }
                Log.e("UpdateClientBoostLog", response.message());
            }

            @Override
            public void onFailure(Call<ClientUpdateResp> call, Throwable t) {
                Log.e("UpdateClientBoostLog", t.getMessage().toString());
            }
        });
    }

    private void addInTransactionLog(double amount_usd, double amount_btc, Pay payresponse) {
        String amountUsd = String.valueOf(amount_usd);
        String amountBtc = String.valueOf(amount_btc);
        String label = TRANSACTION_LABEL;
        String clientId = clientData.getClient_id();
        String merchantID = merchantData.getMerchant_name();
        String transactionID = label;

//        String transactionTimeStamp=String.valueOf(payresponse.getCreated_at());
        //quoc testing
        String transactionTimeStamp = DateUtils.getCurrentDate();
        //quoc testing

        Call<TransactionResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).transction_add(label, transactionID, amountBtc, amountUsd, clientId, merchantID, transactionTimeStamp, String.valueOf(USD_TO_BTC_RATE));
        call.enqueue(new Callback<TransactionResp>() {
            @Override
            public void onResponse(Call<TransactionResp> call, Response<TransactionResp> response) {
                if (response != null) {
                    if (response.body() != null) {
                        TransactionResp transactionResp = response.body();
                        if (transactionResp.getMessage().equals("successfully done") && transactionResp.getTransactionInfo() != null) {
                            TransactionInfo transactionInfo = new TransactionInfo();
                            transactionInfo = transactionResp.getTransactionInfo();
                            merchantData.setMerchant_maxboost(transactionInfo.getMerchant_remaining());
                            Double t = Double.parseDouble(merchantData.getMerchant_maxboost());
                            merchantMAxBoostval.setText("$" + String.format("%.2f", round(t, 2)));
                            clientData.setClient_maxboost(transactionInfo.getClient_remaining());
                            Double tt = Double.parseDouble(clientData.getClient_maxboost());
                            clientMaxBoostval.setText("$" + String.format("%.2f", round(tt, 2)));
                            GlobalState.getInstance().setMainClientData(clientData);
                            GlobalState.getInstance().setMainMerchantData(merchantData);
                        } else {
                            showToast("Not Done!!");
                        }
                        Log.e("Test", "Test");
                    } else {
                        showToast(response.message());
                        Log.e("AddTransactionLog", response.message());
                    }
                }
                Log.e("AddTransactionLog", response.message());
            }

            @Override
            public void onFailure(Call<TransactionResp> call, Throwable t) {
                Log.e("AddTransactionLog", t.getMessage().toString());
            }
        });
    }

    private void showCofirmationDialog(Pay payresponse) {
        routingNodeExecute(clientNodeID222);
        updateMerchantClientMaxBoost();
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        commandeerRefundDialogstep2 = new Dialog(MerchantBoostTerminal.this);
        commandeerRefundDialogstep2.setContentView(R.layout.dialoglayoutrefundcommandeerlaststepconfirmedpay);
        Objects.requireNonNull(commandeerRefundDialogstep2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //commandeerRefundDialogstep2.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        //dialog.getWindow().setLayout(500, 500);
        commandeerRefundDialogstep2.setCancelable(false);
        final ImageView ivBack = commandeerRefundDialogstep2.findViewById(R.id.iv_back);
        final TextView textView = commandeerRefundDialogstep2.findViewById(R.id.textView2);
        final ImageView imCon = commandeerRefundDialogstep2.findViewById(R.id.ic_confirm);
        final Button ok = commandeerRefundDialogstep2.findViewById(R.id.btn_ok);
        final Button printBtn = commandeerRefundDialogstep2.findViewById(R.id.btn_print);
        textView.setText("Payment Status:" + payresponse.getStatus());
        if (payresponse.getStatus().equals("complete")) {
            imCon.setImageResource(R.drawable.ic_baseline_check_circle_24);
            if (pop2Dialog != null) {
                if (pop2Dialog.isShowing()) {
                    pop2Dialog.dismiss();
                }
            }
            InvoiceForPrint invoiceForPrint = new InvoiceForPrint();
            invoiceForPrint.setCreated_at(payresponse.getCreated_at());
            invoiceForPrint.setMsatoshi(payresponse.getMsatoshi());
            invoiceForPrint.setDestination(payresponse.getDestination());
            invoiceForPrint.setPayment_hash(payresponse.getPayment_hash());
            invoiceForPrint.setPayment_preimage(payresponse.getPayment_preimage());
            GlobalState.getInstance().setInvoiceForPrint(invoiceForPrint);
        }
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //routingNodeExecute(clientNodeID222);
                //updateMerchantClientMaxBoost();
//                openActivity(MainActivity.class);

                //quoc testing
                openActivity(MerchantLink.class);
                //quoc testing
                commandeerRefundDialogstep2.dismiss();

            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: DO Print Stuff HEre!!
                //:TODO : do what need to make print..
                if (payresponse.getStatus().equals("complete")) {
                    mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                    if (!mBluetoothAdapter.isEnabled()) {
                        dialogBoxForConnecctingBTPrinter();
                    } else {
                        if (mBluetoothSocket != null) {
                            Toast.makeText(MerchantBoostTerminal.this, "Already Connected", Toast.LENGTH_LONG).show();
                            try {
                                sendData();
                            } catch (IOException e) {
                                Log.e("SendDataError", e.toString());
                                e.printStackTrace();
                            }
                        } else {
                            dialogBoxForConnecctingBTPrinter();
                        }
                    }
                } else {
                    commandeerRefundDialogstep2.dismiss();
                }

            }
        });

        // progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandeerRefundDialogstep2.dismiss();
                openActivity(MainActivity.class);
            }
        });
        commandeerRefundDialogstep2.show();
    }

    private void dialogBoxForRefundCommandeerStep2(final String bolt11value, String msatoshi, String url, String pass, String fa2pass) {
        //quoc testing temp add
        commandeerRefundDialog.dismiss();
        //quoc testing temp add

        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        commandeerRefundDialogstep2 = new Dialog(MerchantBoostTerminal.this);
        commandeerRefundDialogstep2.setContentView(R.layout.dialoglayoutrefundcommandeerstep2);
        Objects.requireNonNull(commandeerRefundDialogstep2.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //commandeerRefundDialogstep2.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        //dialog.getWindow().setLayout(500, 500);
        commandeerRefundDialogstep2.setCancelable(false);
        String mst = msatoshi;
        double btc = mSatoshoToBtc(Double.valueOf(msatoshi));
        double priceInBTC = GlobalState.getInstance().getCurrentAllRate().getUSD().getLast();
        double usd = priceInBTC * btc;
        usd = round(usd, 2);
        mst = String.valueOf(usd);
        final TextView bolt11 = commandeerRefundDialogstep2.findViewById(R.id.bolt11valtxt);

        final TextView label = commandeerRefundDialogstep2.findViewById(R.id.labelvaltxt);
        final EditText amount = commandeerRefundDialogstep2.findViewById(R.id.amountval);
        amount.setText(mst);
        amount.setInputType(InputType.TYPE_NULL);
        final ImageView ivBack = commandeerRefundDialogstep2.findViewById(R.id.iv_back);
        Button excecute = commandeerRefundDialogstep2.findViewById(R.id.btn_next);
        bolt11.setText(bolt11value);
        label.setText("outgoingMaxBoost" + getUnixTimeStamp());
        if (msatoshi.equals("0.0")) {
            excecute.setVisibility(View.INVISIBLE);
        }
        //progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandeerRefundDialogstep2.dismiss();
            }
        });
        excecute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bolt11val = bolt11.getText().toString();
                String labelval = label.getText().toString();
                String amountval = amount.getText().toString();
                boolean status = true;
                if (bolt11val.isEmpty()) {
                    showToast("Bolt11 " + getString(R.string.empty));
                    status = false;
                    return;
                }
                if (labelval.isEmpty()) {
                    showToast("Label " + getString(R.string.empty));
                    status = false;
                    return;
                }
                if (status) {
                    //executeCommandeerRefundApi(bolt11val,labelval,amountval);
                    //quoc testing temp comment
//                    CommandRefundApi(bolt11val,labelval,amountval,url,pass,fa2pass);
//                    commandeerRefundDialogstep2.dismiss();
                    //quoc testing temp comment

                    //quoc testing add require password
                    Context c = MerchantBoostTerminal.this;
                    final Dialog enter2FaPassDialog;
                    enter2FaPassDialog = new Dialog(c);
                    enter2FaPassDialog.setContentView(R.layout.merchat_twofa_pass_lay);
                    Objects.requireNonNull(enter2FaPassDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    enter2FaPassDialog.setCancelable(false);
                    final EditText et_2Fa_pass = enter2FaPassDialog.findViewById(R.id.taskEditText);
                    final Button btn_confirm = enter2FaPassDialog.findViewById(R.id.btn_confirm);
                    final Button btn_cancel = enter2FaPassDialog.findViewById(R.id.btn_cancel);
                    final ImageView iv_back = enter2FaPassDialog.findViewById(R.id.iv_back);
                    et_2Fa_pass.setHint(R.string.enterboostpassword);

                    iv_back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            enter2FaPassDialog.dismiss();
                        }
                    });
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String task = String.valueOf(et_2Fa_pass.getText());
                            if (task.isEmpty()) {
                                goAlertDialogwithOneBTn(1, "", "Enter 2FA Password", "Ok", "");
                            } else {
                                if (task.equals(merchantData.getPassword())) {
                                    CommandRefundApi(bolt11val, labelval, amountval, url, pass, task);
                                    commandeerRefundDialogstep2.dismiss();
                                    enter2FaPassDialog.dismiss();
                                } else {
                                    goAlertDialogwithOneBTn(1, "", "Incorrect Password", "Retry", "");
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
                    //quoc testing add require password


                    //Confirmationn MSg
                }
            }
        });
        commandeerRefundDialogstep2.show();
    }

    private Pay parseJSONForPayOthers(String jsonString) {
        Pay pay = null;
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(jsonString);
        } catch (JSONException e) {
            // e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        JSONObject jsonObj = null;
        if (jsonArr != null) {
            try {
                jsonObj = jsonArr.getJSONObject(0);
            } catch (JSONException e) {
                //e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
        }
        if (jsonObj != null) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<Pay>() {
                }.getType();
                pay = gson.fromJson(jsonObj.toString(), type);
                // GlobalState.getInstance().setInvoice(pay);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }
        return pay;
    }

    private DecodePayBolt11 parseJSONForDecodePayBolt11(String jsonString) {
        DecodePayBolt11 decodePayBolt11 = null;
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(jsonString);
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        JSONObject jsonObj = null;
        if (jsonArr != null) {
            try {
                jsonObj = jsonArr.getJSONObject(0);
            } catch (JSONException e) {
                //e.printStackTrace();
                Log.e("Error", e.getMessage());
            }
        }
        if (jsonObj != null) {
            try {
                Gson gson = new Gson();
                Type type = new TypeToken<DecodePayBolt11>() {
                }.getType();
                decodePayBolt11 = gson.fromJson(jsonObj.toString(), type);
                GlobalState.getInstance().setCurrentDecodePayBolt11(decodePayBolt11);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }
        return decodePayBolt11;
    }

    private class GetInfoOFLineNode extends AsyncTask<String, Integer, String> {
        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;
        private ProgressDialog pdLoading;

        public GetInfoOFLineNode(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
            pdLoading = new ProgressDialog(this.parent);
            pdLoading.setMessage("Loading...");
        }

        protected void onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
            pdLoading.show();
            pdLoading.setCancelable(false);
            pdLoading.setCanceledOnTouchOutside(false);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            String response = "";
            int count = urls.length;
            String soverignLink = urls[0];
            String user = urls[1];
            String pass = urls[2];
            String clientMainNodeId = urls[3];
            String lat = "0.0";
            if (GlobalState.getInstance().getLattitude() != null) {
                lat = GlobalState.getInstance().getLattitude();
            }
            String lng = "0.0";
            if (GlobalState.getInstance().getLongitude() != null) {
                lng = GlobalState.getInstance().getLongitude();
            }
            String query = "rpc-cmd,cli-node," + lat + "_" + lng + "," + System.currentTimeMillis() / 1000 + ",[ listpeers " + clientMainNodeId + " ]";
            String[] ipport = soverignLink.split(":");
            String ip = ipport[0];
            int port = Integer.valueOf(ipport[1]);
            // port=18000;
            Boolean status = Boolean.valueOf(NetworkManager.getInstance().connectClient(ip, port));
            if (status) {
                int role = NetworkManager.getInstance().validateUser(user, pass);
                if (role == ADMINROLE) {
                    try {
                        NetworkManager.getInstance().sendToServer(query);
                    } catch (Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    try {
                        response = NetworkManager.getInstance().recvFromServer();
                    } catch (Exception e) {
                        Log.e(TAG, e.getLocalizedMessage());
                    }
                    if (response.contains("peers")) {
                        Log.e(TAG, response);
                        try {
                            NetworkManager.getInstance().sendToServer("bye");
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    } else {

                        try {
                            NetworkManager.getInstance().sendToServer("bye");
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                        response = "fail";
                    }
                } else {
                    response = "fail";
                }
            } else {
                response = "fail";
            }
            return response;
        }

        protected void onProgressUpdate(Integer... progress) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }

        protected void onPostExecute(String result) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);

            try {
                if (pdLoading != null && pdLoading.isShowing()) {
                    pdLoading.dismiss();
                }
            } catch (Exception e) {
                // e.printStackTrace();
                Log.e("Error", e.getMessage());
            }

            if (result.equals("fail")) {
                parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                //Toast.makeText(parent,result,Toast.LENGTH_SHORT).show();

            } else {
                String[] resaray = result.split(",");
                if (result.contains("peers")) {
                    if (resaray[0].contains("resp") && resaray[1].contains("status") && resaray[2].contains("rpc-cmd") && resaray[3].contains("cli-node")) {
                        String jsonresponse = "";
                        for (int i = 4; i < resaray.length; i++) {
                            jsonresponse += "," + resaray[i];
                        }
                        jsonresponse = jsonresponse.substring(1);
                        parseJSONForNodeLineInfor(jsonresponse);
                    } else {
                        parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                    }

                } else {
                    parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                }

            }
            Log.e(TAG, "LineNodeResultatPOST:" + result);
            count = count + 1;

            if (routingNodeArrayList != null) {
                if (routingNodeArrayList.size() >= count + 1) {
                    String soverignLink = routingNodeArrayList.get(count).getIp() + ":" + routingNodeArrayList.get(count).getPort();
                    String user = routingNodeArrayList.get(count).getUsername();
                    String pass = routingNodeArrayList.get(count).getPassword();
                    executeRoutingNodeCals(soverignLink, user, pass, clientNodeId);
                } else {

                    updatetheNodeList();
                }
            }
        }
    }

    private void updatetheNodeList() {
        ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
        if (tempList != null) {
            if (tempList.size() > 0) {
                for (int x = 0; x < tempList.size(); x++) {
                    if (tempList.get(x).isOn()) {
                        switch (x) {
                            case 0:
                                //line1maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(0).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE1_MAX_BOOST = 0;
                                for (int i = 0; i < tempList.get(0).getChannels().size(); i++) {
                                    LINE1_MAX_BOOST = LINE1_MAX_BOOST + mSatoshoToBtc(tempList.get(0).getChannels().get(i).getSpendable_msatoshi());
                                }
                                LINE1_MAX_BOOST = round(getUsdFromBtc(LINE1_MAX_BOOST), 2);
                                line1maxboostamount.setText("$" + String.format("%.2f", round(LINE1_MAX_BOOST, 2)));
                                line1maxboostamountBTC.setText(excatFigure(round(getBtcFromUsd(LINE1_MAX_BOOST), 9)) + "BTC");
                                isGetNodeLine1MaxBoost = true;
                                break;
                            case 1:
                                //line2maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(1).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE2_MAX_BOOST = 0;
                                for (int i = 0; i < tempList.get(1).getChannels().size(); i++) {
                                    LINE2_MAX_BOOST = LINE2_MAX_BOOST + mSatoshoToBtc(tempList.get(1).getChannels().get(i).getSpendable_msatoshi());
                                }

                                LINE2_MAX_BOOST = round(getUsdFromBtc(LINE2_MAX_BOOST), 2);
                                line2maxboostamount.setText("$" + String.format("%.2f", round(LINE2_MAX_BOOST, 2)));
                                line2maxboostamountBTC.setText(excatFigure(round(getBtcFromUsd(LINE2_MAX_BOOST), 9)) + "BTC");
                                isGetNodeLine2MaxBoost = true;
                                break;
                            case 2:
                                //line3maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(2).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE3_MAX_BOOST = 0;
                                for (int i = 0; i < tempList.get(2).getChannels().size(); i++) {
                                    LINE3_MAX_BOOST = LINE3_MAX_BOOST + mSatoshoToBtc(tempList.get(2).getChannels().get(i).getSpendable_msatoshi());
                                }

                                LINE3_MAX_BOOST = round(getUsdFromBtc(LINE3_MAX_BOOST), 2);
                                line3maxboostamount.setText("$" + String.format("%.2f", round(LINE3_MAX_BOOST, 2)));
                                line3maxboostamountBTC.setText(excatFigure(round(getBtcFromUsd(LINE3_MAX_BOOST), 9)) + "BTC");
                                isGetNodeLine3MaxBoost = true;
                                break;
                            case 3:
                                // line4maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(3).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE4_MAX_BOOST = 0;
                                for (int i = 0; i < tempList.get(3).getChannels().size(); i++) {
                                    LINE4_MAX_BOOST = LINE4_MAX_BOOST + mSatoshoToBtc(tempList.get(3).getChannels().get(i).getSpendable_msatoshi());
                                }

                                LINE4_MAX_BOOST = round(getUsdFromBtc(LINE4_MAX_BOOST), 2);


                                line4maxboostamount.setText("$" + String.format("%.2f", round(LINE4_MAX_BOOST, 2)));
                                line4maxboostamountBTC.setText(excatFigure(round(getBtcFromUsd(LINE4_MAX_BOOST), 9)) + "BTC");
                                isGetNodeLine4MaxBoost = true;
                                break;
                            default:
                                line1maxboostamount.setText("0.0");
                                line2maxboostamount.setText("0.0");
                                line3maxboostamount.setText("0.0");
                                line4maxboostamount.setText("0.0");
                                break;
                        }
                    }
                }
            }
        }
    }

    private void parseJSONForNodeLineInfor(String data) {
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(data);
        } catch (JSONException e) {
            // e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        JSONObject jsonObj = null;
        try {
            jsonObj = jsonArr.getJSONObject(0);
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        JSONArray ja_data = null;
        try {
            ja_data = jsonObj.getJSONArray("peers");
        } catch (JSONException e) {
            //e.printStackTrace();
            Log.e("Error", e.getMessage());
        }
        int length = jsonObj.length();
        JSONObject finalJSONobject = null;
        boolean sta = false;
        try {

            finalJSONobject = new JSONObject(String.valueOf(ja_data.getJSONObject(0)));
            sta = true;

        } catch (JSONException e) {
            sta = false;
            //e.printStackTrace();
            Log.e("error4", e.getMessage());
        }
        if (sta) {
            String temp1 = finalJSONobject.toString();
            Log.e("finalJSONobject", finalJSONobject.toString());
            NodeLineInfo nodeLineInfo = new NodeLineInfo();
            Gson gson = new Gson();
            boolean failed = false;
            try {
                nodeLineInfo = gson.fromJson(finalJSONobject.toString(), NodeLineInfo.class);
                //...
            } catch (IllegalStateException | JsonSyntaxException exception) {
                failed = true;
                //...
            }
            if (failed) {
            } else {
                nodeLineInfo.setOn(true);
                GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
                Log.e("Test1", "test");
            }
            //nodeLineInfo= gson.fromJson(finalJSONobject.toString(),NodeLineInfo .class);
        } else {
            NodeLineInfo nodeLineInfo = new NodeLineInfo();
            nodeLineInfo.setOn(false);
            GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
            ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
            Log.e("Test1", "test");
        }
        ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
        Log.e("Test1", "test");
        //  updateChannelInfo();
    }

    private void getAllRountingNodeList() {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

        Call<RoutingNodeListResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).get_Routing_Node_List();

        call.enqueue(new Callback<RoutingNodeListResp>() {
            @Override
            public void onResponse(Call<RoutingNodeListResp> call, Response<RoutingNodeListResp> response) {

                if (response.body() != null) {
                    if (response.body().getMessage().equals("successfully done")) {
                        if (response.body().getRoutingNodesList() != null) {
                            routingNodeArrayList = response.body().getRoutingNodesList();
                            if (routingNodeArrayList.size() > 0) {
                                GlobalState.getInstance().setNodeArrayList(routingNodeArrayList);
                                Log.e(TAG, "RoutingNodeListLenght:" + routingNodeArrayList.size());
                            } else {
                                Log.e(TAG, "RoutingNodeListZero");
                            }
                        } else {
                            Log.e(TAG, "RoutingNodeListEmpty");
                        }
                    } else {
                        st.toast(response.body().getMessage());
                    }
                }
                progressDialog.dismiss();
                getAllMerchantList();
            }

            @Override
            public void onFailure(Call<RoutingNodeListResp> call, Throwable t) {
                progressDialog.dismiss();
                st.toast("Network Error");
                getAllRountingNodeList();
                getAllMerchantList();
            }
        });
    }

    private void getNodesDataWithExecute(String url, String clientNodeId2, String accessToken) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new ChuckerInterceptor.Builder(this)
                                .build()
                )
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {

            Map<String, String> requestBody1 = new HashMap<>();
            requestBody1.put("cmd", clientNodeId2.trim());
            Call<NodesDataWithExecuteResponse> call1 = apiInterface.getNodesDataWithExecute("Bearer " + accessToken, requestBody1);
            call1.enqueue(new Callback<NodesDataWithExecuteResponse>() {
                @Override
                public void onResponse(Call<NodesDataWithExecuteResponse> call, Response<NodesDataWithExecuteResponse> response) {
                    try {

                        String firstName = response.body().nodesDataWithExecuteData.stdout;
//                        firstName = firstName.replaceAll("\\s", "");
//                        firstName= firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
//                        firstName=firstName.substring(2);
//                        firstName= firstName.replaceAll("b'", "");
//                        firstName= firstName.replaceAll("',", "");
//                        firstName= firstName.replaceAll("'", "");
//                        firstName = firstName.substring(0,firstName.length() -1);
//                        if (!firstName.contains("code")){
//
//                        }else {
//                            firstName= firstName.replaceAll("\\\\", "");
//                            JSONObject object2=new JSONObject(firstName);
//                            String message=object2.getString("message");
//                            st.toast(message);
//                            progressDialog.dismiss();
//
//                        }

                        JSONObject object2 = new JSONObject(firstName);
                        JSONArray ja_data = null;
                        try {
                            ja_data = object2.getJSONArray("peers");
                        } catch (JSONException e) {
                            progressDialog.dismiss();
                            //e.printStackTrace();
                            Log.e("Error", e.getMessage());
                        }
                        if (ja_data.length() > 0) {
                            JSONObject finalJSONobject = null;
                            boolean sta = false;
                            try {
                                finalJSONobject = new JSONObject(String.valueOf(ja_data.getJSONObject(0)));
                                sta = true;
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                sta = false;
                                //e.printStackTrace();
                                Log.e("error4", e.getMessage());
                            }
                            if (sta) {
                                String temp1 = finalJSONobject.toString();
                                Log.e("finalJSONobject", finalJSONobject.toString());
                                NodeLineInfo nodeLineInfo = new NodeLineInfo();
                                Gson gson = new Gson();
                                boolean failed = false;
                                try {
                                    nodeLineInfo = gson.fromJson(finalJSONobject.toString(), NodeLineInfo.class);
                                    //...
                                } catch (IllegalStateException | JsonSyntaxException exception) {

                                    failed = true;
                                    //...
                                }
                                if (failed) {
                                    //quoc testing add fake noteLineInfo
                                    NodeLineInfo nodeLineInfo1 = new NodeLineInfo();
                                    GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo1);
                                    //quoc testin add fake noteLineInfo
                                    //progressDialog.dismiss();
                                } else {
                                    // progressDialog.dismiss();
                                    nodeLineInfo.setOn(true);
                                    GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                                    ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
                                    Log.e("Test1", "test");
                                }
                                count = count + 1;
                                if (routingNodeArrayList != null) {
                                    if (routingNodeArrayList.size() >= count + 1) {
                                        String soverignLink = routingNodeArrayList.get(count).getIp() + ":" + routingNodeArrayList.get(count).getPort();
                                        String user = routingNodeArrayList.get(count).getUsername();
                                        String pass = routingNodeArrayList.get(count).getPassword();
                                        getNodesDataWithExecute(soverignLink, "listpeers " + clientNodeId + " ", accessToken);
                                    } else {
                                        progressDialog.dismiss();
                                        updatetheNodeList();
                                    }
                                } else {
                                    progressDialog.dismiss();
                                }
                            } else {
                                progressDialog.dismiss();
                                NodeLineInfo nodeLineInfo = new NodeLineInfo();
                                nodeLineInfo.setOn(false);
                                GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                                ArrayList<NodeLineInfo> tempList = GlobalState.getInstance().getNodeLineInfoArrayList();
                                Log.e("Test1", "test");
                            }
                        } else if (ja_data.equals("[]")) {
                            count = count + 1;
                            //quoc testing add fake noteLineInfo
                            NodeLineInfo nodeLineInfo = new NodeLineInfo();
                            GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                            //quoc testin add fake noteLineInfo
                            if (routingNodeArrayList != null) {
                                if (routingNodeArrayList.size() >= count + 1) {
                                    String soverignLink = routingNodeArrayList.get(count).getIp() + ":" + routingNodeArrayList.get(count).getPort();
                                    String user = routingNodeArrayList.get(count).getUsername();
                                    String pass = routingNodeArrayList.get(count).getPassword();
                                    getNodesDataWithExecute(soverignLink, "listpeers " + clientNodeId + " ", accessToken);
                                } else {
                                    progressDialog.dismiss();
                                    updatetheNodeList();
                                }
                            } else {
                                progressDialog.dismiss();
                            }

                        } else {
                            // progressDialog.dismiss();
                            //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            count = count + 1;
                            //quoc testing add fake noteLineInfo
                            NodeLineInfo nodeLineInfo = new NodeLineInfo();
                            GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                            //quoc testin add fake noteLineInfo
                            if (routingNodeArrayList != null) {
                                if (routingNodeArrayList.size() >= count + 1) {
                                    String soverignLink = routingNodeArrayList.get(count).getIp() + ":" + routingNodeArrayList.get(count).getPort();
                                    String user = routingNodeArrayList.get(count).getUsername();
                                    String pass = routingNodeArrayList.get(count).getPassword();
                                    getNodesDataWithExecute(soverignLink, "listpeers " + clientNodeId + " ", accessToken);
                                } else {
                                    progressDialog.dismiss();
                                    updatetheNodeList();
                                }
                            } else {
                                progressDialog.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();

                        // progressDialog.dismiss();
                        //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                        count = count + 1;
                        //quoc testing add fake noteLineInfo
                        NodeLineInfo nodeLineInfo = new NodeLineInfo();
                        GlobalState.getInstance().addInNodeLineInfoArrayList(nodeLineInfo);
                        //quoc testin add fake noteLineInfo
                        if (routingNodeArrayList != null) {
                            if (routingNodeArrayList.size() >= count + 1) {
                                String soverignLink = routingNodeArrayList.get(count).getIp() + ":" + routingNodeArrayList.get(count).getPort();
                                String user = routingNodeArrayList.get(count).getUsername();
                                String pass = routingNodeArrayList.get(count).getPassword();
                                getNodesDataWithExecute(soverignLink, "listpeers " + clientNodeId + " ", accessToken);
                            } else {
                                progressDialog.dismiss();
                                updatetheNodeList();
                            }
                        } else {
                            progressDialog.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<NodesDataWithExecuteResponse> call, Throwable t) {
                    Log.e("TAG", "onResponse: " + t.getMessage().toString());
                    progressDialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.dismiss();
        }
    }

    private void getARoutingAPIAuth1(String merchantId, String merchantBackendPassword, String url, String clientNodeId2) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("merchant_id", merchantId);
        requestBody.put("merchant_backend_password", merchantBackendPassword);
        Call<ARoutingAPIAuthResponse> call = ApiClient.getRetrofit(this).create(ApiInterface.class).getRoutingAPIAuth1(requestBody);
        call.enqueue(new Callback<ARoutingAPIAuthResponse>() {
            @Override
            public void onResponse(Call<ARoutingAPIAuthResponse> call, Response<ARoutingAPIAuthResponse> response) {
                if (response.body() != null) {
                    MerchantBoostTerminal.this.authLevel1 = response.body().aRoutingAPIAuthData.accessToken;
                    getNodesDataWithExecute(url, clientNodeId2, response.body().aRoutingAPIAuthData.accessToken);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ARoutingAPIAuthResponse> call, Throwable t) {
                progressDialog.dismiss();
                getAllMerchantList();
                st.toast("Error: " + t.getMessage());
            }
        });
    }

    private void getARoutingAPIAuth2(String merchantId, String boost2FAPassword, String url, String clientNodeId2) {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("merchant_id", merchantId);
        requestBody.put("boost_2fa_password", boost2FAPassword);
        Call<ARoutingAPIAuthResponse> call = ApiClient.getRetrofit(this).create(ApiInterface.class).getRoutingAPIAuth2(requestBody);
        call.enqueue(new Callback<ARoutingAPIAuthResponse>() {
            @Override
            public void onResponse(Call<ARoutingAPIAuthResponse> call, Response<ARoutingAPIAuthResponse> response) {
                if (response.body() != null) {
                    MerchantBoostTerminal.this.authLevel2 = response.body().aRoutingAPIAuthData.accessToken;
//                    getNodesDataWithExecute(url,clientNodeId2, response.body().aRoutingAPIAuthData.accessToken);

                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ARoutingAPIAuthResponse> call, Throwable t) {
                progressDialog.dismiss();
                getAllMerchantList();
                st.toast("Error: " + t.getMessage());
            }
        });
    }

    private void getAllMerchantList() {
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        Call<MerchantListResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).get_Merchant_List();
        call.enqueue(new Callback<MerchantListResp>() {
            @Override
            public void onResponse(Call<MerchantListResp> call, Response<MerchantListResp> response) {
                if (response.body() != null) {
                    if (response.body().getMessage().equals("successfully done")) {
                        if (response.body().getMerchantDataList() != null) {
                            allMerchantDataList = response.body().getMerchantDataList();
                            if (allMerchantDataList.size() > 0) {
                                GlobalState.getInstance().setAllMerchantDataList(allMerchantDataList);
                                Log.e(TAG, "MerchantListSize:" + allMerchantDataList.size());
                            } else {
                                Log.e(TAG, "MerchantListSizeZero");
                            }
                        } else {
                            Log.e(TAG, "MerchantListSizeEmpty");
                        }
                    } else {
                        st.toast(response.body().getMessage());
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MerchantListResp> call, Throwable t) {
                progressDialog.dismiss();
                st.toast("Error: " + t.getMessage());
            }
        });
    }

    private void getBitCoinValue() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        Call<CurrentAllRate> call = retrofit.create(ApiInterface.class).getBitCoin();

        call.enqueue(new Callback<CurrentAllRate>() {
            @Override
            public void onResponse(Call<CurrentAllRate> call, Response<CurrentAllRate> response) {

                CurrentAllRate bitRate2 = new CurrentAllRate();
                bitRate2 = response.body();
                GlobalState.getInstance().setCurrentAllRate(bitRate2);
                CurrentAllRate tem = GlobalState.getInstance().getCurrentAllRate();
                bitCoinValue = bitRate2.getUSD().getLast();
            }

            @Override
            public void onFailure(Call<CurrentAllRate> call, Throwable t) {
                st.toast("bitcoin fail: " + t.getMessage());
            }
        });
    }

    private void getFundingNodeInfor() {
        Call<FundingNodeListResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).get_Funding_Node_List();

        call.enqueue(new Callback<FundingNodeListResp>() {
            @Override
            public void onResponse(Call<FundingNodeListResp> call, Response<FundingNodeListResp> response) {

                if (response.body() != null) {
                    FundingNode fundingNode = response.body().getFundingNodesList().get(0);
                    //TODO:Add new Field in db for registartion fees
                    // fundingNode.setRegistration_fees(10);
                    GlobalState.getInstance().setFundingNode(fundingNode);
                }
            }

            @Override
            public void onFailure(Call<FundingNodeListResp> call, Throwable t) {
                st.toast("RPC Failed Try Again");
            }
        });
    }

    private void scannerIntent() {
        mode = 1;
        IntentIntegrator qrScan;
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        String prompt = "Scan Client Node ID";
        qrScan.setPrompt(prompt);
        qrScan.initiateScan();
    }

    private void scannerIntent2() {
        mode = 3;
        IntentIntegrator qrScan;
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        String prompt = "Scan Bolt11";
        qrScan.setPrompt(prompt);
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                if (mode == 3) {

                    //quoc testing temp comment
//                    commandeerRefundDialog.dismiss();
                    //quoc testing temp comment

                    bolt11fromqr = result.getContents();
                    // showToast(bolt11fromqr);
                    //decodeBolt11(bolt11fromqr);
                    decodeBolt1122(bolt11fromqr, "");
                } else if (mode == 1) {
                    //Toast.makeText(this, "Result Found", Toast.LENGTH_LONG).show();
                    clientNodeId = result.getContents();
                    if (!clientNodeId.isEmpty()) {
                        clientNodeID222 = clientNodeId;
                        routingNodeExecute(clientNodeId);
                    }
                } else if (mode == REQUEST_ENABLE_BT) {
                    if (resultCode == Activity.RESULT_OK) {
                        ListPairedDevices();
                        initials();
                    } else {
                        Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void showToast(String message) {
        Toast.makeText(MerchantBoostTerminal.this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendEmailFromClass(String subject, String message) {
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, Utils.toEmail, subject, message);
        javaMailAPI.execute();
        JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, clientData.getEmail(), subject, message);
        javaMailAPI2.execute();
        JavaMailAPI javaMailAPI3 = new JavaMailAPI(this, merchantData.getEmail(), subject, message);
        javaMailAPI3.execute();
    }

    private void updateMerchantClientMaxBoost() {
        merchantData = GlobalState.getInstance().getMainMerchantData();
        clientData = GlobalState.getInstance().getMainClientData();
        //TODO:For Merchant Data Update
        if (merchantData != null) {
            Double t = Double.parseDouble(merchantData.getMerchant_maxboost());
            merchantMAxBoostval.setText("$" + String.format("%.2f", round(t, 2)));

        } else {
            merchantMAxBoostval.setText("$0");
        }
        //TODO:For Client Data Update
        if (clientData != null) {
            Double tt = Double.parseDouble(clientData.getClient_maxboost());
            clientMaxBoostval.setText("$" + String.format("%.2f", round(tt, 2)));

        } else {
            clientMaxBoostval.setText("$0");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        t = false;
        timeStampOfPause = System.currentTimeMillis();
//        new CountDownTimer((secs +1) * 1000, 1000) // Wait 5 secs, tick every 1 sec
//        {
//            @Override
//            public final void onTick(final long millisUntilFinished)
//            {
//
//            }
//            @Override
//            public final void onFinish()
//            {
//                t=true;
//                finish();
//            }
//        }.start();
        // Toast.makeText(this,"pause",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timeStampOfPause == 0) {
            //do nothing
            Log.e("OnResume", "1st Time Do Nothing");
        } else {
            long diffInMillisec = System.currentTimeMillis() - timeStampOfPause;
            long diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec);
            long seconds = diffInSec % 60;
            diffInSec /= 60;
            long minutes = diffInSec % 60;
            diffInSec /= 60;
            long hours = diffInSec % 24;
            //quoc testing, quoc change from 3 to 5 for new requirement
            if (minutes > 5) {
                Log.e("OnResume", "2st Time Do SomeThing");
                startActivity(new Intent(MerchantBoostTerminal.this, MerchantLink.class));
            } else {
                //st.toast("no");
                Log.e("OnResume", "2st Time Do Nothing");
            }
        }
        // Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog = new Dialog(MerchantBoostTerminal.this);
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout);
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        goAlertDialogwithOneBTnDialog.setCancelable(false);
        final TextView alertTitle_tv = goAlertDialogwithOneBTnDialog.findViewById(R.id.alertTitle);
        final TextView alertMessage_tv = goAlertDialogwithOneBTnDialog.findViewById(R.id.alertMessage);
        final Button yesbtn = goAlertDialogwithOneBTnDialog.findViewById(R.id.yesbtn);
        final Button nobtn = goAlertDialogwithOneBTnDialog.findViewById(R.id.nobtn);
        yesbtn.setText("Yes");
        nobtn.setText("No");
        alertTitle_tv.setText("");
        alertMessage_tv.setText("Are you sure you want to exit?");
        alertTitle_tv.setVisibility(View.GONE);
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

    //TODO:PRINTING RELATED TASKS!
    private void initials() {
        ProgressBar tv_prgbar = blutoothDevicesDialog.findViewById(R.id.printerProgress);
        tv_prgbar.setVisibility(View.VISIBLE);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        mPairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        ListView t_blueDeviceListView = blutoothDevicesDialog.findViewById(R.id.blueDeviceListView);
        t_blueDeviceListView.setAdapter(mPairedDevicesArrayAdapter);
        t_blueDeviceListView.setOnItemClickListener(mDeviceClickListener);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter.getBondedDevices();

        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                mPairedDevicesArrayAdapter.add(mDevice.getName() + "\n" + mDevice.getAddress());
            }
        } else {
            String mNoDevices = "None Paired";
            mPairedDevicesArrayAdapter.add(mNoDevices);
        }
        tv_prgbar.setVisibility(View.GONE);
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> mAdapterView, View mView, int mPosition, long mLong) {
            TextView tv_status = blutoothDevicesDialog.findViewById(R.id.tv_status);
            ProgressBar tv_prgbar = blutoothDevicesDialog.findViewById(R.id.printerProgress);
            try {
                tv_prgbar.setVisibility(View.VISIBLE);
                tv_status.setText("Device Status:Connecting....");
                mBluetoothAdapter.cancelDiscovery();
                String mDeviceInfo = ((TextView) mView).getText().toString();
                String mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length() - 17);
                mBluetoothDevice = mBluetoothAdapter.getRemoteDevice(mDeviceAddress);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Code here will run in UI thread
                        TextView tv_status = blutoothDevicesDialog.findViewById(R.id.tv_status);
                        ProgressBar tv_prgbar = blutoothDevicesDialog.findViewById(R.id.printerProgress);

                        try {
                            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(applicationUUID);
                            mBluetoothAdapter.cancelDiscovery();
                            mBluetoothSocket.connect();
                            tv_status.setText("Device Status:Connected");
                            //controlLay(1);
                            tv_prgbar.setVisibility(View.GONE);
                            blutoothDevicesDialog.dismiss();
                        } catch (IOException eConnectException) {
                            tv_status.setText("Device Status:Try Again");
                            tv_prgbar.setVisibility(View.GONE);
                            Log.e("ConnectError", eConnectException.toString());
                            closeSocket(mBluetoothSocket);
                            //controlLay(0);
                        }
                    }
                });
            } catch (Exception ex) {
                Log.e("ConnectError", ex.toString());
            }
        }
    };

    private void dialogBoxForConnecctingBTPrinter() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        blutoothDevicesDialog = new Dialog(this);
        blutoothDevicesDialog.setContentView(R.layout.blutoothdevicelistdialoglayout);
        Objects.requireNonNull(blutoothDevicesDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        blutoothDevicesDialog.getWindow().setLayout((int) (width / 1.1f), (int) (height / 1.3));
        //dialog.getWindow().setLayout(500, 500);
        blutoothDevicesDialog.setCancelable(false);
        //init dialog views
        final ImageView ivBack = blutoothDevicesDialog.findViewById(R.id.iv_back);
        final Button scanDevices = blutoothDevicesDialog.findViewById(R.id.btn_scanDevices);
        TextView tv_status = blutoothDevicesDialog.findViewById(R.id.tv_status);
        ListView blueDeviceListView = blutoothDevicesDialog.findViewById(R.id.blueDeviceListView);
        initials();
        scanDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initials();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blutoothDevicesDialog.dismiss();
            }
        });
        blutoothDevicesDialog.show();
    }

    private void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d("", "SocketClosed");
        } catch (IOException ex) {
            Log.d("", "CouldNotCloseSocket");
        }
    }

    private void ListPairedDevices() {
        Set<BluetoothDevice> mPairedDevices = mBluetoothAdapter
                .getBondedDevices();
        if (mPairedDevices.size() > 0) {
            for (BluetoothDevice mDevice : mPairedDevices) {
                Log.v("", "PairedDevices: " + mDevice.getName() + "  "
                        + mDevice.getAddress());
            }
        }
    }

    void sendData() throws IOException {
        String x = sp.getStringValue("merchant_id");
        String merchantId = "test";
        if (x != null) {
            merchantId = x;
        }
        try {
            btoutputstream = mBluetoothSocket.getOutputStream();
            // the text typed by the user
            InvoiceForPrint recInvoiceForPrint = GlobalState.getInstance().getInvoiceForPrint();
            DecimalFormat precision = new DecimalFormat("0.00");
            if (recInvoiceForPrint != null) {
                final String amount = excatFigure(round((mSatoshoToBtc(recInvoiceForPrint.getMsatoshi())), 9)) + "BTC/$" + precision.format(round(getUsdFromBtc(mSatoshoToBtc(recInvoiceForPrint.getMsatoshi())), 2)) + "USD";
                final String amountInBtc = excatFigure(round((mSatoshoToBtc(recInvoiceForPrint.getMsatoshi())), 9)) + "BTC";
                final String amountInUsd = precision.format(round(getUsdFromBtc(mSatoshoToBtc(recInvoiceForPrint.getMsatoshi())), 2)) + "USD";
                final Bitmap bitmapPaymentPreImage = getBitMapFromHex(recInvoiceForPrint.getPayment_preimage());
                final Bitmap bitmapPaymentHash = getBitMapFromHex(recInvoiceForPrint.getPayment_hash());
                final Bitmap bitmapDestination = getBitMapFromHex(recInvoiceForPrint.getDestination());
                double tem = recInvoiceForPrint.getCreated_at();
                long actTime = (long) tem;
                actTime = Math.round(tem);
                final String creatAtTime = getDateFromUTCTimestamp2(actTime, AppConstants.OUTPUT_DATE_FORMATE);
                printingProgressBar.show();
                printingProgressBar.setCancelable(false);
                printingProgressBar.setCanceledOnTouchOutside(false);
                String finalMerchantId = merchantId;
                Thread t = new Thread() {
                    public void run() {
                        try {
                            // This is printer specific code you can comment ==== > Start
                            btoutputstream.write(PrinterCommands.reset);
                            btoutputstream.write(PrinterCommands.INIT);
                            btoutputstream.write(PrinterCommands.FEED_LINE);
                            btoutputstream.write("\n\n".getBytes());
                            //Items title should Center
                            btoutputstream.write("\tBoost Charge".getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\t------------".getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\tStatus:".getBytes());
                            btoutputstream.write("Complete".getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\tMerchant Id:".getBytes());
                            btoutputstream.write(finalMerchantId.getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\tCharge Time:".getBytes());
                            btoutputstream.write("\n\t".getBytes());
                            btoutputstream.write(creatAtTime.getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\tAmount: ".getBytes());
                            btoutputstream.write("\n\t".getBytes());
                            btoutputstream.write(amountInBtc.getBytes());
                            btoutputstream.write("\n\t".getBytes());
                            btoutputstream.write(amountInUsd.getBytes());
                            btoutputstream.write("\n".getBytes());
                            btoutputstream.write("\tDestination:".getBytes());
                            if (bitmapDestination != null) {
                                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmapDestination, 250, 250, true);
                                new ByteArrayOutputStream();
                                PrintPic printPic = PrintPic.getInstance();
                                printPic.init(bMapScaled);
                                byte[] bitmapdata = printPic.printDraw();
                                btoutputstream.write(PrinterCommands.print);
                                btoutputstream.write(bitmapdata);
                                btoutputstream.write(PrinterCommands.print);

                            }
                            btoutputstream.write("-------------------------------\n".getBytes());
                            btoutputstream.write("\tPayment Hash:".getBytes());
                            if (bitmapPaymentHash != null) {
                                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmapPaymentHash, 250, 250, true);
                                new ByteArrayOutputStream();
                                PrintPic printPic = PrintPic.getInstance();
                                printPic.init(bMapScaled);
                                byte[] bitmapdata = printPic.printDraw();
                                btoutputstream.write(PrinterCommands.print);
                                btoutputstream.write(bitmapdata);
                                btoutputstream.write(PrinterCommands.print);

                            }
                            btoutputstream.write("-------------------------------\n".getBytes());
                            btoutputstream.write("\tPayment Preimage:".getBytes());
                            if (bitmapPaymentPreImage != null) {
                                Bitmap bMapScaled = Bitmap.createScaledBitmap(bitmapPaymentPreImage, 250, 250, true);
                                new ByteArrayOutputStream();
                                PrintPic printPic = PrintPic.getInstance();
                                printPic.init(bMapScaled);
                                byte[] bitmapdata = printPic.printDraw();
                                btoutputstream.write(PrinterCommands.print);
                                btoutputstream.write(bitmapdata);
                                btoutputstream.write(PrinterCommands.print);

                            }
                            Thread.sleep(1000);
                            printingProgressBar.dismiss();
                            //confirmPaymentDialog.dismiss();
                        } catch (Exception e) {
                            Log.e("PrintError", "Exe ", e);
                        }
                    }
                };
                t.start();
            } else {
                btoutputstream.write(PrinterCommands.reset);
                btoutputstream.write(PrinterCommands.INIT);
                btoutputstream.write(PrinterCommands.FEED_LINE);
                String paidAt = "\n\n\n\nNot Data Found\n\n\n";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // commandeerRefundDialogstep2.dismiss();
    }

    protected void printNewLine() {
        try {
            btoutputstream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goAlertDialogwithOneBTn(int i, String alertTitleMessage, String alertMessage, String alertBtn1Message, String alertBtn2Message) {
        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog = new Dialog(MerchantBoostTerminal.this);
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout);
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        goAlertDialogwithOneBTnDialog.setCancelable(false);
        final TextView alertTitle_tv = goAlertDialogwithOneBTnDialog.findViewById(R.id.alertTitle);
        final TextView alertMessage_tv = goAlertDialogwithOneBTnDialog.findViewById(R.id.alertMessage);
        final Button yesbtn = goAlertDialogwithOneBTnDialog.findViewById(R.id.yesbtn);
        final Button nobtn = goAlertDialogwithOneBTnDialog.findViewById(R.id.nobtn);
        yesbtn.setText(alertBtn1Message);
        nobtn.setText(alertBtn2Message);
        alertTitle_tv.setText(alertTitleMessage);
        alertMessage_tv.setText(alertMessage);
        if (i == 1) {
            nobtn.setVisibility(View.GONE);
            alertTitle_tv.setVisibility(View.GONE);
        } else {

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