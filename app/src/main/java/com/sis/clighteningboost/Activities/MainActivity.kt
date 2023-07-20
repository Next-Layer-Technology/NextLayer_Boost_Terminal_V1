package com.sis.clighteningboost.Activities

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import com.sis.clighteningboost.Api.ApiClient
import com.sis.clighteningboost.Api.ApiInterface
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import com.sis.clighteningboost.Dialog.BoostNodeDialog
import com.sis.clighteningboost.Models.REST.ClientData
import com.sis.clighteningboost.Models.REST.ClientLoginResp
import com.sis.clighteningboost.Models.REST.MerchantData
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientResp
import com.sis.clighteningboost.Models.TradeSocketResponse
import com.sis.clighteningboost.R
import com.sis.clighteningboost.utils.GlobalState.Companion.instance
import com.sis.clighteningboost.utils.SharedPreference
import com.sis.clighteningboost.utils.StaticClass
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.gusavila92.websocketclient.WebSocketClient
import java.net.URI
import java.net.URISyntaxException
import java.util.*

class MainActivity : BaseActivity() {
    var progressDialog: ProgressDialog? = null
    var st: StaticClass? = null
    override var sp: SharedPreference? = null
    var tv_client_id: EditText? = null
    var scanClientIDQr: Button? = null
    var merchantData: MerchantData? = null
    var btcprice: TextView? = null
    var tv_not_a_sovereign_partner_yet: TextView? = null
    var mode = 0
    var clientID = ""
    var mSocket: Socket? = null
    var mOnMsgReceived = false
    var mReceivingNodeId = ""
    private var webSocketClient: WebSocketClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        btcprice = findViewById(R.id.btcprice)
        tv_not_a_sovereign_partner_yet = findViewById(R.id.tv_not_a_sovereign_partner_yet)
        bitCoinValue
        val udata = "Not a sovereign partner yet?"
        //        SpannableString content = new SpannableString(udata);
//        content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
        //       tv_not_a_sovereign_partner_yet.setText(content);
//        tv_not_a_sovereign_partner_yet.setPaintFlags(tv_not_a_sovereign_partner_yet.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        tv_not_a_sovereign_partner_yet.setText(R.string.notasovereignpartneryet);
//        String htmlString="<u>Not a sovereign partner yet?</u>";
//        tv_not_a_sovereign_partner_yet.setText(Html.fromHtml(htmlString));
        scanClientIDQr = findViewById(R.id.btn_qr_scan_on_client_screen)
        tv_client_id = findViewById(R.id.tv_client_id)
        //TODO:Testing Pupose
        merchantData = instance!!.mainMerchantData
        //        tv_client_id.setText(merchantData.getMerchant_maxboost());
        st = StaticClass(this)
        findViewById<View>(R.id.flashpay_btn).setOnClickListener { view: View? -> findNearbyClientforMerchant() }
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Connecting....")
        progressDialog!!.setCancelable(false)
        sp = SharedPreference(this, "local_data")
        createSocketIOInstance()
        findViewById<View>(R.id.btn_connect).setOnClickListener { view: View? ->
            hoverEffect(view)
            val id = tv_client_id?.getText().toString()
            if (id.length == 0) {
                st!!.toast("Please enter client id")
                return@setOnClickListener
            }
            findClient(id)
        }
        findViewById<View>(R.id.btn_register).setOnClickListener { view: View? ->
            hoverEffect(view)
            openActivity(Registration::class.java)
        }
        findViewById<View>(R.id.btn_qr_scan_on_client_screen).setOnClickListener { view: View? ->
            hoverEffect(view)
            // String id = tv_client_id.getText().toString();
            scannerIntentClientID()
        }
        createWebSocketClient()
    }

    fun findNearbyClientforMerchant() {
        progressDialog!!.show()
        val accessToken = sp!!.getStringValue("accessToken")
        val token = "Bearer $accessToken"
        Log.d("Socket", mSocket!!.connected().toString() + " " + mSocket!!.id())
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).merchant_nearby_clients(token)
        call!!.enqueue(object : Callback<MerchantNearbyClientResp?> {
            override fun onResponse(
                call: Call<MerchantNearbyClientResp?>,
                response: Response<MerchantNearbyClientResp?>
            ) {
                if (response.isSuccessful) {
                    val merchantNearbyClientResp = response.body()
                    if (merchantNearbyClientResp != null && !merchantNearbyClientResp.merchantDataList!!.isEmpty()) {
                        showPopup(
                            BoostNodeDialog(
                                this@MainActivity,
                                true,
                                merchantNearbyClientResp.merchantDataList!!,
                                mSocket!!
                            )
                        )
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            response.body()!!.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Token Expired", Toast.LENGTH_SHORT).show()
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<MerchantNearbyClientResp?>, t: Throwable) {
                progressDialog!!.dismiss()
            }
        })
    }

    private fun showPopup(nodeDialog: BoostNodeDialog) {
        nodeDialog.isCancelable
        val fragmentManager = supportFragmentManager
        nodeDialog.show(fragmentManager, TAG)
    }

    private fun createWebSocketClient() {
        val uri: URI
        uri = try {
            // Connect to local host
//            uri = new URI("ws://98.226.215.246:5000");
            URI("wss://ws.bitstamp.net/")
        } catch (e: URISyntaxException) {
            e.printStackTrace()
            return
        }
        webSocketClient = object : WebSocketClient(uri) {
            override fun onOpen() {
                Log.i("WebSocket", "Session is starting")
                val json = resources.getString(R.string.channel)
                try {
                    val jsonObject = JSONObject(json)
                    webSocketClient!!.send(jsonObject.toString())
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onTextReceived(s: String) {
                Log.i("WebSocket", "Message received")
                runOnUiThread {
                    try {
                        Log.i("WebSocket", s)
                        //TextView textView = findViewById(R.id.animalSound);
                        //textView.setText(message);
                        val gson = Gson()
                        val tradeSocketResponse = gson.fromJson(
                            s,
                            TradeSocketResponse::class.java
                        )
                        if (tradeSocketResponse != null && tradeSocketResponse.tradeData != null && tradeSocketResponse.tradeData!!.price > 0) {
                            btcprice!!.text =
                                "$ " + tradeSocketResponse.tradeData!!.price.toString()
                        }
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

    private fun scannerIntentClientID() {
        mode = 1
        val qrScan: IntentIntegrator
        qrScan = IntentIntegrator(this)
        qrScan.setOrientationLocked(false)
        val prompt = "Scan Client  ID"
        qrScan.setPrompt(prompt)
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show()
            } else {
                if (mode == 1) {
                    clientID = result.contents
                    if (clientID.length == 0) {
                        st!!.toast("Please enter client id")
                        return
                    }
                    findClient(clientID)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun findClient(id: String) {
        progressDialog!!.show()
        val accessToken = sp!!.getStringValue("accessToken")
        val token = "Bearer $accessToken"
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).client_Loging(token, id)
        call!!.enqueue(object : Callback<ClientLoginResp?> {
            override fun onResponse(
                call: Call<ClientLoginResp?>,
                response: Response<ClientLoginResp?>
            ) {
                if (response.body() != null) {
                    if (response.body()!!.message == "Successfully done" && response.body()!!
                            .clientData != null
                    ) {
                        var clientData = ClientData()
                        clientData = response.body()!!.clientData!!
                        instance!!.mainClientData = clientData
                        sp!!.saveStringValue("client_id", id)
                        sp!!.saveStringValue("client_name", clientData.client_name)
                        progressDialog!!.dismiss()
                        openActivity(MerchantBoostTerminal::class.java)
                        //                    finish();
                    } else {
                        goAlertDialogwithOneBTn(
                            1,
                            "",
                            "This Client ID has not been activated",
                            "Retry",
                            ""
                        )
                        progressDialog!!.dismiss()
                    }
                } else {
                    Log.e("Error:", response.toString())
                    st!!.toast(response.toString())
                    progressDialog!!.dismiss()
                }
            }

            override fun onFailure(call: Call<ClientLoginResp?>, t: Throwable) {
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
                    instance!!.currentAllRate = bitRate2
                    val tem = instance!!.currentAllRate
                    if (tem != null) {
                        if (tem.uSD != null) {
                            if (tem.uSD!!.last != null) {
                                btcprice!!.text = "$ " + tem.uSD!!.last.toString()
                            } else {
                                btcprice!!.text = "Not Found"
                            }
                        } else {
                            btcprice!!.text = "Not Found"
                        }
                    } else {
                        btcprice!!.text = "Not Found"
                    }
                }

                override fun onFailure(call: Call<CurrentAllRate?>, t: Throwable) {
                    btcprice!!.text = "Not Found"
                    st!!.toast("bitcoin fail: " + t.message)
                }
            })
        }

    override fun onBackPressed() {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MainActivity)
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
        alertTitle_tv.visibility = View.GONE
        alertMessage_tv.text = "Are you sure you want to exit?"
        yesbtn.setOnClickListener {
            goAlertDialogwithOneBTnDialog.dismiss()
            sp!!.clearAll()
            openActivity(MerchantLink::class.java)
            finishAffinity()
        }
        nobtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
        goAlertDialogwithOneBTnDialog.show()
    }

    private fun goAlertDialogwithOneBTn(
        i: Int,
        alertTitleMessage: String,
        alertMessage: String,
        alertBtn1Message: String,
        alertBtn2Message: String
    ) {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MainActivity)
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

    private val onServerConnect = Emitter.Listener { Log.e("Response", "onServerConnected") }
    private fun createSocketIOInstance() {
        val accessToken = sp!!.getStringValue("accessToken")
        val options = IO.Options()
        val headers: MutableMap<String, List<String>> = HashMap()
        val bearer = "Bearer $accessToken"
        headers["Authorization"] = listOf(bearer)
        options.extraHeaders = headers
        try {
            mSocket = IO.socket("https://realtime.nextlayer.live", options)
            mSocket?.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
        mSocket?.on("err") { args: Array<Any> ->
            runOnUiThread {
                val data: JSONObject = args[0] as JSONObject
                Log.d("Socket", data.toString())
                Toast.makeText(this, data["message"].toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}