package com.sis.clighteningboost.Activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.google.zxing.integration.android.IntentIntegrator
import com.sis.clighteningboost.Adapter.MerchantItemAdapter
import com.sis.clighteningboost.Api.ApiClient
import com.sis.clighteningboost.Api.ApiClientForNode
import com.sis.clighteningboost.Api.ApiInterface
import com.sis.clighteningboost.Api.ApiInterfaceForNodes
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse
import com.sis.clighteningboost.Models.NodesDataWithExecuteResponse
import com.sis.clighteningboost.Models.REST.*
import com.sis.clighteningboost.Models.RPC.DecodePayBolt11
import com.sis.clighteningboost.Models.RPC.InvoiceForPrint
import com.sis.clighteningboost.Models.RPC.NodeLineInfo
import com.sis.clighteningboost.Models.RPC.Pay
import com.sis.clighteningboost.R
import com.sis.clighteningboost.RPC.NetworkManager
import com.sis.clighteningboost.utils.*
import com.sis.clighteningboost.utils.DateUtils.currentDate
import com.sis.clighteningboost.utils.Print.PrintPic
import com.sis.clighteningboost.utils.Print.PrinterCommands
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.gusavila92.websocketclient.WebSocketClient
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream
import java.lang.Boolean
import java.net.URI
import java.net.URISyntaxException
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.Any
import kotlin.ByteArray
import kotlin.Double
import kotlin.Exception
import kotlin.IllegalStateException
import kotlin.Int
import kotlin.Long
import kotlin.NullPointerException
import kotlin.String
import kotlin.Throwable
import kotlin.Throws
import kotlin.Unit
import kotlin.arrayOf
import kotlin.let
import kotlin.plus
import kotlin.toString

class MerchantBoostTerminal : BaseActivity() {
    var t = false
    override var sp: SharedPreference? = null
    var merchant_id_tv: TextView? = null
    var client_id_tv: TextView? = null
    var st: StaticClass? = null
    var clientnodeid: EditText? = null
    var progressDialog: ProgressDialog? = null
    var routingNodeArrayList: ArrayList<RoutingNode>? = ArrayList()
    private var allMerchantDataList = ArrayList<MerchantData>()
    var clientNodeId = ""
    var recyclerView: RecyclerView? = null
    var merchantItemAdapter: MerchantItemAdapter? = null
    var isGetNodeLine1MaxBoost = false
    var isGetNodeLine2MaxBoost = false
    var isGetNodeLine3MaxBoost = false
    var isGetNodeLine4MaxBoost = false
    var pop2Dialog: Dialog? = null
    var line1maxboostamount: TextView? = null
    var line1maxboostamountBTC: TextView? = null
    var line2maxboostamount: TextView? = null
    var line2maxboostamountBTC: TextView? = null
    var line3maxboostamount: TextView? = null
    var line3maxboostamountBTC: TextView? = null
    var line4maxboostamount: TextView? = null
    var line4maxboostamountBTC: TextView? = null
    var clientMaxBoostval: TextView? = null
    var merchantMAxBoostval: TextView? = null
    var count = 0
    private var merchantData: MerchantData? = null
    private var clientData: ClientData? = null
    private var AMOUNT_BTC = 0.0
    private var AMOUNT_USD = 0.0
    private var BOOST_CHARG_BTC = 0.0
    private var BOOST_CHARG_USD = 0.0
    private var TOTAL_AMOUNT_BTC = 0.0
    private var TOTAL_AMOUNT_USD = 0.0
    private var USD_TO_BTC_RATE = 0.0
    private var TRANSACTION_LABEL = ""
    private var LINE1_MAX_BOOST = 0.0
    private var LINE2_MAX_BOOST = 0.0
    private var LINE3_MAX_BOOST = 0.0
    private var LINE4_MAX_BOOST = 0.0
    private val merchant_IDd = ""
    private var timeStampOfPause: Long = 0

    //listchannels null [node id]
    //BOLT 11 Qr Scan Stuff
    var scannerID = 0
    var commandeerRefundDialog: Dialog? = null
    var commandeerRefundDialogstep2: Dialog? = null
    var bolt11fromqr = ""
    var decodePayBolt11ProgressDialog: ProgressDialog? = null
    var payOtherProgressDialog: ProgressDialog? = null
    var mode = 0
    var clientImage: ImageView? = null
    var clientNodeID222 = ""
    var merchantId: String? = ""
    var mBluetoothAdapter: BluetoothAdapter? = null
    private val applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var mBluetoothSocket: BluetoothSocket? = null
    var mBluetoothDevice: BluetoothDevice? = null
    var printstat = 0
    private var mPairedDevicesArrayAdapter: ArrayAdapter<String>? = null
    var printingProgressBar: ProgressDialog? = null
    var blutoothDevicesDialog: Dialog? = null
    var setTextWithSpan: TextView? = null
    var rl_enterNodeIdLayer: RelativeLayout? = null
    var rl_lineNodeInfo: RelativeLayout? = null
    private var authLevel1: String? = null
    private var authLevel2: String? = null
    private var invoiceId: String? = null
    private var lockTimeStamp: String? = null
    private var receivingNodeId: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lockTimeStamp = ((Date().time / 1000).toString())
        setContentView(R.layout.activity_merchant_boost_terminal2)
        sp = SharedPreference(this, "local_data")
        st = StaticClass(this)
        printingProgressBar = ProgressDialog(this@MerchantBoostTerminal)
        merchant_id_tv = findViewById(R.id.merchant_id_tv)
        client_id_tv = findViewById(R.id.client_id_tv)
        rl_lineNodeInfo = findViewById(R.id.rl_lineNodeInfo)
        rl_enterNodeIdLayer = findViewById(R.id.rl_enterNodeIdLayer)
        line1maxboostamount = findViewById(R.id.line1maxboostamount)
        line2maxboostamount = findViewById(R.id.line2maxboostamount)
        line3maxboostamount = findViewById(R.id.line3maxboostamount)
        line4maxboostamount = findViewById(R.id.line4maxboostamount)
        line1maxboostamountBTC = findViewById(R.id.line1maxboostamountBTC)
        line2maxboostamountBTC = findViewById(R.id.line2maxboostamountBTC)
        line3maxboostamountBTC = findViewById(R.id.line3maxboostamountBTC)
        line4maxboostamountBTC = findViewById(R.id.line4maxboostamountBTC)
        clientMaxBoostval = findViewById(R.id.client_maxboost)
        merchantMAxBoostval = findViewById(R.id.merchant_maxboost)
        clientImage = findViewById(R.id.iv_client)
        merchantId = sp!!.getStringValue("merchant_id")
        merchant_id_tv?.setText(sp!!.getStringValue("merchant_id"))
        client_id_tv?.setText(sp!!.getStringValue("client_name"))
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading")
        decodePayBolt11ProgressDialog = ProgressDialog(this)
        decodePayBolt11ProgressDialog!!.setMessage("Loading")
        payOtherProgressDialog = ProgressDialog(this)
        payOtherProgressDialog!!.setMessage("Loading")
        allRountingNodeList
        fundingNodeInfor
        merchantData = GlobalState.instance!!.mainMerchantData
        clientData = GlobalState.instance!!.mainClientData
        if (merchantData != null) {
            val t = merchantData!!.remaining_daily_boost!!.toDouble()
            merchantMAxBoostval?.setText("$" + String.format("%.2f", round(t, 2)))
        } else {
            merchantMAxBoostval?.setText("$0")
        }
        if (clientData != null) {
            val tt = clientData!!.remaining_daily_boost!!.toDouble()
            clientMaxBoostval?.setText("$" + String.format("%.2f", round(tt, 2)))
            if (clientData!!.client_image_id != null) {
                if (!clientData!!.client_image_id!!.isEmpty()) {
                    val mImageUrlString =
                        AppConstants.CLIENT_IMAGE_BASE_URL + clientData!!.client_image_id
                    // Load the image into image view
                    Glide.with(this).load(mImageUrlString).into(clientImage!!)
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
        val proceed = findViewById<Button>(R.id.btn_proceed_for_node)
        val et_clientnodeid = findViewById<EditText>(R.id.et_clientnodeid)
        proceed.setOnClickListener {
            val et_clientnodeid = findViewById<EditText>(R.id.et_clientnodeid)
            val et_clientnodeid_val = et_clientnodeid.text.toString()
            if (!et_clientnodeid_val.isEmpty()) {
                clientNodeID222 = et_clientnodeid_val
                routingNodeExecute(et_clientnodeid_val)
            } else {
                Toast.makeText(application, "Please enter Node ID", Toast.LENGTH_SHORT).show()
            }
        }
        if (intent.getStringExtra("node_id") != null && intent.getStringExtra("node_id")!!.isNotEmpty()
        ) {
            receivingNodeId = intent.getStringExtra("node_id")
            et_clientnodeid.setText(receivingNodeId)
            proceed.performClick()
        }

        findViewById<View>(R.id.btn_logout).visibility = View.VISIBLE
        findViewById<View>(R.id.btn_logout).setOnClickListener {
            sp!!.clearAll()
            openActivity(MerchantLink::class.java)
            finish()
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
        findViewById<View>(R.id.Line1).setOnClickListener {
            if (isGetNodeLine1MaxBoost) {
                showFirstDialog(1)
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
        findViewById<View>(R.id.line2).setOnClickListener {
            if (isGetNodeLine2MaxBoost) {
                showFirstDialog(2)
            }
        }
        findViewById<View>(R.id.line3).setOnClickListener {
            if (isGetNodeLine3MaxBoost) {
                showFirstDialog(3)
            }
        }
        findViewById<View>(R.id.line4).setOnClickListener {
            if (isGetNodeLine4MaxBoost) {
                showFirstDialog(4)
            }
        }
        findViewById<View>(R.id.btn_scan_for_node).setOnClickListener {
            scannerIntent()
            //String bolt11="lnbc66660p1psmehhepp5tphydr3ngwpzkhgdjrfw28pduc0exmypw9r3t8am5kh8wpq3wycqdqvdfkkgen0v34sxqyjw5qcqpjsp5m2fvdd0st23sp749nysze2a32mrt5m4wxkwx4zpwtlyqw4crcyeq9qyyssqcp2kvswqn6auxnuqztzg6e866v2pr57y05dqzkhtutfffun8gxg5u7m275ssfaa42ct3q0y67xqfhmtv5wanpdpr5jkzhurx74dcpsgp0566m9";
            //decodeBolt1122(bolt11);
        }

        bitCoinValue
        val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                //call function
                bitCoinValue
                ha.postDelayed(this, AppConstants.getLatestRateDelayTime)
            }
        }, AppConstants.getLatestRateDelayTime)
        createWebSocketClient()
    }

    private var webSocketClient: WebSocketClient? = null
    private fun createWebSocketClient() {
        val uri: URI = try {
            // Connect to local host
            URI("wss://ws.bitstamp.net/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket", "Session is starting")
                webSocketClient!!.send("Hello World!")
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket", "Message received")
                runOnUiThread {
                    try {
                        Log.i("WebSocket", s)
                        //TextView textView = findViewById(R.id.animalSound);
                        //textView.setText(message);
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onBinaryReceived(data: ByteArray) {}
            override fun onPingReceived(data: ByteArray) {}
            override fun onPongReceived(data: ByteArray) {}
            override fun onException(e: Exception) {
                println(e.message)
            }

            override fun onCloseReceived() {
                Log.i("WebSocket", "Closed ")
                println("onCloseReceived")
            }
        }
        webSocketClient?.setConnectTimeout(10000)
        webSocketClient?.setReadTimeout(60000)
        webSocketClient?.enableAutomaticReconnection(5000)
        webSocketClient?.connect()
    }

    private fun routingNodeExecute(clientNodeId2: String) {
        val temp = ArrayList<NodeLineInfo>()
        GlobalState.instance!!.nodeLineInfoArrayList = temp
        clientNodeId = clientNodeId2
        count = 0
        routingNodeArrayList = ArrayList()
        routingNodeArrayList = GlobalState.instance!!.nodeArrayList
        if (routingNodeArrayList != null) {
            if (routingNodeArrayList!!.size > 0) {
                val soverignLink =
                    routingNodeArrayList!![0].ip + ":" + routingNodeArrayList!![0].port
                val user = routingNodeArrayList!![0].username
                val pass = routingNodeArrayList!![0].password
                //executeRoutingNodeCals(soverignLink,user,pass,clientNodeId);
                // getRoutingNodesData("onoff","abc123","123","listpeers "+clientNodeId+" ");
                getNodeData2(merchantId, soverignLink, pass.orEmpty(), "listpeers $clientNodeId ")
                Log.e("Testphase", ":End")
            }
        }
    }

    //DevelopByNaeem//
    private fun getNodeData2(
        merchantId: String?,
        url: String,
        pass: String,
        clientNodeId2: String
    ) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
//        val gson = GsonBuilder().setLenient().create()
//        val httpLoggingInterceptor = HttpLoggingInterceptor()
//        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
//        val httpClient: OkHttpClient = OkHttpClient.Builder()
//            .addInterceptor(
//                ChuckerInterceptor.Builder(this)
//                    .build()
//            )
//            .addNetworkInterceptor(httpLoggingInterceptor)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://$url/")
//            .client(httpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//        val apiInterface = retrofit.create(
//            ApiInterfaceForNodes::class.java
//        )
        try {
//            val requestBody: MutableMap<String, String?> = HashMap()
//            requestBody["merchantId"] = merchantId
//            requestBody["merchantBackendPassword"] = "abc123"
//            requestBody["boost2faPassword"] = "123456"
//            requestBody["command"] = clientNodeId2

            //quoc testing
            val merchantPwd = sp!!.getStringValue("merchant_password")
            getARoutingAPIAuth1(merchantId, merchantPwd!!, url, clientNodeId2)
            if (authLevel2 == null) {
                getARoutingAPIAuth2(merchantId, "123456", url, clientNodeId2)
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
        } catch (e: Exception) {
            e.printStackTrace()
            progressDialog!!.dismiss()
        }
    }

    private fun getRoutingNodesData(
        merchantId: String,
        mBPass: String,
        pass: String,
        clientNodeId2: String
    ) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        val call = ApiClientForNode.getRetrofit(this)!!.create(
            ApiInterfaceForNodes::class.java
        ).getNodesData(merchantId, mBPass, pass, clientNodeId2)
        call!!.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>, response: Response<ResponseBody?>) {
                if (response.body() != null) {
                    val result = response.body().toString()
                    val resaray = result.split(",".toRegex()).toTypedArray()
                    if (result.contains("peers")) {
                        if (resaray[0].contains("resp") && resaray[1].contains("status") && resaray[2].contains(
                                "rpc-cmd"
                            ) && resaray[3].contains("cli-node")
                        ) {
                            var jsonresponse = ""
                            for (i in 4 until resaray.size) {
                                jsonresponse += "," + resaray[i]
                            }
                            jsonresponse = jsonresponse.substring(1)
                            parseJSONForNodeLineInfor(jsonresponse)
                        } else {
                            parseJSONForNodeLineInfor("[ {   \"peers\": []} ]")
                        }
                    } else {
                        parseJSONForNodeLineInfor("[ {   \"peers\": []} ]")
                    }
                }
                progressDialog!!.dismiss()
                //getAllMerchantList();
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                progressDialog!!.dismiss()
                st!!.toast("Network Error")
                //getAllRountingNodeList();
                //getAllMerchantList();
            }
        })
    }

    private fun executeRoutingNodeCals(
        soverignLink: String,
        user: String,
        pass: String,
        clientNodeId2: String
    ) {
        val getInfoOFLineNode = GetInfoOFLineNode(this@MerchantBoostTerminal)
        getInfoOFLineNode.executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR,
            *arrayOf<String>(
                (soverignLink),
                (user),
                (pass),
                (clientNodeId2)
            )
        )
    }

    private fun showFirstDialog(position: Int) {
        if (clientData != null && merchantData != null) {
            if (clientData!!.client_maxboost != null && merchantData!!.merchant_maxboost != null) {
                val clientMax = clientData!!.client_maxboost!!.toDouble()
                val merchantMax = merchantData!!.merchant_maxboost!!.toDouble()

                if (clientMax > 0 ) {

                } else {
                    val reason =
                        "Client Recharge Exceeded"
                    AlertDialog.Builder(this@MerchantBoostTerminal)
                        .setMessage(reason)
                        .setPositiveButton("Try Again Tommorrow", null)
                        .show()
                    return
                }

                if (merchantMax > 0 ) {

                } else {
                    val reason =
                        "Merchant Recharge Exceeded"
                    AlertDialog.Builder(this@MerchantBoostTerminal)
                        .setMessage(reason)
                        .setPositiveButton("Try Again Tommorrow", null)
                        .show()
                    return
                }


                when (position) {
                    1 -> if ( LINE1_MAX_BOOST > 0) {
                        showFirstPopUp(position)
                    } else {
                        val reason =
                            "Line Recharge Exceeded"
                        AlertDialog.Builder(this@MerchantBoostTerminal)
                            .setMessage(reason)
                            .setPositiveButton("Try Again Tommorrow", null)
                            .show()
                        return
                    }

                    2 -> if (LINE2_MAX_BOOST > 0) {
                            showFirstPopUp(position)
                        } else {
                            val reason =
                                "Line Recharge Exceeded"
                            AlertDialog.Builder(this@MerchantBoostTerminal)
                                .setMessage(reason)
                                .setPositiveButton("Try Again Tommorrow", null)
                                .show()
                            return
                        }

                    3 -> if (LINE3_MAX_BOOST > 0) {
                            showFirstPopUp(position)
                        } else {
                            val reason =
                                "Line Recharge Exceeded"
                            AlertDialog.Builder(this@MerchantBoostTerminal)
                                .setMessage(reason)
                                .setPositiveButton("Try Again Tommorrow", null)
                                .show()
                            return
                        }

                    4 -> if (LINE4_MAX_BOOST > 0) {
                            showFirstPopUp(position)
                        } else {
                            val reason =
                                "Line Recharge Exceeded"
                            AlertDialog.Builder(this@MerchantBoostTerminal)
                                .setMessage(reason)
                                .setPositiveButton("Try Again Tommorrow", null)
                                .show()
                            return
                        }

                }

            }
        }


    }

    private fun showFirstPopUp(lineNo: Int) {
        var temBosstLimitOfChanel = 0.0
        temBosstLimitOfChanel = when (lineNo) {
            1 -> LINE1_MAX_BOOST
            2 -> LINE2_MAX_BOOST
            3 -> LINE3_MAX_BOOST
            4 -> LINE4_MAX_BOOST
            else -> 0.0
        }
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        val pop1Dialog: Dialog
        pop1Dialog = Dialog(this)
        pop1Dialog.setContentView(R.layout.firstpopuplayout2)
        Objects.requireNonNull(pop1Dialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //op1Dialog.getWindow().setLayout((int) (width/1.2), (int) (height/1.5));
        pop1Dialog.setCancelable(false)
        val et_amount = pop1Dialog.findViewById<EditText>(R.id.et_amount_of_boost)
        val ivBack = pop1Dialog.findViewById<ImageView>(R.id.iv_back)
        val proceed = pop1Dialog.findViewById<Button>(R.id.btn_next)
        ivBack.setOnClickListener { pop1Dialog.dismiss() }
        val finalTemBosstLimitOfChanel = temBosstLimitOfChanel
        proceed.setOnClickListener(View.OnClickListener {
            val amountBoostVal = et_amount.text.toString()
            if (amountBoostVal.isEmpty()) {
                showToast("Enter Boost Amount")
            } else {
                var clientMaxBoost = 0.0
                var merchantMaxBoost = 0.0
                var enteredAmount = 0.0
                clientMaxBoost = clientData!!.client_maxboost!!.toDouble()
                merchantMaxBoost = merchantData!!.merchant_maxboost!!.toDouble()
                enteredAmount = amountBoostVal.toDouble()
                var reason = ""
                if (enteredAmount <= clientMaxBoost && enteredAmount <= merchantMaxBoost && enteredAmount <= finalTemBosstLimitOfChanel) {
                    show2ndPopUp(amountBoostVal)
                    pop1Dialog.dismiss()
                } else {
                    reason = ""
                    if (enteredAmount > clientMaxBoost && enteredAmount > merchantMaxBoost && enteredAmount > finalTemBosstLimitOfChanel) {
                        reason =
                            "Merchant Max Boost Exceeded\nClient Recharge Exceeded\nLine Recharge Exceeded"
                    } else {
                        if (enteredAmount > clientMaxBoost) {
                            reason = """
                                ${reason}Client Recharge Exceeded

                                """.trimIndent()
                        }
                        if (enteredAmount > merchantMaxBoost) {
                            reason = """
                                ${reason}Merchant Max Boost Exceeded

                                """.trimIndent()
                        }
                        if (enteredAmount > finalTemBosstLimitOfChanel) {
                            reason = """
                                ${reason}Line Recharge Exceeded

                                """.trimIndent()
                        }
                    }
                    et_amount.setText("")
                    AlertDialog.Builder(this@MerchantBoostTerminal)
                        .setMessage(reason)
                        .setPositiveButton("Try Again Tommorrow", null)
                        .show()
                    return@OnClickListener
                }
            }
        })
        pop1Dialog.show()
    }

    private fun show2ndPopUp(amountBoostVal: String) {
        lockTimeStamp = (Date().time / 1000).toString() + ""
        val fundingNode = GlobalState.instance!!.fundingNode
        var feeBoostPercntage = 4.49
        if (fundingNode != null) {
            if (fundingNode.lightning_boost_fee != null) {
                feeBoostPercntage = fundingNode.lightning_boost_fee!!.toDouble()
            }
        }
        Log.e("Amount", amountBoostVal)
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        pop2Dialog = Dialog(this)
        pop2Dialog!!.setContentView(R.layout.secondpopup)
        Objects.requireNonNull(pop2Dialog!!.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // pop2Dialog.getWindow().setLayout((int) (width/1.2), (int) (height));
        pop2Dialog!!.setCancelable(false)
        val getinvocie = pop2Dialog!!.findViewById<Button>(R.id.getinvocie)
        getinvocie.visibility = View.INVISIBLE
        val amount_usd = pop2Dialog!!.findViewById<TextView>(R.id.amount_usd)
        val boostFee_usd = pop2Dialog!!.findViewById<TextView>(R.id.fees_usd)
        val totalAmount_usd = pop2Dialog!!.findViewById<TextView>(R.id.total_usd)
        val collectTotalAmount = pop2Dialog!!.findViewById<TextView>(R.id.collect_amount)
        val confirmCheckBox = pop2Dialog!!.findViewById<CheckBox>(R.id.checkBox1)
        confirmCheckBox.setOnClickListener {
            if (confirmCheckBox.isChecked) {
                // true,do the task
                getinvocie.visibility = View.VISIBLE
            } else {
                getinvocie.visibility = View.INVISIBLE
            }
        }
        val amountt = amountBoostVal.toDouble()
        AMOUNT_BTC = round(getBtcFromUsd(amountt), 9)
        AMOUNT_USD = amountt
        var usd_fees_from_prcnt = feeBoostPercntage / 100
        usd_fees_from_prcnt = usd_fees_from_prcnt * AMOUNT_USD
        val total = usd_fees_from_prcnt + amountt
        BOOST_CHARG_BTC = round(getBtcFromUsd(usd_fees_from_prcnt), 9)
        BOOST_CHARG_USD = usd_fees_from_prcnt
        TOTAL_AMOUNT_BTC = round(getBtcFromUsd(total), 9)
        TOTAL_AMOUNT_USD = total
        USD_TO_BTC_RATE = AMOUNT_USD / AMOUNT_BTC
        val temp = String.format("%.2f", round(AMOUNT_USD, 2))
        amount_usd.text = "$" + temp + " / " + excatFigure(round(AMOUNT_BTC, 9)) + " BTC"
        boostFee_usd.text =
            "$" + String.format("%.2f", round(BOOST_CHARG_USD, 2)) + " / " + excatFigure(
                round(BOOST_CHARG_BTC, 9)
            ) + " BTC"
        totalAmount_usd.text =
            "$" + String.format("%.2f", round(TOTAL_AMOUNT_USD, 2)) + " / " + excatFigure(
                round(TOTAL_AMOUNT_BTC, 9)
            ) + " BTC"
        collectTotalAmount.text = "$" + String.format("%.2f", round(TOTAL_AMOUNT_USD, 2))
        val ivBack = pop2Dialog!!.findViewById<ImageView>(R.id.iv_back)
        ivBack.setOnClickListener { pop2Dialog!!.dismiss() }
        getinvocie.setOnClickListener { //quoc testing comment code
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
            dialogBoxForRefundCommandeer("123456")
        }
        pop2Dialog!!.show()
    }

    //Pop3 Scan BOLT11  QR
    private fun dialogBoxForRefundCommandeer(fa2pass: String) {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        commandeerRefundDialog = Dialog(this@MerchantBoostTerminal)
        commandeerRefundDialog!!.setContentView(R.layout.dialoglayoutrefundcommandeer2)
        Objects.requireNonNull(commandeerRefundDialog!!.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        //commandeerRefundDialog.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        commandeerRefundDialog!!.setCancelable(false)
        val bolt11 = commandeerRefundDialog!!.findViewById<EditText>(R.id.bolt11val)
        val ivBack = commandeerRefundDialog!!.findViewById<ImageView>(R.id.iv_back)
        val tv_amountshown = commandeerRefundDialog!!.findViewById<TextView>(R.id.amount)
        tv_amountshown.text = excatFigure(round(AMOUNT_BTC, 9)) + " BTC"
        val btnNext = commandeerRefundDialog!!.findViewById<Button>(R.id.btn_next)
        val btnscanQr = commandeerRefundDialog!!.findViewById<Button>(R.id.btn_scanQR)
        // progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener { commandeerRefundDialog!!.dismiss() }
        btnNext.setOnClickListener(View.OnClickListener {
            val bolt11value = bolt11.text.toString()
            if (bolt11value.isEmpty()) {
                AlertDialog.Builder(this@MerchantBoostTerminal)
                    .setMessage("Bolt 11 Empty")
                    .setPositiveButton("Try Again", null)
                    .show()
                return@OnClickListener
            } else {

                //quoc testing temp comment
//                    commandeerRefundDialog.dismiss();
                //quoc testing temp comment
                bolt11fromqr = bolt11value
                decodeBolt1122(bolt11value, fa2pass)
                //decodeBolt11(bolt11value);
            }
        })
        btnscanQr.setOnClickListener { //qrScan.forSupportFragment(AdminFragment1.this).initiateScan();
            scannerIntent2()
        }
        commandeerRefundDialog!!.show()
    }

    private fun decodeBolt1122(bolt11fromqr1: String, fa2pass: String) {
        decodePayBolt11ProgressDialog!!.show()
        decodePayBolt11ProgressDialog!!.setCancelable(false)
        decodePayBolt11ProgressDialog!!.setCanceledOnTouchOutside(false)
        val node = GlobalState.instance!!.fundingNode
        if (node != null) {
            val ip = GlobalState.instance!!.fundingNode!!.ip
            val port = GlobalState.instance!!.fundingNode!!.port!!.toInt()
            val userNAme = GlobalState.instance!!.fundingNode!!.username
            val pass = GlobalState.instance!!.fundingNode!!.password.orEmpty()
            val url = ip + ":" + GlobalState.instance!!.fundingNode!!.port
            //String bolt11="lnbc66660p1psmehhepp5tphydr3ngwpzkhgdjrfw28pduc0exmypw9r3t8am5kh8wpq3wycqdqvdfkkgen0v34sxqyjw5qcqpjsp5m2fvdd0st23sp749nysze2a32mrt5m4wxkwx4zpwtlyqw4crcyeq9qyyssqcp2kvswqn6auxnuqztzg6e866v2pr57y05dqzkhtutfffun8gxg5u7m275ssfaa42ct3q0y67xqfhmtv5wanpdpr5jkzhurx74dcpsgp0566m9";
            invoiceId = bolt11fromqr1
            bolt11fromqr = bolt11fromqr1
            decodeBolt112(merchantId, url, pass, " decodepay $bolt11fromqr1", fa2pass)

            //quoc testing
            decodeBolt112Execute(merchantId, url, pass, " decodepay $bolt11fromqr1", fa2pass)
            //quoc testing
        } else {
        }
    }

    private fun decodeBolt112Execute(
        merchantId: String?,
        url: String,
        pass: String,
        clientNodeId2: String,
        fa2pass: String
    ) {
        val gson = GsonBuilder().setLenient().create()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(this)
                    .build()
            )
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["cmd"] = clientNodeId2.trim { it <= ' ' }
            val call =
                apiInterface.getDecodeBolt112WithExecute("Bearer $authLevel1", requestBody.toMap())
            call!!.enqueue(object : Callback<DecodeBolt112WithExecuteResponse?> {
                override fun onResponse(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    response: Response<DecodeBolt112WithExecuteResponse?>
                ) {
                    try {
                        if (response.body()!!.success && response.body()!!.decodeBolt112WithExecuteData != null) {
                            val object2 =
                                JSONObject(response.body()!!.decodeBolt112WithExecuteData!!.stdout)
                            if (object2.length() > 0) {
                                if (response.body()!!.decodeBolt112WithExecuteData!!.stdout!!.contains(
                                        "msatoshi"
                                    )
                                ) {
                                    var decodePayBolt11: DecodePayBolt11? = null
                                    var jsonObj: JSONObject? = null
                                    jsonObj = object2
                                    if (jsonObj != null) {
                                        try {
                                            val gson = Gson()
                                            val type =
                                                object : TypeToken<DecodePayBolt11?>() {}.type
                                            decodePayBolt11 =
                                                gson.fromJson(jsonObj.toString(), type)
                                            GlobalState.instance!!.currentDecodePayBolt11 =
                                                decodePayBolt11
                                        } catch (e: Exception) {
                                            decodePayBolt11ProgressDialog!!.dismiss()
                                            Log.e("Error", e.message!!)
                                        }
                                    }
                                    GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                                    if (decodePayBolt11 != null) {
                                        val btc =
                                            mSatoshoToBtc(java.lang.Double.valueOf(decodePayBolt11.msatoshi.toString()))
                                        val priceInBTC =
                                            GlobalState.instance!!.currentAllRate!!.uSD!!.last!!
                                        var usd = priceInBTC * btc
                                        usd = round(usd, 2)

                                        //quoc testing
                                        if (usd != round(AMOUNT_USD, 2)) {
                                            goAlertDialogwithOneBTn(
                                                1,
                                                "Error",
                                                resources.getString(R.string.invoice_made_for_incorrect_value),
                                                "Ok",
                                                ""
                                            )
                                            return
                                        } else {
                                            //quoc testing
                                            decodePayBolt11ProgressDialog!!.dismiss()
                                            dialogBoxForRefundCommandeerStep2(
                                                bolt11fromqr,
                                                decodePayBolt11.msatoshi.toString(),
                                                url,
                                                pass,
                                                fa2pass
                                            )
                                        }
                                    } else {
                                        val decode2PayBolt11 = DecodePayBolt11()
                                        decode2PayBolt11.msatoshi = 0.0
                                        GlobalState.instance!!.currentDecodePayBolt11 =
                                            decode2PayBolt11
                                        decodePayBolt11ProgressDialog!!.dismiss()
                                        dialogBoxForRefundCommandeerStep2(
                                            bolt11fromqr,
                                            decode2PayBolt11.msatoshi.toString(),
                                            url,
                                            pass,
                                            fa2pass
                                        )
                                    }
                                } else {
                                    val decodePayBolt11 = DecodePayBolt11()
                                    decodePayBolt11.msatoshi = 0.0
                                    GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                                    decodePayBolt11ProgressDialog!!.dismiss()
                                    dialogBoxForRefundCommandeerStep2(
                                        bolt11fromqr,
                                        decodePayBolt11.msatoshi.toString(),
                                        url,
                                        pass,
                                        fa2pass
                                    )
                                }
                            } else {
                                decodePayBolt11ProgressDialog!!.dismiss()
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }
                        } else {
                            if (response.body() != null && response.body()!!.message != null) {
                                goAlertDialogwithOneBTn(1, "", response.body()!!.message, "Ok", "")
                            } else {
                                goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "")
                            }
                            decodePayBolt11ProgressDialog!!.dismiss()
                        }
                    } catch (e: Exception) {
                        if (response.body() != null && response.body()!!.message != null) {
                            goAlertDialogwithOneBTn(1, "", response.body()!!.message, "Ok", "")
                        } else if (response != null && response.message() != null) {
                            goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "")
                        } else {
                            goAlertDialogwithOneBTn(1, "", e.message, "Ok", "")
                        }
                        decodePayBolt11ProgressDialog!!.dismiss()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    t: Throwable
                ) {
                    decodePayBolt11ProgressDialog!!.dismiss()
                    Log.e("TAG", "onResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            decodePayBolt11ProgressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    private fun decodeBolt112(
        merchantId: String?,
        url: String,
        pass: String,
        clientNodeId2: String,
        fa2pass: String
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String?> = HashMap()
            requestBody["merchantId"] = merchantId
            requestBody["merchantBackendPassword"] = pass
            requestBody["boost2faPassword"] = fa2pass
            requestBody["command"] = clientNodeId2
            val call = apiInterface.getNodes(requestBody.toMap())
            call!!.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                    try {
                        val `object` = JSONObject(Gson().toJson(response.body()))
                        val keys = `object`.keys()
                        val object1: JSONObject? = null
                        val map = HashMap<String, Any>()
                        if (keys.hasNext()) {
                            val key = keys.next() as String
                            val object22 = `object`[key]
                            map[key] = `object`[key]
                        }
                        var firstName = map["output"] as String?
                        firstName = firstName!!.replace("\\s".toRegex(), "")
                        firstName = firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "")
                        firstName = firstName.substring(2)
                        firstName = firstName.replace("b'".toRegex(), "")
                        firstName = firstName.replace("',".toRegex(), "")
                        firstName = firstName.replace("'".toRegex(), "")
                        firstName = firstName.substring(0, firstName.length - 1)
                        if (!firstName.contains("code")) {
                            val object2 = JSONObject(firstName)
                            if (object2.length() > 0) {
                                if (firstName.contains("msatoshi")) {
                                    var decodePayBolt11: DecodePayBolt11? = null
                                    var jsonObj: JSONObject? = null
                                    jsonObj = object2
                                    if (jsonObj != null) {
                                        try {
                                            val gson = Gson()
                                            val type =
                                                object : TypeToken<DecodePayBolt11?>() {}.type
                                            decodePayBolt11 =
                                                gson.fromJson(jsonObj.toString(), type)
                                            GlobalState.instance!!.currentDecodePayBolt11 =
                                                decodePayBolt11
                                        } catch (e: Exception) {
                                            decodePayBolt11ProgressDialog!!.dismiss()
                                            Log.e("Error", e.message!!)
                                        }
                                    }
                                    GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                                    if (decodePayBolt11 != null) {
                                        decodePayBolt11ProgressDialog!!.dismiss()
                                        dialogBoxForRefundCommandeerStep2(
                                            bolt11fromqr,
                                            decodePayBolt11.msatoshi.toString(),
                                            url,
                                            pass,
                                            fa2pass
                                        )
                                    } else {
                                        val decode2PayBolt11 = DecodePayBolt11()
                                        decode2PayBolt11.msatoshi = 0.0
                                        GlobalState.instance!!.currentDecodePayBolt11 =
                                            decode2PayBolt11
                                        decodePayBolt11ProgressDialog!!.dismiss()
                                        dialogBoxForRefundCommandeerStep2(
                                            bolt11fromqr,
                                            decode2PayBolt11.msatoshi.toString(),
                                            url,
                                            pass,
                                            fa2pass
                                        )
                                    }
                                } else {
                                    val decodePayBolt11 = DecodePayBolt11()
                                    decodePayBolt11.msatoshi = 0.0
                                    GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                                    decodePayBolt11ProgressDialog!!.dismiss()
                                    dialogBoxForRefundCommandeerStep2(
                                        bolt11fromqr,
                                        decodePayBolt11.msatoshi.toString(),
                                        url,
                                        pass,
                                        fa2pass
                                    )
                                }
                            } else {
                                decodePayBolt11ProgressDialog!!.dismiss()
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }
                        } else {
                            firstName = firstName.replace("\\\\".toRegex(), "")
                            val object2 = JSONObject(firstName)
                            val message = object2.getString("message")
                            st!!.toast(message)
                            decodePayBolt11ProgressDialog!!.dismiss()
                        }
                    } catch (e: JSONException) {
                        decodePayBolt11ProgressDialog!!.dismiss()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    decodePayBolt11ProgressDialog!!.dismiss()
                    Log.e("TAG", "onResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            decodePayBolt11ProgressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    private fun decodeBolt11(bolt11fromqr: String) {
        val decodePayBolt11API = DecodePayBolt11API(this@MerchantBoostTerminal)
        decodePayBolt11API.executeOnExecutor(
            AsyncTask.THREAD_POOL_EXECUTOR,
            *arrayOf<String>((bolt11fromqr))
        )
        decodePayBolt11ProgressDialog!!.show()
        decodePayBolt11ProgressDialog!!.setCancelable(false)
        decodePayBolt11ProgressDialog!!.setCanceledOnTouchOutside(false)
    }

    private inner class DecodePayBolt11API     // record the calling activity, to use in showing/hiding dialogs
        (private val parent: Activity) : AsyncTask<String?, Int?, String>() {
        override fun onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected override fun doInBackground(vararg params: String?): String? {
            // called on the background thread
            val count = params.size
            val bolt11 = params[0]
            var response = ""
            var mlattitude: String? = "0.0"
            if (GlobalState.instance!!.lattitude != null) {
                mlattitude = GlobalState.instance!!.lattitude
            }
            var mlongitude: String? = "0.0"
            if (GlobalState.instance!!.longitude != null) {
                mlongitude = GlobalState.instance!!.longitude
            }
            //Bolt 11, msatoshi, label
            val decodePayBolt11Query =
                "rpc-cmd,cli-node," + mlattitude + "_" + mlongitude + "," + unixTimeStamp + ",[ decodepay " + bolt11 + " ]"
            Log.e("DecodePayBolt11Query:", decodePayBolt11Query)
            val node = GlobalState.instance!!.fundingNode
            if (node != null) {
                val ip = GlobalState.instance!!.fundingNode!!.ip
                val port = GlobalState.instance!!.fundingNode!!.port!!.toInt()
                val userNAme = GlobalState.instance!!.fundingNode!!.username
                val pass = GlobalState.instance!!.fundingNode!!.password.orEmpty()
                val status = Boolean.valueOf(NetworkManager.instance.connectClient(ip, port))
                if (status) {
                    val role = NetworkManager.instance.validateUser(userNAme!!, pass!!)
                    if (role == ADMINROLE) {
                        try {
                            NetworkManager.instance.sendToServer(decodePayBolt11Query)
                        } catch (e: Exception) {
                            Log.e(TAG, e.localizedMessage)
                        }
                        try {
                            // Try now
                            response = NetworkManager.instance.recvFromServer()
                        } catch (e: Exception) {
                            Log.e(TAG, e.localizedMessage)
                        }
                    } else {
                        response = "fail"
                    }
                } else {
                    response = "fail"
                }
            } else {
                response = "fail"
            }
            Log.e(TAG, "DecodePayBolt11QueryResponse:$response")
            return response
        }

        protected override fun onProgressUpdate(vararg values: Int?) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }


        override fun onPostExecute(result: String) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);
            // isInApp=true;

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
               */if (result.contains("msatoshi")) {
                val split = result.split(",".toRegex()).toTypedArray()
                var invoiceReponse = ""
                for (i in 4 until split.size) {
                    invoiceReponse += "," + split[i]
                }
                invoiceReponse = invoiceReponse.substring(1)
                val decodePayBolt11 = parseJSONForDecodePayBolt11(invoiceReponse)
                GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                if (decodePayBolt11 != null) {
                    decodePayBolt11ProgressDialog!!.dismiss()
                    dialogBoxForRefundCommandeerStep2(
                        bolt11fromqr,
                        decodePayBolt11.msatoshi.toString(),
                        "",
                        "",
                        ""
                    )
                } else {
                    val decode2PayBolt11 = DecodePayBolt11()
                    decode2PayBolt11.msatoshi = 0.0
                    GlobalState.instance!!.currentDecodePayBolt11 = decode2PayBolt11
                    decodePayBolt11ProgressDialog!!.dismiss()
                    dialogBoxForRefundCommandeerStep2(
                        bolt11fromqr,
                        decode2PayBolt11.msatoshi.toString(),
                        "",
                        "",
                        ""
                    )
                }
            } else {
                val decodePayBolt11 = DecodePayBolt11()
                decodePayBolt11.msatoshi = 0.0
                GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
                decodePayBolt11ProgressDialog!!.dismiss()
                dialogBoxForRefundCommandeerStep2(
                    bolt11fromqr,
                    decodePayBolt11.msatoshi.toString(),
                    "",
                    "",
                    ""
                )
            }
            Log.e(TAG, "DecodePayBolt11QueryResponse:$result")
        }


    }

    private fun CommandRefundApi(
        bolt11value: String,
        labelval: String,
        amountusd: String,
        url: String,
        pass: String,
        fa2pass: String
    ) {
        payOtherProgressDialog!!.show()
        payOtherProgressDialog!!.setCancelable(false)
        payOtherProgressDialog!!.setCanceledOnTouchOutside(false)
        var priceInBTC = 1 / GlobalState.instance!!.currentAllRate!!.uSD!!.last!!
        priceInBTC = priceInBTC * amountusd.toDouble()
        var amountInMsatoshi = priceInBTC * AppConstants.btcToSathosi
        amountInMsatoshi = amountInMsatoshi * AppConstants.satoshiToMSathosi
        //msatoshi=excatFigure(amountInMsatoshi);
        val formatter: NumberFormat = DecimalFormat("#0")
        val rMSatoshi = formatter.format(amountInMsatoshi)
        //showToast(bolt11value+"-"+labelval+"-"+rMSatoshi);
        Log.e("abcd", "$bolt11value-$labelval-$rMSatoshi")
        TRANSACTION_LABEL = labelval
        val commad = "pay $bolt11value null $labelval"

//        CommandRefundApiRequest(merchantId,url,pass,commad,fa2pass);

        //quoc testing
        CommandRefundApiRequestWithExecute(merchantId, url, pass, commad, fa2pass)
        //quoc testing
    }

    private fun CommandRefundApiRequestWithExecute(
        merchantId: String?,
        url: String,
        pass: String,
        commad: String,
        fa2pass: String
    ) {
        val gson = GsonBuilder().setLenient().create()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(this)
                    .build()
            )
            .addNetworkInterceptor(httpLoggingInterceptor)
            .readTimeout(180, TimeUnit.SECONDS)
            .connectTimeout(180, TimeUnit.SECONDS)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String?> = HashMap()
            //            requestBody.put("cmd", commad);
//            Call<DecodeBolt112WithExecuteResponse> call=apiInterface.callRefundApiRequestWithExecute("Bearer " + authLevel2,requestBody);
            requestBody["invoice"] = invoiceId
            requestBody["client_id"] = clientData!!.client_id
            requestBody["invoiceLabel"] = TRANSACTION_LABEL
            requestBody["lock_timestamp"] = lockTimeStamp
            requestBody["lock_msatoshi"] = (AMOUNT_BTC * 100000 * 1000000).toLong().toString() + ""
            requestBody["lock_usd"] = AMOUNT_USD.toString() + ""
            val call =
                apiInterface.callPayApiRequestWithExecute("Bearer $authLevel2", requestBody.toMap())
            call!!.enqueue(object : Callback<DecodeBolt112WithExecuteResponse?> {
                override fun onResponse(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    response: Response<DecodeBolt112WithExecuteResponse?>
                ) {
                    try {
                        if (response.body()!!.success) {
                            val object2 =
                                JSONObject(response.body()!!.decodeBolt112WithExecuteData!!.stdout)
                            if (object2.length() > 0) {
                                if (response.body()!!.decodeBolt112WithExecuteData!!.stdout!!.contains(
                                        "payment_hash"
                                    )
                                ) {
                                    var pay: Pay? = null
                                    var jsonObj: JSONObject? = null
                                    jsonObj = object2
                                    if (jsonObj != null) {
                                        try {
                                            val gson = Gson()
                                            val type = object : TypeToken<Pay?>() {}.type
                                            pay = gson.fromJson(jsonObj.toString(), type)
                                            // GlobalState.getInstance().setInvoice(pay);
                                        } catch (e: Exception) {
                                            payOtherProgressDialog!!.dismiss()
                                            Log.e("Error", e.message!!)
                                        }
                                    }
                                    if (pay != null) {
                                        if (pay.status == "complete") {
                                            //showToast("Succefully Pay");
                                            val body =
                                                "Amount:" + pay.amount_msat + " Recived Succefully"
                                            //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                                            //quoc testing comment for new requirement
//                                            addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                                            //quoc testing comment for new requirement

                                            //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                                            //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                                            showCofirmationDialog(pay)
                                        } else {
                                            val payresponse1 = Pay()
                                            payresponse1.status = "Not complete"
                                            showCofirmationDialog(payresponse1)
                                        }
                                    } else {
                                        val payresponse2 = Pay()
                                        payresponse2.status = "Not complete"
                                        showCofirmationDialog(payresponse2)
                                    }
                                    payOtherProgressDialog!!.dismiss()
                                } else {
                                    val payresponse3 = Pay()
                                    payresponse3.status = "Not complete"
                                    showCofirmationDialog(payresponse3)
                                    payOtherProgressDialog!!.dismiss()
                                }
                            } else {
                                payOtherProgressDialog!!.dismiss()
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }
                        } else {
                            Toast.makeText(
                                this@MerchantBoostTerminal,
                                "" + response.body()!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        payOtherProgressDialog!!.dismiss()
                        if (response.body() != null && response.body()!!.message != null) {
                            goAlertDialogwithOneBTn(1, "", response.body()!!.message, "Ok", "")
                        } else if (response != null && response.message() != null) {
                            goAlertDialogwithOneBTn(1, "", response.message(), "Ok", "")
                        } else {
                            goAlertDialogwithOneBTn(1, "", e.message, "Ok", "")
                        }
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    t: Throwable
                ) {
                    payOtherProgressDialog!!.dismiss()
                    Log.e("TAG", "onResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            payOtherProgressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    private fun CommandRefundApiRequest(
        merchantId: String,
        url: String,
        pass: String,
        commad: String,
        fa2pass: String
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["merchantId"] = merchantId
            requestBody["merchantBackendPassword"] = pass
            requestBody["boost2faPassword"] = fa2pass
            requestBody["command"] = commad
            val call = apiInterface.getNodes(requestBody.toMap())
            call!!.enqueue(object : Callback<Any?> {
                override fun onResponse(call: Call<Any?>, response: Response<Any?>) {
                    try {
                        val `object` = JSONObject(Gson().toJson(response.body()))
                        val keys = `object`.keys()
                        val object1: JSONObject? = null
                        val map = HashMap<String, Any>()
                        if (keys.hasNext()) {
                            val key = keys.next() as String
                            val object22 = `object`[key]
                            map[key] = `object`[key]
                        }
                        var firstName = map["output"] as String?
                        firstName = firstName!!.replace("\\s".toRegex(), "")
                        firstName = firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "")
                        firstName = firstName.substring(2)
                        firstName = firstName.replace("b'".toRegex(), "")
                        firstName = firstName.replace("',".toRegex(), "")
                        firstName = firstName.replace("'".toRegex(), "")
                        firstName = firstName.substring(0, firstName.length - 1)
                        if (!firstName.contains("code")) {
                            val object2 = JSONObject(firstName)
                            if (object2.length() > 0) {
                                if (firstName.contains("payment_hash")) {
                                    var pay: Pay? = null
                                    var jsonObj: JSONObject? = null
                                    jsonObj = object2
                                    if (jsonObj != null) {
                                        try {
                                            val gson = Gson()
                                            val type = object : TypeToken<Pay?>() {}.type
                                            pay = gson.fromJson(jsonObj.toString(), type)
                                            // GlobalState.getInstance().setInvoice(pay);
                                        } catch (e: Exception) {
                                            payOtherProgressDialog!!.dismiss()
                                            Log.e("Error", e.message!!)
                                        }
                                    }
                                    if (pay != null) {
                                        if (pay.status == "complete") {
                                            //showToast("Succefully Pay");
                                            val body =
                                                "Amount:" + pay.amount_msat + " Recived Succefully"
                                            //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                                            //quoc testing comment for new requirement
//                                            addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                                            //quoc testing comment for new requirement

                                            //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                                            //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                                            showCofirmationDialog(pay)
                                        } else {
                                            val payresponse1 = Pay()
                                            payresponse1.status = "Not complete"
                                            showCofirmationDialog(payresponse1)
                                        }
                                    } else {
                                        val payresponse2 = Pay()
                                        payresponse2.status = "Not complete"
                                        showCofirmationDialog(payresponse2)
                                    }
                                    payOtherProgressDialog!!.dismiss()
                                } else {
                                    val payresponse3 = Pay()
                                    payresponse3.status = "Not complete"
                                    showCofirmationDialog(payresponse3)
                                    payOtherProgressDialog!!.dismiss()
                                }
                            } else {
                                payOtherProgressDialog!!.dismiss()
                                //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            }
                        } else {
                            firstName = firstName.replace("\\\\".toRegex(), "")
                            val object2 = JSONObject(firstName)
                            val message = object2.getString("message")
                            st!!.toast(message)
                            payOtherProgressDialog!!.dismiss()
                        }
                    } catch (e: JSONException) {
                        payOtherProgressDialog!!.dismiss()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    payOtherProgressDialog!!.dismiss()
                    Log.e("TAG", "onResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            payOtherProgressDialog!!.dismiss()
            e.printStackTrace()
        }
    }

    private fun executeCommandeerRefundApi(
        bolt11value: String,
        labelval: String,
        amountusd: String
    ) {
        var priceInBTC = 1 / GlobalState.instance!!.currentAllRate!!.uSD!!.last!!
        priceInBTC = priceInBTC * amountusd.toDouble()
        var amountInMsatoshi = priceInBTC * AppConstants.btcToSathosi
        amountInMsatoshi = amountInMsatoshi * AppConstants.satoshiToMSathosi
        //msatoshi=excatFigure(amountInMsatoshi);
        val formatter: NumberFormat = DecimalFormat("#0")
        val rMSatoshi = formatter.format(amountInMsatoshi)
        //showToast(bolt11value+"-"+labelval+"-"+rMSatoshi);
        Log.e("abcd", "$bolt11value-$labelval-$rMSatoshi")
        TRANSACTION_LABEL = labelval
        val payRequestToOtherFromServer = PayRequestToOtherFromServer(this@MerchantBoostTerminal)
        if (Build.VERSION.SDK_INT >= 11 /*HONEYCOMB*/) {
            payRequestToOtherFromServer.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                *arrayOf<String>((bolt11value), (rMSatoshi), (labelval))
            )
            payOtherProgressDialog!!.show()
            payOtherProgressDialog!!.setCancelable(false)
            payOtherProgressDialog!!.setCanceledOnTouchOutside(false)
        } else {
            payRequestToOtherFromServer.execute(
                *arrayOf<String>(
                    (bolt11value),
                    (rMSatoshi),
                    (labelval)
                )
            )
            payOtherProgressDialog!!.show()
            payOtherProgressDialog!!.setCancelable(false)
            payOtherProgressDialog!!.setCanceledOnTouchOutside(false)
        }
    }

    private inner class PayRequestToOtherFromServer     // record the calling activity, to use in showing/hiding dialogs
        (private val parent: Activity) : AsyncTask<String?, Int?, String>() {
        override fun onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected override fun doInBackground(vararg urls: String?): String {
            // called on the background thread
            val count = urls.size
            val bolt11 = urls[0]
            val masatoshi = urls[1]
            val label = urls[2]
            var response = ""
            var mlattitude: String? = "0.0"
            if (GlobalState.instance!!.lattitude != null) {
                mlattitude = GlobalState.instance!!.lattitude
            }
            var mlongitude: String? = "0.0"
            if (GlobalState.instance!!.longitude != null) {
                mlongitude = GlobalState.instance!!.longitude
            }
            //Bolt 11, msatoshi, label
            val payToOtherQuery =
                "rpc-cmd,cli-node," + mlattitude + "_" + mlongitude + "," + unixTimeStamp + ",[ pay " + bolt11 + " " + "null" + " " + label + " ]"
            Log.e("PayQuery:", payToOtherQuery)
            try {
                NetworkManager.instance.sendToServer(payToOtherQuery)
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
            }
            try {
                // Try now
                response = NetworkManager.instance.recvFromServer()
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
            }
            //  int role= NetworkManager.getInstance().validateUser(tempdflUserId, tempdflPsswd);
            return response
        }

        protected override fun onProgressUpdate(vararg values: Int?) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }

        override fun onPostExecute(result: String) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);
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
   ]*/Log.e(TAG, "PayOthers:$result")
            if (result.contains("payment_hash")) {
                val split = result.split(",".toRegex()).toTypedArray()
                var invoiceReponse = ""
                for (i in 4 until split.size) {
                    invoiceReponse += "," + split[i]
                }
                invoiceReponse = invoiceReponse.substring(1)
                val payresponse = parseJSONForPayOthers(invoiceReponse)
                if (payresponse != null) {
                    if (payresponse.status == "complete") {
                        //showToast("Succefully Pay");
                        val body = "Amount:" + payresponse.amount_msat + " Recived Succefully"
                        //sendEmailFromClass("Payment Recived from:"+clientData.getClient_id(),payresponse.getAmount_msat());

                        //quoc testing comment for new requirement
//                      addInTransactionLog(AMOUNT_USD, AMOUNT_BTC, pay);
                        //quoc testing comment for new requirement

                        //updateClientMaxBoost(AMOUNT_USD,Double.parseDouble(clientData.getClient_maxboost()));
                        //updateMerchantMaxBoost(AMOUNT_USD,Double.parseDouble(merchantData.getMerchant_maxboost()));
                        showCofirmationDialog(payresponse)
                    } else {
                        val payresponse1 = Pay()
                        //                        payresponse1.setCreated_at(239823892);
//                        payresponse1.setMsatoshi(12321212);
//                        payresponse1.setDestination("abasasasasasasasasasasasasasas");
//                        payresponse1.setPayment_preimage("asasasasasasasasasasasasasasas");
//                        payresponse1.setPayment_hash("asasasasasasasasasasasasasasasas");
//                        payresponse1.setStatus("complete");
                        payresponse1.status = "Not complete"
                        showCofirmationDialog(payresponse1)
                    }
                } else {
                    val payresponse2 = Pay()
                    //                    payresponse2.setCreated_at(239823892);
//                    payresponse2.setMsatoshi(12321212);
//                    payresponse2.setDestination("abasasasasasasasasasasasasasas");
//                    payresponse2.setPayment_preimage("asasasasasasasasasasasasasasas");
//                    payresponse2.setPayment_hash("asasasasasasasasasasasasasasasas");
//                    payresponse2.setStatus("complete");
                    payresponse2.status = "Not complete"
                    showCofirmationDialog(payresponse2)
                }
                payOtherProgressDialog!!.dismiss()
            } else {
                val payresponse3 = Pay()
                //                payresponse3.setCreated_at(239823892);
//                payresponse3.setMsatoshi(12321212);
//                payresponse3.setDestination("abasasasasasasasasasasasasasas");
//                payresponse3.setPayment_preimage("asasasasasasasasasasasasasasas");
//                payresponse3.setPayment_hash("asasasasasasasasasasasasasasasas");
//                payresponse3.setStatus("complete");
                payresponse3.status = "Not complete"
                showCofirmationDialog(payresponse3)
                payOtherProgressDialog!!.dismiss()
            }
        }


    }

    private fun updateMerchantMaxBoost(amount_usd: Double, merchantAviableBoost: Double) {
        var updatedMaxBoostMerchant = 0.0
        updatedMaxBoostMerchant = merchantAviableBoost - amount_usd
        //TODO:ABi merchcant id ko update krna he
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).merchant_Update(merchantData!!.id, updatedMaxBoostMerchant.toString())
        call!!.enqueue(object : Callback<MerchantUpdateResp?> {
            override fun onResponse(
                call: Call<MerchantUpdateResp?>,
                response: Response<MerchantUpdateResp?>
            ) {
                if (response != null) {
                    if (response.body() != null) {
                        val merchantUpdateResp = response.body()
                        if (merchantUpdateResp!!.merchantData != null) {
                            merchantData = merchantUpdateResp.merchantData
                            val t = merchantData!!.merchant_maxboost!!.toDouble()
                            merchantMAxBoostval!!.text = "$" + String.format("%.2f", round(t, 2))
                        }
                    }
                }
                Log.e("UpdateMerchantBoostLog", response.message())
            }

            override fun onFailure(call: Call<MerchantUpdateResp?>, t: Throwable) {
                Log.e("UpdateMerchantBoostLog", t.message.toString())
            }
        })
    }

    private fun updateClientMaxBoost(amount_usd: Double, clientAviableBoost: Double) {
        var updatedMaxBoostClient = 0.0
        updatedMaxBoostClient = clientAviableBoost - amount_usd
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).client_Update(clientData!!.id, updatedMaxBoostClient.toString())
        call!!.enqueue(object : Callback<ClientUpdateResp?> {
            override fun onResponse(
                call: Call<ClientUpdateResp?>,
                response: Response<ClientUpdateResp?>
            ) {
                if (response != null) {
                    if (response.body() != null) {
                        val clientUpdateResp = response.body()
                        if (clientUpdateResp!!.clientData != null) {
                            clientData = clientUpdateResp.clientData
                            val tt = clientData?.client_maxboost?.toDouble()
                            clientMaxBoostval!!.text = "$" + String.format("%.2f", round(tt!!, 2))
                        }
                        Log.e("Test", "Test")
                    }
                }
                Log.e("UpdateClientBoostLog", response.message())
            }

            override fun onFailure(call: Call<ClientUpdateResp?>, t: Throwable) {
                Log.e("UpdateClientBoostLog", t.message.toString())
            }
        })
    }

    private fun addInTransactionLog(amount_usd: Double, amount_btc: Double, payresponse: Pay) {
        val amountUsd = amount_usd.toString()
        val amountBtc = amount_btc.toString()
        val label = TRANSACTION_LABEL
        val clientId = clientData!!.client_id
        val merchantID = merchantData!!.merchant_name

//        String transactionTimeStamp=String.valueOf(payresponse.getCreated_at());
        //quoc testing
        val transactionTimeStamp = currentDate
        //quoc testing
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).transction_add(
            label,
            label,
            amountBtc,
            amountUsd,
            clientId,
            merchantID,
            transactionTimeStamp,
            USD_TO_BTC_RATE.toString()
        )
        call!!.enqueue(object : Callback<TransactionResp?> {
            override fun onResponse(
                call: Call<TransactionResp?>,
                response: Response<TransactionResp?>
            ) {
                if (response != null) {
                    if (response.body() != null) {
                        val transactionResp = response.body()
                        if (transactionResp!!.message == "successfully done" && transactionResp.transactionInfo != null) {
                            var transactionInfo = TransactionInfo()
                            transactionInfo = transactionResp.transactionInfo!!
                            merchantData!!.merchant_maxboost = transactionInfo.merchant_remaining
                            val t = merchantData!!.merchant_maxboost!!.toDouble()
                            merchantMAxBoostval!!.text = "$" + String.format("%.2f", round(t, 2))
                            clientData!!.client_maxboost = transactionInfo.client_remaining
                            val tt = clientData!!.client_maxboost!!.toDouble()
                            clientMaxBoostval!!.text = "$" + String.format("%.2f", round(tt, 2))
                            GlobalState.instance!!.mainClientData = clientData
                            GlobalState.instance!!.mainMerchantData = merchantData
                        } else {
                            showToast("Not Done!!")
                        }
                        Log.e("Test", "Test")
                    } else {
                        showToast(response.message())
                        Log.e("AddTransactionLog", response.message())
                    }
                }
                Log.e("AddTransactionLog", response.message())
            }

            override fun onFailure(call: Call<TransactionResp?>, t: Throwable) {
                Log.e("AddTransactionLog", t.message.toString())
            }
        })
    }

    private fun showCofirmationDialog(payresponse: Pay) {
        routingNodeExecute(clientNodeID222)
        updateMerchantClientMaxBoost()
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        commandeerRefundDialogstep2 = Dialog(this@MerchantBoostTerminal)
        commandeerRefundDialogstep2!!.setContentView(R.layout.dialoglayoutrefundcommandeerlaststepconfirmedpay)
        Objects.requireNonNull(commandeerRefundDialogstep2!!.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        //commandeerRefundDialogstep2.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        //dialog.getWindow().setLayout(500, 500);
        commandeerRefundDialogstep2!!.setCancelable(false)
        val ivBack = commandeerRefundDialogstep2!!.findViewById<ImageView>(R.id.iv_back)
        val textView = commandeerRefundDialogstep2!!.findViewById<TextView>(R.id.textView2)
        val imCon = commandeerRefundDialogstep2!!.findViewById<ImageView>(R.id.ic_confirm)
        val ok = commandeerRefundDialogstep2!!.findViewById<Button>(R.id.btn_ok)
        val printBtn = commandeerRefundDialogstep2!!.findViewById<Button>(R.id.btn_print)
        textView.text = "Payment Status:" + payresponse.status
        if (payresponse.status == "complete") {
            imCon.setImageResource(R.drawable.ic_baseline_check_circle_24)
            if (pop2Dialog != null) {
                if (pop2Dialog!!.isShowing) {
                    pop2Dialog!!.dismiss()
                }
            }
            val invoiceForPrint = InvoiceForPrint()
            invoiceForPrint.created_at = payresponse.created_at
            invoiceForPrint.msatoshi = payresponse.msatoshi
            invoiceForPrint.destination = payresponse.destination
            invoiceForPrint.payment_hash = payresponse.payment_hash
            invoiceForPrint.payment_preimage = payresponse.payment_preimage
            GlobalState.instance!!.invoiceForPrint = invoiceForPrint
        }
        ok.setOnClickListener { //routingNodeExecute(clientNodeID222);
            //updateMerchantClientMaxBoost();
//                openActivity(MainActivity.class);

            //quoc testing
            openActivity(MerchantLink::class.java)
            //quoc testing
            commandeerRefundDialogstep2!!.dismiss()
        }
        printBtn.setOnClickListener { //TODO: DO Print Stuff HEre!!
            //:TODO : do what need to make print..
            if (payresponse.status == "complete") {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
                if (!mBluetoothAdapter!!.isEnabled()) {
                    dialogBoxForConnecctingBTPrinter()
                } else {
                    if (mBluetoothSocket != null) {
                        Toast.makeText(
                            this@MerchantBoostTerminal,
                            "Already Connected",
                            Toast.LENGTH_LONG
                        ).show()
                        try {
                            sendData()
                        } catch (e: IOException) {
                            Log.e("SendDataError", e.toString())
                            e.printStackTrace()
                        }
                    } else {
                        dialogBoxForConnecctingBTPrinter()
                    }
                }
            } else {
                commandeerRefundDialogstep2!!.dismiss()
            }
        }

        // progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener {
            commandeerRefundDialogstep2!!.dismiss()
            openActivity(MainActivity::class.java)
        }
        commandeerRefundDialogstep2!!.show()
    }

    private fun dialogBoxForRefundCommandeerStep2(
        bolt11value: String,
        msatoshi: String,
        url: String,
        pass: String,
        fa2pass: String
    ) {
        //quoc testing temp add
        commandeerRefundDialog!!.dismiss()
        //quoc testing temp add
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        commandeerRefundDialogstep2 = Dialog(this@MerchantBoostTerminal)
        commandeerRefundDialogstep2!!.setContentView(R.layout.dialoglayoutrefundcommandeerstep2)
        Objects.requireNonNull(commandeerRefundDialogstep2!!.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        //commandeerRefundDialogstep2.getWindow().setLayout((int) (width / 1.2f), (int) (height / 1.3));
        //dialog.getWindow().setLayout(500, 500);
        commandeerRefundDialogstep2!!.setCancelable(false)
        var mst = msatoshi
        val btc = mSatoshoToBtc(java.lang.Double.valueOf(msatoshi))
        val priceInBTC = GlobalState.instance!!.currentAllRate!!.uSD!!.last
        var usd = priceInBTC!! * btc
        usd = round(usd, 2)
        mst = usd.toString()
        val bolt11 = commandeerRefundDialogstep2!!.findViewById<TextView>(R.id.bolt11valtxt)
        val label = commandeerRefundDialogstep2!!.findViewById<TextView>(R.id.labelvaltxt)
        val amount = commandeerRefundDialogstep2!!.findViewById<EditText>(R.id.amountval)
        amount.setText(mst)
        amount.inputType = InputType.TYPE_NULL
        val ivBack = commandeerRefundDialogstep2!!.findViewById<ImageView>(R.id.iv_back)
        val excecute = commandeerRefundDialogstep2!!.findViewById<Button>(R.id.btn_next)
        bolt11.text = bolt11value
        label.text = "outgoingMaxBoost$unixTimeStamp"
        if (msatoshi == "0.0") {
            excecute.visibility = View.INVISIBLE
        }
        //progressBar = dialog.findViewById(R.id.progress_bar);
        ivBack.setOnClickListener { commandeerRefundDialogstep2!!.dismiss() }
        excecute.setOnClickListener(View.OnClickListener {
            val bolt11val = bolt11.text.toString()
            val labelval = label.text.toString()
            val amountval = amount.text.toString()
            var status = true
            if (bolt11val.isEmpty()) {
                showToast("Bolt11 " + getString(R.string.empty))
                status = false
                return@OnClickListener
            }
            if (labelval.isEmpty()) {
                showToast("Label " + getString(R.string.empty))
                status = false
                return@OnClickListener
            }
            if (status) {
                //executeCommandeerRefundApi(bolt11val,labelval,amountval);
                //quoc testing temp comment
//                    CommandRefundApi(bolt11val,labelval,amountval,url,pass,fa2pass);
//                    commandeerRefundDialogstep2.dismiss();
                //quoc testing temp comment

                //quoc testing add require password
                val c: Context = this@MerchantBoostTerminal
                val enter2FaPassDialog: Dialog
                enter2FaPassDialog = Dialog(c)
                enter2FaPassDialog.setContentView(R.layout.merchat_twofa_pass_lay)
                Objects.requireNonNull(enter2FaPassDialog.window)?.setBackgroundDrawable(
                    ColorDrawable(
                        Color.TRANSPARENT
                    )
                )
                enter2FaPassDialog.setCancelable(false)
                val et_2Fa_pass = enter2FaPassDialog.findViewById<EditText>(R.id.taskEditText)
                val btn_confirm = enter2FaPassDialog.findViewById<Button>(R.id.btn_confirm)
                val btn_cancel = enter2FaPassDialog.findViewById<Button>(R.id.btn_cancel)
                val iv_back = enter2FaPassDialog.findViewById<ImageView>(R.id.iv_back)
                et_2Fa_pass.setHint(R.string.enterboostpassword)
                iv_back.setOnClickListener { enter2FaPassDialog.dismiss() }
                btn_confirm.setOnClickListener {
                    val task = et_2Fa_pass.text.toString()
                    if (task.isEmpty()) {
                        goAlertDialogwithOneBTn(1, "", "Enter 2FA Password", "Ok", "")
                    } else {
                        if (task == merchantData!!.password) {
                            CommandRefundApi(bolt11val, labelval, amountval, url, pass, task)
                            commandeerRefundDialogstep2!!.dismiss()
                            enter2FaPassDialog.dismiss()
                        } else {
                            goAlertDialogwithOneBTn(1, "", "Incorrect Password", "Retry", "")
                        }
                    }
                }
                btn_cancel.setOnClickListener { enter2FaPassDialog.dismiss() }
                enter2FaPassDialog.show()
                //quoc testing add require password


                //Confirmationn MSg
            }
        })
        commandeerRefundDialogstep2!!.show()
    }

    private fun parseJSONForPayOthers(jsonString: String): Pay? {
        var pay: Pay? = null
        var jsonArr: JSONArray? = null
        try {
            jsonArr = JSONArray(jsonString)
        } catch (e: JSONException) {
            // e.printStackTrace();
            Log.e("Error", e.message!!)
        }
        var jsonObj: JSONObject? = null
        if (jsonArr != null) {
            try {
                jsonObj = jsonArr.getJSONObject(0)
            } catch (e: JSONException) {
                //e.printStackTrace();
                Log.e("Error", e.message!!)
            }
        }
        if (jsonObj != null) {
            try {
                val gson = Gson()
                val type = object : TypeToken<Pay?>() {}.type
                pay = gson.fromJson(jsonObj.toString(), type)
                // GlobalState.getInstance().setInvoice(pay);
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
            }
        }
        return pay
    }

    private fun parseJSONForDecodePayBolt11(jsonString: String): DecodePayBolt11? {
        var decodePayBolt11: DecodePayBolt11? = null
        var jsonArr: JSONArray? = null
        try {
            jsonArr = JSONArray(jsonString)
        } catch (e: JSONException) {
            //e.printStackTrace();
            Log.e("Error", e.message!!)
        }
        var jsonObj: JSONObject? = null
        if (jsonArr != null) {
            try {
                jsonObj = jsonArr.getJSONObject(0)
            } catch (e: JSONException) {
                //e.printStackTrace();
                Log.e("Error", e.message!!)
            }
        }
        if (jsonObj != null) {
            try {
                val gson = Gson()
                val type = object : TypeToken<DecodePayBolt11?>() {}.type
                decodePayBolt11 = gson.fromJson(jsonObj.toString(), type)
                GlobalState.instance!!.currentDecodePayBolt11 = decodePayBolt11
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
            }
        }
        return decodePayBolt11
    }

    private inner class GetInfoOFLineNode(private val parent: Activity) :
        AsyncTask<String?, Int?, String>() {
        private val pdLoading: ProgressDialog?

        init {
            // record the calling activity, to use in showing/hiding dialogs
            pdLoading = ProgressDialog(parent)
            pdLoading.setMessage("Loading...")
        }

        override fun onPreExecute() {
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
            pdLoading!!.show()
            pdLoading.setCancelable(false)
            pdLoading.setCanceledOnTouchOutside(false)
        }

        protected override fun doInBackground(vararg urls: String?): String {
            // called on the background thread
            var response = ""
            val count = urls.size
            val soverignLink = urls[0]
            val user = urls[1]
            val pass = urls[2]
            val clientMainNodeId = urls[3]
            var lat: String? = "0.0"
            if (GlobalState.instance!!.lattitude != null) {
                lat = GlobalState.instance!!.lattitude
            }
            var lng: String? = "0.0"
            if (GlobalState.instance!!.longitude != null) {
                lng = GlobalState.instance!!.longitude
            }
            val query =
                "rpc-cmd,cli-node," + lat + "_" + lng + "," + System.currentTimeMillis() / 1000 + ",[ listpeers " + clientMainNodeId + " ]"
            val ipport = soverignLink?.split(":".toRegex())!!.toTypedArray()
            val ip = ipport[0]
            val port = Integer.valueOf(ipport[1])
            // port=18000;
            val status = Boolean.valueOf(NetworkManager.instance.connectClient(ip, port))
            if (status) {
                val role = NetworkManager.instance.validateUser(user!!, pass!!)
                if (role == ADMINROLE) {
                    try {
                        NetworkManager.instance.sendToServer(query)
                    } catch (e: Exception) {
                        Log.e(TAG, e.localizedMessage)
                    }
                    try {
                        response = NetworkManager.instance.recvFromServer()
                    } catch (e: Exception) {
                        Log.e(TAG, e.localizedMessage)
                    }
                    if (response.contains("peers")) {
                        Log.e(TAG, response)
                        try {
                            NetworkManager.instance.sendToServer("bye")
                        } catch (e: Exception) {
                            Log.e(TAG, e.localizedMessage)
                        }
                    } else {
                        try {
                            NetworkManager.instance.sendToServer("bye")
                        } catch (e: Exception) {
                            Log.e(TAG, e.localizedMessage)
                        }
                        response = "fail"
                    }
                } else {
                    response = "fail"
                }
            } else {
                response = "fail"
            }
            return response
        }

        protected override fun onProgressUpdate(vararg progress: Int?) {
            // called on the UI thread
            //setProgressPercent(progress[0]);
        }

        override fun onPostExecute(result: String) {
            // this method is called back on the UI thread, so it's safe to
            //  make UI calls (like dismissing a dialog) here
            //  parent.dismissDialog(LOADING_DIALOG);
            try {
                if (pdLoading != null && pdLoading.isShowing) {
                    pdLoading.dismiss()
                }
            } catch (e: Exception) {
                // e.printStackTrace();
                Log.e("Error", e.message!!)
            }
            if (result == "fail") {
                parseJSONForNodeLineInfor("[ {   \"peers\": []} ]")
                //Toast.makeText(parent,result,Toast.LENGTH_SHORT).show();
            } else {
                val resaray = result.split(",".toRegex()).toTypedArray()
                if (result.contains("peers")) {
                    if (resaray[0].contains("resp") && resaray[1].contains("status") && resaray[2].contains(
                            "rpc-cmd"
                        ) && resaray[3].contains("cli-node")
                    ) {
                        var jsonresponse = ""
                        for (i in 4 until resaray.size) {
                            jsonresponse += "," + resaray[i]
                        }
                        jsonresponse = jsonresponse.substring(1)
                        parseJSONForNodeLineInfor(jsonresponse)
                    } else {
                        parseJSONForNodeLineInfor("[ {   \"peers\": []} ]")
                    }
                } else {
                    parseJSONForNodeLineInfor("[ {   \"peers\": []} ]")
                }
            }
            Log.e(TAG, "LineNodeResultatPOST:$result")
            count += 1
            if (routingNodeArrayList != null) {
                if (routingNodeArrayList!!.size >= count + 1) {
                    val soverignLink =
                        routingNodeArrayList!![count].ip + ":" + routingNodeArrayList!![count].port
                    val user = routingNodeArrayList!![count].username
                    val pass = routingNodeArrayList!![count].password
                    executeRoutingNodeCals(soverignLink, user!!, pass!!, clientNodeId)
                } else {
                    updatetheNodeList()
                }
            }
        }


    }

    private fun updatetheNodeList() {
        val tempList = GlobalState.instance!!.nodeLineInfoArrayList
        if (tempList != null) {
            if (tempList.size > 0) {
                for (x in tempList.indices) {
                    if (tempList[x].isOn) {
                        when (x) {
                            0 -> {
                                //line1maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(0).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE1_MAX_BOOST = 0.0
                                var i = 0
                                while (i < tempList[0].channels.size) {
                                    LINE1_MAX_BOOST =
                                        LINE1_MAX_BOOST + mSatoshoToBtc(tempList[0].channels[i].spendable_msatoshi)
                                    i++
                                }
                                LINE1_MAX_BOOST = round(getUsdFromBtc(LINE1_MAX_BOOST), 2)
                                line1maxboostamount!!.text =
                                    "$" + String.format("%.2f", round(LINE1_MAX_BOOST, 2))
                                line1maxboostamountBTC!!.text =
                                    excatFigure(round(getBtcFromUsd(LINE1_MAX_BOOST), 9)) + "BTC"
                                isGetNodeLine1MaxBoost = true
                            }
                            1 -> {
                                //line2maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(1).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE2_MAX_BOOST = 0.0
                                var i = 0
                                while (i < tempList[1].channels.size) {
                                    LINE2_MAX_BOOST =
                                        LINE2_MAX_BOOST + mSatoshoToBtc(tempList[1].channels[i].spendable_msatoshi)
                                    i++
                                }
                                LINE2_MAX_BOOST = round(getUsdFromBtc(LINE2_MAX_BOOST), 2)
                                line2maxboostamount!!.text =
                                    "$" + String.format("%.2f", round(LINE2_MAX_BOOST, 2))
                                line2maxboostamountBTC!!.text =
                                    excatFigure(round(getBtcFromUsd(LINE2_MAX_BOOST), 9)) + "BTC"
                                isGetNodeLine2MaxBoost = true
                            }
                            2 -> {
                                //line3maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(2).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE3_MAX_BOOST = 0.0
                                var i = 0
                                while (i < tempList[2].channels.size) {
                                    LINE3_MAX_BOOST =
                                        LINE3_MAX_BOOST + mSatoshoToBtc(tempList[2].channels[i].spendable_msatoshi)
                                    i++
                                }
                                LINE3_MAX_BOOST = round(getUsdFromBtc(LINE3_MAX_BOOST), 2)
                                line3maxboostamount!!.text =
                                    "$" + String.format("%.2f", round(LINE3_MAX_BOOST, 2))
                                line3maxboostamountBTC!!.text =
                                    excatFigure(round(getBtcFromUsd(LINE3_MAX_BOOST), 9)) + "BTC"
                                isGetNodeLine3MaxBoost = true
                            }
                            3 -> {
                                // line4maxboostamount.setText(String.valueOf(excatFigure(mSatoshoToBtc(tempList.get(3).getChannels().get(0).getSpendable_msatoshi()))));
                                LINE4_MAX_BOOST = 0.0
                                var i = 0
                                while (i < tempList[3].channels.size) {
                                    LINE4_MAX_BOOST =
                                        LINE4_MAX_BOOST + mSatoshoToBtc(tempList[3].channels[i].spendable_msatoshi)
                                    i++
                                }
                                LINE4_MAX_BOOST = round(getUsdFromBtc(LINE4_MAX_BOOST), 2)
                                line4maxboostamount!!.text =
                                    "$" + String.format("%.2f", round(LINE4_MAX_BOOST, 2))
                                line4maxboostamountBTC!!.text =
                                    excatFigure(round(getBtcFromUsd(LINE4_MAX_BOOST), 9)) + "BTC"
                                isGetNodeLine4MaxBoost = true
                            }
                            else -> {
                                line1maxboostamount!!.text = "0.0"
                                line2maxboostamount!!.text = "0.0"
                                line3maxboostamount!!.text = "0.0"
                                line4maxboostamount!!.text = "0.0"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun parseJSONForNodeLineInfor(data: String) {
        var jsonArr: JSONArray? = null
        try {
            jsonArr = JSONArray(data)
        } catch (e: JSONException) {
            // e.printStackTrace();
            Log.e("Error", e.message!!)
        }
        var jsonObj: JSONObject? = null
        try {
            jsonObj = jsonArr!!.getJSONObject(0)
        } catch (e: JSONException) {
            //e.printStackTrace();
            Log.e("Error", e.message!!)
        }
        var ja_data: JSONArray? = null
        try {
            ja_data = jsonObj!!.getJSONArray("peers")
        } catch (e: JSONException) {
            //e.printStackTrace();
            Log.e("Error", e.message!!)
        }
        val length = jsonObj!!.length()
        var finalJSONobject: JSONObject? = null
        var sta = false
        try {
            finalJSONobject = JSONObject(ja_data!!.getJSONObject(0).toString())
            sta = true
        } catch (e: JSONException) {
            sta = false
            //e.printStackTrace();
            Log.e("error4", e.message!!)
        }
        if (sta) {
            val temp1 = finalJSONobject.toString()
            Log.e("finalJSONobject", finalJSONobject.toString())
            var nodeLineInfo = NodeLineInfo()
            val gson = Gson()
            var failed = false
            try {
                nodeLineInfo = gson.fromJson(finalJSONobject.toString(), NodeLineInfo::class.java)
                //...
            } catch (exception: IllegalStateException) {
                failed = true
                //...
            } catch (exception: JsonSyntaxException) {
                failed = true
            }
            if (failed) {
            } else {
                nodeLineInfo.isOn = true
                GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                val tempList = GlobalState.instance!!.nodeLineInfoArrayList
                Log.e("Test1", "test")
            }
            //nodeLineInfo= gson.fromJson(finalJSONobject.toString(),NodeLineInfo .class);
        } else {
            val nodeLineInfo = NodeLineInfo()
            nodeLineInfo.isOn = false
            GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
            val tempList = GlobalState.instance!!.nodeLineInfoArrayList
            Log.e("Test1", "test")
        }
        val tempList = GlobalState.instance!!.nodeLineInfoArrayList
        Log.e("Test1", "test")
        //  updateChannelInfo();
    }

    private val allRountingNodeList: Unit
        get() {
            progressDialog!!.show()
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setCancelable(false)
            val call = ApiClient.getRetrofit(this)!!.create(
                ApiInterface::class.java
            ).get_Routing_Node_List()
            call!!.enqueue(object : Callback<RoutingNodeListResp?> {
                override fun onResponse(
                    call: Call<RoutingNodeListResp?>,
                    response: Response<RoutingNodeListResp?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.message == "successfully done") {
                            if (response.body()!!.routingNodesList != null) {
                                routingNodeArrayList = response.body()!!.routingNodesList
                                if (routingNodeArrayList!!.size > 0) {
                                    GlobalState.instance!!.nodeArrayList = routingNodeArrayList
                                    Log.e(
                                        TAG,
                                        "RoutingNodeListLenght:" + routingNodeArrayList?.size
                                    )
                                } else {
                                    Log.e(TAG, "RoutingNodeListZero")
                                }
                            } else {
                                Log.e(TAG, "RoutingNodeListEmpty")
                            }
                        } else {
                            st!!.toast(response.body()!!.message)
                        }
                    }
                    progressDialog!!.dismiss()
                    allMerchantList
                }

                override fun onFailure(call: Call<RoutingNodeListResp?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    st!!.toast("Network Error")
                    allRountingNodeList
                    allMerchantList
                }
            })
        }

    private fun getNodesDataWithExecute(url: String, clientNodeId2: String, accessToken: String) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        val gson = GsonBuilder().setLenient().create()
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(
                ChuckerInterceptor.Builder(this)
                    .build()
            )
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody1: MutableMap<String, String> = HashMap()
            requestBody1["cmd"] = clientNodeId2.trim { it <= ' ' }
            val call1 =
                apiInterface.getNodesDataWithExecute("Bearer $accessToken", requestBody1.toMap())
            call1!!.enqueue(object : Callback<NodesDataWithExecuteResponse?> {
                override fun onResponse(
                    call: Call<NodesDataWithExecuteResponse?>,
                    response: Response<NodesDataWithExecuteResponse?>
                ) {
                    try {
                        val firstName = response.body()!!.nodesDataWithExecuteData!!.stdout
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
                        val object2 = JSONObject(firstName)
                        var ja_data: JSONArray? = null
                        try {
                            ja_data = object2.getJSONArray("peers")
                        } catch (e: JSONException) {
                            progressDialog!!.dismiss()
                            //e.printStackTrace();
                            Log.e("Error", e.message!!)
                            st!!.toast("Error: " + e.message)
                        }
                        if (ja_data!!.length() > 0) {
                            var finalJSONobject: JSONObject? = null
                            var sta = false
                            try {
                                finalJSONobject = JSONObject(ja_data.getJSONObject(0).toString())
                                sta = true
                            } catch (e: JSONException) {
                                progressDialog!!.dismiss()
                                sta = false
                                //e.printStackTrace();
                                Log.e("error4", e.message!!)
                                st!!.toast("Error: " + e.message)
                            }
                            if (sta) {
                                val temp1 = finalJSONobject.toString()
                                Log.e("finalJSONobject", finalJSONobject.toString())
                                var nodeLineInfo = NodeLineInfo()
                                val gson = Gson()
                                var failed = false
                                try {
                                    nodeLineInfo = gson.fromJson(
                                        finalJSONobject.toString(),
                                        NodeLineInfo::class.java
                                    )
                                    //...
                                } catch (exception: IllegalStateException) {
                                    st!!.toast("Error: " + exception.message)
                                    failed = true
                                    //...
                                } catch (exception: JsonSyntaxException) {
                                    st!!.toast("Error: " + exception.message)
                                    failed = true
                                }
                                if (failed) {
                                    //quoc testing add fake noteLineInfo
                                    val nodeLineInfo1 = NodeLineInfo()
                                    GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo1)
                                    //quoc testin add fake noteLineInfo
                                    //progressDialog.dismiss();
                                } else {
                                    // progressDialog.dismiss();
                                    nodeLineInfo.isOn = true
                                    GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                                    val tempList = GlobalState.instance!!.nodeLineInfoArrayList
                                    Log.e("Test1", "test")
                                }
                                count += 1
                                if (routingNodeArrayList != null) {
                                    if (routingNodeArrayList!!.size >= count + 1) {
                                        val soverignLink =
                                            routingNodeArrayList!![count].ip + ":" + routingNodeArrayList!![count].port
                                        val user = routingNodeArrayList!![count].username
                                        val pass = routingNodeArrayList!![count].password
                                        getNodesDataWithExecute(
                                            soverignLink,
                                            "listpeers $clientNodeId ",
                                            accessToken
                                        )
                                    } else {
                                        progressDialog!!.dismiss()
                                        updatetheNodeList()
                                    }
                                } else {
                                    progressDialog!!.dismiss()
                                }
                            } else {
                                progressDialog!!.dismiss()
                                val nodeLineInfo = NodeLineInfo()
                                nodeLineInfo.isOn = false
                                GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                                val tempList = GlobalState.instance!!.nodeLineInfoArrayList
                                Log.e("Test1", "test")
                            }
                        } else if (ja_data.toString() == "[]") {
                            count += 1
                            //quoc testing add fake noteLineInfo
                            val nodeLineInfo = NodeLineInfo()
                            GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                            //quoc testin add fake noteLineInfo
                            if (routingNodeArrayList != null) {
                                if (routingNodeArrayList!!.size >= count + 1) {
                                    val soverignLink =
                                        routingNodeArrayList!![count].ip + ":" + routingNodeArrayList!![count].port
                                    val user = routingNodeArrayList!![count].username
                                    val pass = routingNodeArrayList!![count].password
                                    getNodesDataWithExecute(
                                        soverignLink,
                                        "listpeers $clientNodeId ",
                                        accessToken
                                    )
                                } else {
                                    progressDialog!!.dismiss()
                                    updatetheNodeList()
                                }
                            } else {
                                progressDialog!!.dismiss()
                            }
                        } else {
                            // progressDialog.dismiss();
                            //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                            count = count + 1
                            //quoc testing add fake noteLineInfo
                            val nodeLineInfo = NodeLineInfo()
                            GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                            //quoc testin add fake noteLineInfo
                            if (routingNodeArrayList != null) {
                                if (routingNodeArrayList!!.size >= count + 1) {
                                    val soverignLink =
                                        routingNodeArrayList!![count].ip + ":" + routingNodeArrayList!![count].port
                                    val user = routingNodeArrayList!![count].username
                                    val pass = routingNodeArrayList!![count].password
                                    getNodesDataWithExecute(
                                        soverignLink,
                                        "listpeers $clientNodeId ",
                                        accessToken
                                    )
                                } else {
                                    progressDialog!!.dismiss()
                                    updatetheNodeList()
                                }
                            } else {
                                progressDialog!!.dismiss()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()

                        // progressDialog.dismiss();
                        //parseJSONForNodeLineInfor("[ {   \"peers\": []} ]");
                        count += 1
                        //quoc testing add fake noteLineInfo
                        val nodeLineInfo = NodeLineInfo()
                        GlobalState.instance!!.addInNodeLineInfoArrayList(nodeLineInfo)
                        //quoc testin add fake noteLineInfo
                        if (routingNodeArrayList != null) {
                            if (routingNodeArrayList!!.size >= count + 1) {
                                val soverignLink =
                                    routingNodeArrayList!![count].ip + ":" + routingNodeArrayList!![count].port
                                val user = routingNodeArrayList!![count].username
                                val pass = routingNodeArrayList!![count].password
                                getNodesDataWithExecute(
                                    soverignLink,
                                    "listpeers $clientNodeId ",
                                    accessToken
                                )
                            } else {
                                progressDialog!!.dismiss()
                                updatetheNodeList()
                            }
                        } else {
                            progressDialog!!.dismiss()
                        }
                    }
                }

                override fun onFailure(call: Call<NodesDataWithExecuteResponse?>, t: Throwable) {
                    Log.e("TAG", "onResponse: " + t.message.toString())
                    progressDialog!!.dismiss()
                    st!!.toast("Error: " + t.message)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            progressDialog!!.dismiss()
        }
    }

    private fun getARoutingAPIAuth1(
        merchantId: String?,
        merchantBackendPassword: String,
        url: String,
        clientNodeId2: String
    ) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        val requestBody: MutableMap<String, String?> = HashMap()
        requestBody["merchant_id"] = merchantId
        requestBody["merchant_backend_password"] = merchantBackendPassword
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).getRoutingAPIAuth1(requestBody.toMap())
        call!!.enqueue(object : Callback<ARoutingAPIAuthResponse?> {
            override fun onResponse(
                call: Call<ARoutingAPIAuthResponse?>,
                response: Response<ARoutingAPIAuthResponse?>
            ) {
                if (response.body() != null) {
                    authLevel1 = response.body()!!.aRoutingAPIAuthData!!.accessToken
                    getNodesDataWithExecute(
                        url,
                        clientNodeId2,
                        response.body()!!.aRoutingAPIAuthData!!.accessToken!!
                    )
                } else {
                    st!!.toast("Error")
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ARoutingAPIAuthResponse?>, t: Throwable) {
                progressDialog!!.dismiss()
                allMerchantList
                st!!.toast("Error: " + t.message)
            }
        })
    }

    private fun getARoutingAPIAuth2(
        merchantId: String?,
        boost2FAPassword: String,
        url: String,
        clientNodeId2: String
    ) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        val requestBody: MutableMap<String, String?> = HashMap()
        requestBody["merchant_id"] = merchantId
        requestBody["boost_2fa_password"] = boost2FAPassword
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).getRoutingAPIAuth2(requestBody.toMap())
        call!!.enqueue(object : Callback<ARoutingAPIAuthResponse?> {
            override fun onResponse(
                call: Call<ARoutingAPIAuthResponse?>,
                response: Response<ARoutingAPIAuthResponse?>
            ) {
                if (response.body() != null) {
                    authLevel2 = response.body()!!.aRoutingAPIAuthData!!.accessToken
                    //                    getNodesDataWithExecute(url,clientNodeId2, response.body().aRoutingAPIAuthData.accessToken);
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ARoutingAPIAuthResponse?>, t: Throwable) {
                progressDialog!!.dismiss()
                allMerchantList
                st!!.toast("Error: " + t.message)
            }
        })
    }

    private val allMerchantList: Unit
        private get() {
            progressDialog!!.show()
            progressDialog!!.setCanceledOnTouchOutside(false)
            progressDialog!!.setCancelable(false)
            val call = ApiClient.getRetrofit(this)!!.create(
                ApiInterface::class.java
            ).get_Merchant_List()
            call!!.enqueue(object : Callback<MerchantListResp?> {
                override fun onResponse(
                    call: Call<MerchantListResp?>,
                    response: Response<MerchantListResp?>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.message == "successfully done") {
                            if (response.body()!!.merchantDataList != null) {
                                allMerchantDataList = response.body()!!.merchantDataList!!
                                if (allMerchantDataList.size > 0) {
                                    GlobalState.instance!!.allMerchantDataList = allMerchantDataList
                                    Log.e(TAG, "MerchantListSize:" + allMerchantDataList.size)
                                } else {
                                    Log.e(TAG, "MerchantListSizeZero")
                                }
                            } else {
                                Log.e(TAG, "MerchantListSizeEmpty")
                            }
                        } else {
                            st!!.toast(response.body()!!.message)
                        }
                    }
                    progressDialog!!.dismiss()
                }

                override fun onFailure(call: Call<MerchantListResp?>, t: Throwable) {
                    progressDialog!!.dismiss()
                    st!!.toast("Error: " + t.message)
                }
            })
        }
    private val bitCoinValue: Unit
        private get() {
            val retrofit = Retrofit.Builder().baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create()).build()
            val call = retrofit.create(ApiInterface::class.java).bitCoin
            call!!.enqueue(object : Callback<CurrentAllRate?> {
                override fun onResponse(
                    call: Call<CurrentAllRate?>,
                    response: Response<CurrentAllRate?>
                ) {
                    var bitRate2: CurrentAllRate? = CurrentAllRate()
                    bitRate2 = response.body()
                    GlobalState.instance!!.currentAllRate = bitRate2
                    val tem = GlobalState.instance!!.currentAllRate
                    Companion.bitCoinValue = bitRate2!!.uSD!!.last!!
                }

                override fun onFailure(call: Call<CurrentAllRate?>, t: Throwable) {
                    st!!.toast("bitcoin fail: " + t.message)
                }
            })
        }

    //TODO:Add new Field in db for registartion fees
    // fundingNode.setRegistration_fees(10);
    private val fundingNodeInfor: Unit
        private get() {
            val accessToken = sp!!.getStringValue("accessToken")
            val token = "Bearer $accessToken"
            val call = ApiClient.getRetrofit(this)!!.create(
                ApiInterface::class.java
            ).get_Funding_Node_List(token)
            call!!.enqueue(object : Callback<FundingNodeListResp?> {
                override fun onResponse(
                    call: Call<FundingNodeListResp?>,
                    response: Response<FundingNodeListResp?>
                ) {
                    if (response.body() != null) {
                        val fundingNode = response.body()!!.fundingNodesList?.get(0)!!
                        //TODO:Add new Field in db for registartion fees
                        // fundingNode.setRegistration_fees(10);
                        GlobalState.instance!!.fundingNode = fundingNode
                    }
                }

                override fun onFailure(call: Call<FundingNodeListResp?>, t: Throwable) {
                    st!!.toast("RPC Failed Try Again")
                }
            })
        }

    private fun scannerIntent() {
        mode = 1
        val qrScan = IntentIntegrator(this)
        qrScan.setOrientationLocked(false)
        val prompt = "Scan Client Node ID"
        qrScan.setPrompt(prompt)
        qrScan.initiateScan()
    }

    private fun scannerIntent2() {
        mode = 3
        val qrScan: IntentIntegrator
        qrScan = IntentIntegrator(this)
        qrScan.setOrientationLocked(false)
        val prompt = "Scan Bolt11"
        qrScan.setPrompt(prompt)
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                if (mode == 3) {

                    //quoc testing temp comment
//                    commandeerRefundDialog.dismiss();
                    //quoc testing temp comment
                    bolt11fromqr = result.contents
                    // showToast(bolt11fromqr);
                    //decodeBolt11(bolt11fromqr);
                    decodeBolt1122(bolt11fromqr, "")
                } else if (mode == 1) {
                    //Toast.makeText(this, "Result Found", Toast.LENGTH_LONG).show();
                    clientNodeId = result.contents

                    val et_clientnodeid = findViewById<EditText>(R.id.et_clientnodeid)
                    et_clientnodeid.setText(clientNodeId)

                    if (clientNodeId.isNotEmpty()) {
                        clientNodeID222 = clientNodeId
                        routingNodeExecute(clientNodeId)
                    }
                } else if (mode == REQUEST_ENABLE_BT) {
                    if (resultCode == RESULT_OK) {
                        ListPairedDevices()
                        initials()
                    } else {
                        Toast.makeText(this, "Message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun showToast(message: String?) {
        Toast.makeText(this@MerchantBoostTerminal, message, Toast.LENGTH_SHORT).show()
    }

    private fun sendEmailFromClass(subject: String, message: String) {
        val javaMailAPI = JavaMailAPI(this, Utils.toEmail, subject, message)
        javaMailAPI.execute()
        val javaMailAPI2 = JavaMailAPI(this, clientData!!.email!!, subject, message)
        javaMailAPI2.execute()
        val javaMailAPI3 = JavaMailAPI(this, merchantData!!.email!!, subject, message)
        javaMailAPI3.execute()
    }

    private fun updateMerchantClientMaxBoost() {
        merchantData = GlobalState.instance!!.mainMerchantData
        clientData = GlobalState.instance!!.mainClientData
        //TODO:For Merchant Data Update
        if (merchantData != null) {
            val t = merchantData!!.merchant_maxboost!!.toDouble()
            merchantMAxBoostval!!.text = "$" + String.format("%.2f", round(t, 2))
        } else {
            merchantMAxBoostval!!.text = "$0"
        }
        //TODO:For Client Data Update
        if (clientData != null) {
            val tt = clientData!!.client_maxboost!!.toDouble()
            clientMaxBoostval!!.text = "$" + String.format("%.2f", round(tt, 2))
        } else {
            clientMaxBoostval!!.text = "$0"
        }
    }

    override fun onPause() {
        super.onPause()
        t = false
        timeStampOfPause = System.currentTimeMillis()
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

    override fun onResume() {
        super.onResume()
        if (timeStampOfPause == 0L) {
            //do nothing
            Log.e("OnResume", "1st Time Do Nothing")
        } else {
            val diffInMillisec = System.currentTimeMillis() - timeStampOfPause
            var diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMillisec)
            val seconds = diffInSec % 60
            diffInSec /= 60
            val minutes = diffInSec % 60
            diffInSec /= 60
            val hours = diffInSec % 24
            //quoc testing, quoc change from 3 to 5 for new requirement
            if (minutes > 5) {
                Log.e("OnResume", "2st Time Do SomeThing")
                startActivity(Intent(this@MerchantBoostTerminal, MerchantLink::class.java))
            } else {
                //st.toast("no");
                Log.e("OnResume", "2st Time Do Nothing")
            }
        }
        // Toast.makeText(this,"resume",Toast.LENGTH_SHORT).show();
    }

    override fun onBackPressed() {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MerchantBoostTerminal)
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout)
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        goAlertDialogwithOneBTnDialog.setCancelable(false)
        val alertTitle_tv = goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertTitle)
        val alertMessage_tv =
            goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertMessage)
        val yesbtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.yesbtn)
        val nobtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.nobtn)
        yesbtn.text = "Yes"
        nobtn.text = "No"
        alertTitle_tv.text = ""
        alertMessage_tv.text = "Are you sure you want to exit?"
        alertTitle_tv.visibility = View.GONE
        yesbtn.setOnClickListener {
            goAlertDialogwithOneBTnDialog.dismiss()
            sp!!.clearAll()
            openActivity(MerchantLink::class.java)
            finishAffinity()
        }
        nobtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
        goAlertDialogwithOneBTnDialog.show()
    }

    //TODO:PRINTING RELATED TASKS!
    @SuppressLint("MissingPermission")
    private fun initials() {
        val tv_prgbar = blutoothDevicesDialog!!.findViewById<ProgressBar>(R.id.printerProgress)
        tv_prgbar.visibility = View.VISIBLE
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (!mBluetoothAdapter?.isEnabled!!) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            return
        }
        mPairedDevicesArrayAdapter = ArrayAdapter(this, R.layout.device_name)
        val t_blueDeviceListView =
            blutoothDevicesDialog!!.findViewById<ListView>(R.id.blueDeviceListView)
        t_blueDeviceListView.adapter = mPairedDevicesArrayAdapter
        t_blueDeviceListView.onItemClickListener = mDeviceClickListener
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        val mPairedDevices = mBluetoothAdapter!!.getBondedDevices()
        if (mPairedDevices.size > 0) {
            for (mDevice in mPairedDevices) {
                mPairedDevicesArrayAdapter!!.add(
                    """
                        ${mDevice.name}
                        ${mDevice.address}
                        """.trimIndent()
                )
            }
        } else {
            val mNoDevices = "None Paired"
            mPairedDevicesArrayAdapter!!.add(mNoDevices)
        }
        tv_prgbar.visibility = View.GONE
    }

    @SuppressLint("MissingPermission")
    private val mDeviceClickListener =
        OnItemClickListener { mAdapterView, mView, mPosition, mLong ->
            val tv_status = blutoothDevicesDialog!!.findViewById<TextView>(R.id.tv_status)
            val tv_prgbar = blutoothDevicesDialog!!.findViewById<ProgressBar>(R.id.printerProgress)
            try {
                tv_prgbar.visibility = View.VISIBLE
                tv_status.text = "Device Status:Connecting...."
                mBluetoothAdapter!!.cancelDiscovery()
                val mDeviceInfo = (mView as TextView).text.toString()
                val mDeviceAddress = mDeviceInfo.substring(mDeviceInfo.length - 17)
                mBluetoothDevice = mBluetoothAdapter!!.getRemoteDevice(mDeviceAddress)
                Handler(Looper.getMainLooper()).post {
                    // Code here will run in UI thread
                    val tv_status = blutoothDevicesDialog!!.findViewById<TextView>(R.id.tv_status)
                    val tv_prgbar =
                        blutoothDevicesDialog!!.findViewById<ProgressBar>(R.id.printerProgress)
                    try {
                        mBluetoothSocket =
                            mBluetoothDevice?.createRfcommSocketToServiceRecord(applicationUUID)
                        mBluetoothAdapter!!.cancelDiscovery()
                        mBluetoothSocket?.connect()
                        tv_status.text = "Device Status:Connected"
                        //controlLay(1);
                        tv_prgbar.visibility = View.GONE
                        blutoothDevicesDialog!!.dismiss()
                    } catch (eConnectException: IOException) {
                        tv_status.text = "Device Status:Try Again"
                        tv_prgbar.visibility = View.GONE
                        Log.e("ConnectError", eConnectException.toString())
                        closeSocket(mBluetoothSocket)
                        //controlLay(0);
                    }
                }
            } catch (ex: Exception) {
                Log.e("ConnectError", ex.toString())
            }
        }

    private fun dialogBoxForConnecctingBTPrinter() {
        val width = Resources.getSystem().displayMetrics.widthPixels
        val height = Resources.getSystem().displayMetrics.heightPixels
        blutoothDevicesDialog = Dialog(this)
        blutoothDevicesDialog!!.setContentView(R.layout.blutoothdevicelistdialoglayout)
        Objects.requireNonNull(blutoothDevicesDialog!!.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        blutoothDevicesDialog!!.window!!.setLayout((width / 1.1f).toInt(), (height / 1.3).toInt())
        //dialog.getWindow().setLayout(500, 500);
        blutoothDevicesDialog!!.setCancelable(false)
        //init dialog views
        val ivBack = blutoothDevicesDialog!!.findViewById<ImageView>(R.id.iv_back)
        val scanDevices = blutoothDevicesDialog!!.findViewById<Button>(R.id.btn_scanDevices)
        val tv_status = blutoothDevicesDialog!!.findViewById<TextView>(R.id.tv_status)
        val blueDeviceListView =
            blutoothDevicesDialog!!.findViewById<ListView>(R.id.blueDeviceListView)
        initials()
        scanDevices.setOnClickListener { initials() }
        ivBack.setOnClickListener { blutoothDevicesDialog!!.dismiss() }
        blutoothDevicesDialog!!.show()
    }

    private fun closeSocket(nOpenSocket: BluetoothSocket?) {
        try {
            nOpenSocket!!.close()
            Log.d("", "SocketClosed")
        } catch (ex: IOException) {
            Log.d("", "CouldNotCloseSocket")
        }
    }

    @SuppressLint("MissingPermission")
    private fun ListPairedDevices() {
        val mPairedDevices = mBluetoothAdapter!!.getBondedDevices()
        if (mPairedDevices.size > 0) {
            for (mDevice in mPairedDevices) {
                Log.v(
                    "", "PairedDevices: " + mDevice.name + "  "
                            + mDevice.address
                )
            }
        }
    }

    @Throws(IOException::class)
    fun sendData() {
        val x = sp!!.getStringValue("merchant_id")
        var merchantId = "test"
        if (x != null) {
            merchantId = x
        }
        try {
            btoutputstream = mBluetoothSocket!!.outputStream!!
            // the text typed by the user
            val recInvoiceForPrint = GlobalState.instance!!.invoiceForPrint
            val precision = DecimalFormat("0.00")
            if (recInvoiceForPrint != null) {
                val amount = excatFigure(
                    round(
                        mSatoshoToBtc(recInvoiceForPrint.msatoshi),
                        9
                    )
                ) + "BTC/$" + precision.format(
                    round(getUsdFromBtc(mSatoshoToBtc(recInvoiceForPrint.msatoshi)), 2)
                ) + "USD"
                val amountInBtc =
                    excatFigure(round(mSatoshoToBtc(recInvoiceForPrint.msatoshi), 9)) + "BTC"
                val amountInUsd = precision.format(
                    round(
                        getUsdFromBtc(mSatoshoToBtc(recInvoiceForPrint.msatoshi)),
                        2
                    )
                ) + "USD"
                val bitmapPaymentPreImage = getBitMapFromHex(recInvoiceForPrint.payment_preimage)
                val bitmapPaymentHash = getBitMapFromHex(recInvoiceForPrint.payment_hash)
                val bitmapDestination = getBitMapFromHex(recInvoiceForPrint.destination)
                val tem = recInvoiceForPrint.created_at
                var actTime = tem.toLong()
                actTime = Math.round(tem)
                val creatAtTime =
                    getDateFromUTCTimestamp2(actTime, AppConstants.OUTPUT_DATE_FORMATE)
                printingProgressBar!!.show()
                printingProgressBar!!.setCancelable(false)
                printingProgressBar!!.setCanceledOnTouchOutside(false)
                val finalMerchantId = merchantId
                val t: Thread = object : Thread() {
                    override fun run() {
                        btoutputstream?.let { btoutputstream ->
                            try {
                                // This is printer specific code you can comment ==== > Start
                                btoutputstream.write(PrinterCommands.reset)
                                btoutputstream.write(PrinterCommands.INIT)
                                btoutputstream.write(PrinterCommands.FEED_LINE)
                                btoutputstream.write("\n\n".toByteArray())
                                //Items title should Center
                                btoutputstream.write("\tBoost Charge".toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\t------------".toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\tStatus:".toByteArray())
                                btoutputstream.write("Complete".toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\tMerchant Id:".toByteArray())
                                btoutputstream.write(finalMerchantId.toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\tCharge Time:".toByteArray())
                                btoutputstream.write("\n\t".toByteArray())
                                btoutputstream.write(creatAtTime!!.toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\tAmount: ".toByteArray())
                                btoutputstream.write("\n\t".toByteArray())
                                btoutputstream.write(amountInBtc.toByteArray())
                                btoutputstream.write("\n\t".toByteArray())
                                btoutputstream.write(amountInUsd.toByteArray())
                                btoutputstream.write("\n".toByteArray())
                                btoutputstream.write("\tDestination:".toByteArray())
                                if (bitmapDestination != null) {
                                    val bMapScaled =
                                        Bitmap.createScaledBitmap(bitmapDestination, 250, 250, true)
                                    ByteArrayOutputStream()
                                    val printPic = PrintPic.instance
                                    printPic.init(bMapScaled)
                                    val bitmapdata = printPic.printDraw()
                                    btoutputstream.write(PrinterCommands.print)
                                    btoutputstream.write(bitmapdata)
                                    btoutputstream.write(PrinterCommands.print)
                                }
                                btoutputstream.write("-------------------------------\n".toByteArray())
                                btoutputstream.write("\tPayment Hash:".toByteArray())
                                if (bitmapPaymentHash != null) {
                                    val bMapScaled =
                                        Bitmap.createScaledBitmap(bitmapPaymentHash, 250, 250, true)
                                    ByteArrayOutputStream()
                                    val printPic = PrintPic.instance
                                    printPic.init(bMapScaled)
                                    val bitmapdata = printPic.printDraw()
                                    btoutputstream.write(PrinterCommands.print)
                                    btoutputstream.write(bitmapdata)
                                    btoutputstream.write(PrinterCommands.print)
                                }
                                btoutputstream.write("-------------------------------\n".toByteArray())
                                btoutputstream.write("\tPayment Preimage:".toByteArray())
                                if (bitmapPaymentPreImage != null) {
                                    val bMapScaled =
                                        Bitmap.createScaledBitmap(
                                            bitmapPaymentPreImage,
                                            250,
                                            250,
                                            true
                                        )
                                    ByteArrayOutputStream()
                                    val printPic = PrintPic.instance
                                    printPic.init(bMapScaled)
                                    val bitmapdata = printPic.printDraw()
                                    btoutputstream.write(PrinterCommands.print)
                                    btoutputstream.write(bitmapdata)
                                    btoutputstream.write(PrinterCommands.print)
                                }
                                sleep(1000)
                                printingProgressBar!!.dismiss()
                                //confirmPaymentDialog.dismiss();
                            } catch (e: Exception) {
                                Log.e("PrintError", "Exe ", e)
                            }
                        }

                    }
                }
                t.start()
            } else {
                btoutputstream?.write(PrinterCommands.reset)
                btoutputstream?.write(PrinterCommands.INIT)
                btoutputstream?.write(PrinterCommands.FEED_LINE)
                val paidAt = "\n\n\n\nNot Data Found\n\n\n"
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // commandeerRefundDialogstep2.dismiss();
    }

    protected fun printNewLine() {
        try {
            btoutputstream!!.write(PrinterCommands.FEED_LINE)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun goAlertDialogwithOneBTn(
        i: Int,
        alertTitleMessage: String,
        alertMessage: String?,
        alertBtn1Message: String,
        alertBtn2Message: String
    ) {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MerchantBoostTerminal)
        goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout)
        Objects.requireNonNull(goAlertDialogwithOneBTnDialog.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        goAlertDialogwithOneBTnDialog.setCancelable(false)
        val alertTitle_tv = goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertTitle)
        val alertMessage_tv =
            goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertMessage)
        val yesbtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.yesbtn)
        val nobtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.nobtn)
        yesbtn.text = alertBtn1Message
        nobtn.text = alertBtn2Message
        alertTitle_tv.text = alertTitleMessage
        alertMessage_tv.text = alertMessage
        if (i == 1) {
            nobtn.visibility = View.GONE
            alertTitle_tv.visibility = View.GONE
        } else {
        }
        yesbtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
        nobtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
        goAlertDialogwithOneBTnDialog.show()
    }

    companion object {
        const val ADMINROLE = 0
        const val MERCHANTROLE = 1000
        const val CHECKOUT = 2000
        private var bitCoinValue = 0.0
        private const val merhantMaxBoost = 200.0
        private const val clientMaxBoost = 100.0

        // Print Related
        private const val REQUEST_ENABLE_BT = 2
        private var btoutputstream: OutputStream? = null
    }
}