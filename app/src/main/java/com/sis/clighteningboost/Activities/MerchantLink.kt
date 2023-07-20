package com.sis.clighteningboost.Activities

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.gson.JsonObject
import com.google.zxing.integration.android.IntentIntegrator
import com.sis.clighteningboost.Api.ApiClient
import com.sis.clighteningboost.Api.ApiInterface
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import com.sis.clighteningboost.Models.REST.MerchantData
import com.sis.clighteningboost.Models.REST.MerchantLoginResp
import com.sis.clighteningboost.R
import com.sis.clighteningboost.utils.GlobalState.Companion.instance
import com.sis.clighteningboost.utils.SharedPreference
import com.sis.clighteningboost.utils.StaticClass
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

class MerchantLink : BaseActivity() {
    var progressDialog: ProgressDialog? = null
    var st: StaticClass? = null
    override var sp: SharedPreference? = null
    var tv_merchant_link: EditText? = null
    var tv_merchant_link_pass: EditText? = null
    var mode = 0
    var merchantId = ""
    var isConfirmMerchant = false
    var webSocketClient: WebSocketClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.merchant_link_layout2)
        //createWebSocketClient();
        //String temp=String.format("%.2f", round(0.5,2));
        //Log.e("SHora1",temp);
        st = StaticClass(this)
        progressDialog = ProgressDialog(this)
        //progressDialog.setTitle("Finding Merchant");
        progressDialog!!.setMessage("Connecting....")
        progressDialog!!.setCancelable(false)
        tv_merchant_link = findViewById(R.id.tv_merchant_link)
        tv_merchant_link_pass = findViewById(R.id.tv_merchant_pass_link)
        sp = SharedPreference(this, "local_data")
        //        if (sp.containKey("client_id")) {
//            openActivity(MerchantBoostTerminal.class);
//            finish();
//        }
//
//        if (sp.containKey("merchant_id")) {
//            tv_merchant_link.setText(sp.getStringValue("merchant_id"));
//        }
        findViewById<View>(R.id.btn_connect).setOnClickListener(View.OnClickListener { view ->
            hoverEffect(view)
            val id = tv_merchant_link?.getText().toString()
            val pass = tv_merchant_link_pass?.getText().toString()
            if (id.length == 0) {
                goAlertDialogwithOneBTn(1, "", "Please enter merchant id", "Ok", "")
                // st.toast("Please enter merchant id");
                return@OnClickListener
            } else if (pass.length == 0) {
                goAlertDialogwithOneBTn(1, "", "Please enter merchant password", "Ok", "")
                // st.toast("Please enter merchant id");
                return@OnClickListener
            }
            try {
                findMerchant(id, pass)
                tv_merchant_link?.setText("")
                tv_merchant_link_pass?.setText("")
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        })
        findViewById<View>(R.id.btn_qr_scan).setOnClickListener { scannerIntentMerchantID() }

//        if(sp.getStringValue("merchant_id") != null && !sp.getStringValue("merchant_id").equalsIgnoreCase("") && sp.getStringValue("merchant_password") != null && !sp.getStringValue("merchant_password").equalsIgnoreCase("")){
//            try {
//                findMerchant(sp.getStringValue("merchant_id"),sp.getStringValue("merchant_password"));
//            } catch (JSONException e) {
//                progressDialog.dismiss();
//                e.printStackTrace();
//            }
//        }
        createWebSocketClient()
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

    private fun scannerIntentMerchantID() {
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
                    merchantId = result.contents
                    if (merchantId.length == 0) {
                        st!!.toast("Please enter client id")
                        return
                    }
                    tv_merchant_link!!.setText(merchantId)
                    //findMerchant(merchantId);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    @Throws(JSONException::class)
    private fun findMerchant(id: String, password: String) {
        progressDialog!!.show()
        val paramObject = JsonObject()
        paramObject.addProperty("user_id", id)
        paramObject.addProperty("password", password)
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).merchant_Loging(paramObject)
        //Call<MerchantLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_Loging(id,password);
        call!!.enqueue(object : Callback<MerchantLoginResp?> {
            override fun onResponse(
                call: Call<MerchantLoginResp?>,
                response: Response<MerchantLoginResp?>
            ) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    if (response.body()!!.message == "Successfully done") {
                        var merchantData = MerchantData()
                        merchantData = response.body()!!.merchantData!!
                        instance!!.mainMerchantData = merchantData
                        sp!!.saveStringValue("merchant_id", id)
                        sp!!.saveStringValue("accessToken", merchantData.accessToken)
                        sp!!.saveStringValue("refreshToken", merchantData.refreshToken)
                        sp!!.saveStringValue("merchant_password", password)
                        //openActivity(MainActivity.class);
                        //Remove2fa
                        //goTo2FaPasswordDialog( id,merchantData);
                        //sp.saveStringValue("merchant_id", merchantId);
                        openActivity(MainActivity::class.java)
                    } else {
                        progressDialog!!.dismiss()
                        goAlertDialogwithOneBTn(
                            1,
                            "",
                            "Merchant information does not match our records.",
                            "Retry",
                            ""
                        )
                    }
                } else {
                    progressDialog!!.dismiss()
                    Log.e("Error:", response.toString())
                    goAlertDialogwithOneBTn(
                        1,
                        "",
                        "Merchant information does not match our records.",
                        "Retry",
                        ""
                    )
                }
            }

            override fun onFailure(call: Call<MerchantLoginResp?>, t: Throwable) {
                progressDialog!!.dismiss()
                goAlertDialogwithOneBTn(1, "", "Server Error", "Retry", "")
            }
        })
    }

    private fun goTo2FaPasswordDialog(merchantId: String, merchantData: MerchantData) {
        val enter2FaPassDialog: Dialog
        enter2FaPassDialog = Dialog(this@MerchantLink)
        enter2FaPassDialog.setContentView(R.layout.merchat_twofa_pass_lay)
        Objects.requireNonNull(enter2FaPassDialog.window)
            ?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        enter2FaPassDialog.setCancelable(false)
        val et_2Fa_pass = enter2FaPassDialog.findViewById<EditText>(R.id.taskEditText)
        val btn_confirm = enter2FaPassDialog.findViewById<Button>(R.id.btn_confirm)
        val btn_cancel = enter2FaPassDialog.findViewById<Button>(R.id.btn_cancel)
        val iv_back = enter2FaPassDialog.findViewById<ImageView>(R.id.iv_back)
        et_2Fa_pass.setHint(R.string.entertwofapassword)
        iv_back.setOnClickListener { enter2FaPassDialog.dismiss() }
        btn_confirm.setOnClickListener {
            clodeSoftKeyBoard()
            val task = et_2Fa_pass.text.toString()
            if (task.isEmpty()) {
                goAlertDialogwithOneBTn(1, "", "Enter 2FA Password", "Ok", "")
            } else {
                if (task == merchantData.password) {
                    enter2FaPassDialog.dismiss()
                    isConfirmMerchant = true
                    // GlobalState.getInstance().setMerchantConfirm(true);
                    val goAlertDialogwithOneBTnDialog: Dialog
                    goAlertDialogwithOneBTnDialog = Dialog(this@MerchantLink)
                    goAlertDialogwithOneBTnDialog.setContentView(R.layout.alert_dialog_layout)
                    Objects.requireNonNull(goAlertDialogwithOneBTnDialog.window)
                        ?.setBackgroundDrawable(
                            ColorDrawable(
                                Color.TRANSPARENT
                            )
                        )
                    goAlertDialogwithOneBTnDialog.setCancelable(false)
                    val alertTitle_tv =
                        goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertTitle)
                    val alertMessage_tv =
                        goAlertDialogwithOneBTnDialog.findViewById<TextView>(R.id.alertMessage)
                    val yesbtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.yesbtn)
                    val nobtn = goAlertDialogwithOneBTnDialog.findViewById<Button>(R.id.nobtn)
                    yesbtn.text = "Next"
                    nobtn.text = ""
                    alertTitle_tv.text = ""
                    alertMessage_tv.text = "Merchant Id Confirmed"
                    nobtn.visibility = View.GONE
                    alertTitle_tv.visibility = View.GONE
                    yesbtn.setOnClickListener {
                        goAlertDialogwithOneBTnDialog.dismiss()
                        sp!!.saveStringValue("merchant_id", merchantId)
                        openActivity(MainActivity::class.java)
                    }
                    nobtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
                    goAlertDialogwithOneBTnDialog.show()
                } else {
                    goAlertDialogwithOneBTn(1, "", "Incorrect Password", "Retry", "")
                }
            }
        }
        btn_cancel.setOnClickListener { enter2FaPassDialog.dismiss() }
        enter2FaPassDialog.show()
    }

    override fun onResume() {
        super.onResume()
        //statusCheck();
    }

    fun statusCheck() {
        val manager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@MerchantLink,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MerchantLink,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    1
                )
            } else {
                currentLocation
            }
            //st.toast("Enabled");
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Your GPS seems to be disabled, You Have To Enable It...")
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .show()
    }

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    val currentLocation: Unit
        get() {
            val locationRequest = LocationRequest()
            locationRequest.interval = 10000
            locationRequest.fastestInterval = 3000
            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            LocationServices.getFusedLocationProviderClient(this@MerchantLink)
                .requestLocationUpdates(locationRequest, object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        super.onLocationResult(locationResult)
                        LocationServices.getFusedLocationProviderClient(this@MerchantLink)
                            .removeLocationUpdates(this)
                        if (locationResult != null && locationResult.locations.size > 0) {
                            val latestlocationIndex = locationResult.locations.size - 1
                            val latitude = locationResult.locations[latestlocationIndex].latitude
                            val longitude = locationResult.locations[latestlocationIndex].longitude
                            sp!!.saveStringValue("latitude", latitude.toString())
                            sp!!.saveStringValue("longitude", longitude.toString())
                        }
                    }
                }, Looper.myLooper())
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
                }

                override fun onFailure(call: Call<CurrentAllRate?>, t: Throwable) {
                    st!!.toast("bitcoin fail: " + t.message)
                }
            })
        }

    override fun onBackPressed() {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MerchantLink)
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
            finishAffinity()
            finish()
        }
        nobtn.setOnClickListener { goAlertDialogwithOneBTnDialog.dismiss() }
        goAlertDialogwithOneBTnDialog.show()
    }

    fun clodeSoftKeyBoard() {
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun goAlertDialogwithOneBTn(
        i: Int,
        alertTitleMessage: String,
        alertMessage: String,
        alertBtn1Message: String,
        alertBtn2Message: String
    ) {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@MerchantLink)
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
}