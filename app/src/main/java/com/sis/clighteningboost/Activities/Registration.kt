package com.sis.clighteningboost.Activities

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.os.*
import android.provider.MediaStore
import android.text.InputType
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.sis.clighteningboost.Api.ApiClient
import com.sis.clighteningboost.Api.ApiInterface
import com.sis.clighteningboost.Api.ApiInterfaceForNodes
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate
import com.sis.clighteningboost.Interface.RPCResponse
import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse
import com.sis.clighteningboost.Models.REST.*
import com.sis.clighteningboost.R
import com.sis.clighteningboost.RPC.CreateInvoice
import com.sis.clighteningboost.RPC.Invoice
import com.sis.clighteningboost.RPC.NetworkManager
import com.sis.clighteningboost.RPC.Tax
import com.sis.clighteningboost.utils.*
import com.sis.clighteningboost.utils.GlobalState.Companion.globalState
import com.sis.clighteningboost.utils.GlobalState.Companion.instance
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.math.BigDecimal
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Registration : BaseActivity() {
    var btn_next: Button? = null
    var btn_back: Button? = null
    var registration_layout_step_1: LinearLayout? = null
    var registration_layout_step_2: LinearLayout? = null
    var registration_layout_step_3: LinearLayout? = null
    var registration_layout_step_4: LinearLayout? = null
    var registration_layout_step_5: LinearLayout? = null
    var is_gamma_user_check: RelativeLayout? = null
    var selectedLay = 0
    var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
    var tv_step_text: TextView? = null
    var isUserValid = false
    var iv_gamma_user: ImageView? = null
    var show_id_picture: ImageView? = null
    var show_client_picture: ImageView? = null
    var ib_rotate_id_picture: ImageButton? = null
    var ib_rotate_client_picture: ImageButton? = null
    var select_picture_of_id: LinearLayout? = null
    var select_client_picture: LinearLayout? = null
    var show_id_picture_text: TextView? = null
    var show_client_picture_text: TextView? = null
    override var sp: SharedPreference? = null
    var select_instant_option: RelativeLayout? = null
    var select_normal_option: RelativeLayout? = null
    var et_client_date: EditText? = null
    var st: StaticClass? = null
    private var merchantData: MerchantData? = null
    var iv_instant_selected: CircleImageView? = null
    var iv_normal_selected: CircleImageView? = null
    var progressDialog: ProgressDialog? = null
    var fundingNodeTemp: FundingNode? = null
    private var TRANSACTION_LABEL = ""
    private var TRANSACTION_TIMESTAMP = ""
    private var TOTAL_AMOUNT_BTC = 0.0
    private var TOTAL_AMOUNT_USD = 0.0
    private var USD_TO_BTC_RATE = 0.0

    //registration call
    var et_client_name: EditText? = null
    var et_client_national_id: EditText? = null
    var et_client_user_id: EditText? = null
    var et_client_address: EditText? = null
    var et_client_email: EditText? = null

    // showDialog
    var dialogLayout = 0
    val layouts = ArrayList<LinearLayout>()
    var instant_pay_back_icon: ImageView? = null
    var qr_scan_code_image: ImageView? = null
    var rpcResponse: RPCResponse? = null
    private var authLevel1: String? = null
    private var authLevel2: String? = null
    var handler: Handler? = null
    var r: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration2)
        TRANSACTION_LABEL = "test"
        USD_TO_BTC_RATE = 15441.0
        isGamma = false
        st = StaticClass(this)
        sp = SharedPreference(this, "local_data")
        merchantData = instance!!.mainMerchantData
        instance!!.lattitude = sp!!.getStringValue("latitude")
        instance!!.longitude = sp!!.getStringValue("longitude")
        Log.e("MainPe:", isGamma.toString())
        timeRand = System.currentTimeMillis().toString() + "" + (Random().nextInt(1000) + 1)
        client_id = (if (isGamma) "NC" else "C") + timeRand
        Log.e("MainPe1:", isGamma.toString())
        Log.e("MainPe1:", client_id!!)
        client_image_title = "ClientIMG" + timeRand
        card_image_title = "CardIMG" + timeRand
        card_image_bitmap = drawableToBitmap(
            resources.getDrawable(R.drawable.bg_small_btn)
        )
        client_image_bitmap = drawableToBitmap(
            resources.getDrawable(R.drawable.bg_small_btn)
        )
        btn_next = findViewById(R.id.btn_next)
        btn_back = findViewById(R.id.btn_back)
        tv_step_text = findViewById(R.id.tv_step_text)
        iv_instant_selected = findViewById(R.id.iv_instant_selected)
        iv_normal_selected = findViewById(R.id.iv_normal_selected)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Registering...")
        progressDialog!!.setCancelable(false)
        //input fields
        handler = Handler()
        r = Runnable { // TODO Auto-generated method stub
            sp!!.clearAll()
            openActivity(MerchantLink::class.java)
            finishAffinity()
        }
        startHandler()
        ScreenReceiver()
        et_client_name = findViewById(R.id.et_client_name)
        et_client_national_id = findViewById(R.id.et_client_national_id)
        et_client_user_id = findViewById(R.id.et_client_user_id)
        et_client_address = findViewById(R.id.et_client_address)
        et_client_email = findViewById(R.id.et_client_email)
        et_client_date = findViewById(R.id.et_client_date)
        et_client_date?.setInputType(InputType.TYPE_NULL)
        val e = et_client_email?.getText().toString().trim { it <= ' ' }
        et_client_date?.setOnClickListener(View.OnClickListener {
            hideSoftKeyBoard()
            hoverEffect(et_client_date)
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            val picker = DatePickerDialog(
                this@Registration,
                { datePicker, year, month, day ->
                    Log.e("Date2:::", "$month-$day")
                    var m = (month + 1).toString()
                    var d = day.toString()
                    if (month + 1 < 10) {
                        m = "0$m"
                    }
                    if (day < 10) {
                        d = "0$day"
                    }
                    et_client_date?.setText("$m-$d-$year")
                }, year, month, day
            )
            picker.datePicker.maxDate = System.currentTimeMillis()
            picker.show()
        })
        et_client_date?.setOnFocusChangeListener(OnFocusChangeListener { view, b ->
            if (b) {
                et_client_date?.performClick()
            }
        })
        registration_layout_step_1 = findViewById(R.id.registration_layout_step_1)
        registration_layout_step_2 = findViewById(R.id.registration_layout_step_2)
        registration_layout_step_3 = findViewById(R.id.registration_layout_step_3)
        registration_layout_step_4 = findViewById(R.id.registration_layout_step_4)
        registration_layout_step_5 = findViewById(R.id.registration_layout_step_5)
        is_gamma_user_check = findViewById(R.id.is_gamma_user_check)
        select_picture_of_id = findViewById(R.id.select_picture_of_id)
        iv_gamma_user = findViewById(R.id.iv_gamma_user)
        show_id_picture = findViewById(R.id.show_id_picture)
        show_id_picture_text = findViewById(R.id.show_id_picture_text)
        select_client_picture = findViewById(R.id.select_client_picture)
        show_client_picture_text = findViewById(R.id.show_client_picture_text)
        show_client_picture = findViewById(R.id.show_client_picture)
        select_normal_option = findViewById(R.id.select_normal_option)
        select_instant_option = findViewById(R.id.select_instant_option)
        show_id_picture?.setVisibility(View.GONE)
        show_id_picture_text?.setVisibility(View.VISIBLE)
        show_client_picture?.setVisibility(View.GONE)
        show_client_picture_text?.setVisibility(View.VISIBLE)
        iv_gamma_user?.setVisibility(View.GONE)
        btn_next?.setOnClickListener(View.OnClickListener { view ->
            hoverEffect(view)
            nextLay()
        })
        btn_back?.setOnClickListener(View.OnClickListener { view ->
            hoverEffect(view)
            previousLay()
        })
        nextLay()
        select_instant_option?.setOnClickListener {
            hoverEffect(select_instant_option)
            if (fundingNodeTemp != null) {
                connectWithThorAndLoginAPi(
                    fundingNodeTemp!!.ip.orEmpty(),
                    fundingNodeTemp!!.port.orEmpty(),
                    fundingNodeTemp!!.username.orEmpty(),
                    fundingNodeTemp!!.password.orEmpty()
                )
                //connectWithThorAndLogin(fundingNodeTemp.getIp(),fundingNodeTemp.getPort(),fundingNodeTemp.getUsername(),fundingNodeTemp.getPassword());
                // registration(true);
                //doInBackgroundMethod( Utils.CONNECT_TO_NETWORK,Utils.CONNECT_TO_NETWORK_MESSAGE,new String[]{fundingNodeTemp.getIp(), fundingNodeTemp.getPort()});
            } else {
                fundingNodeInfor
                st!!.toast("Try again")
            }
        }
        select_normal_option?.setOnClickListener(View.OnClickListener {
            hoverEffect(select_normal_option)
            INSTANT_NORMAL = 1
            iv_normal_selected?.setVisibility(if (INSTANT_NORMAL == 1) View.VISIBLE else View.GONE)
            iv_instant_selected?.setVisibility(if (INSTANT_NORMAL == 2) View.VISIBLE else View.GONE)
        })
        is_gamma_user_check?.setOnClickListener(View.OnClickListener {
            hoverEffect(is_gamma_user_check)
            Log.e("Before:", isGamma.toString())
            Log.e("Before:", client_id!!)
            isGamma = !isGamma
            timeRand = System.currentTimeMillis().toString() + "" + (Random().nextInt(1000) + 1)
            client_id = (if (isGamma) "NC" else "C") + timeRand
            Log.e("After:", isGamma.toString())
            Log.e("After:", client_id!!)
            iv_gamma_user?.setVisibility(if (isGamma) View.VISIBLE else View.GONE)
            if (isGamma) {
                et_client_user_id?.setVisibility(View.VISIBLE)
            } else {
                et_client_user_id?.setVisibility(View.GONE)
            }
            hideSoftKeyBoard()
        })
        select_picture_of_id?.setOnClickListener(View.OnClickListener { view: View? ->
            hoverEffect(is_gamma_user_check)
            imageOptions(0)
        })
        select_client_picture?.setOnClickListener(View.OnClickListener { view: View? ->
            hoverEffect(is_gamma_user_check)
            imageOptions(1)
        })
        ib_rotate_id_picture = findViewById(R.id.ib_rotate_id_picture)
        ib_rotate_id_picture?.setOnClickListener {

            val originalBitmap = (show_id_picture?.drawable as? BitmapDrawable)?.bitmap
            originalBitmap?.rotateBitmap()?.let {
                show_id_picture?.loadImage(it)
            }
        }
        ib_rotate_client_picture = findViewById(R.id.ib_rotate_client_picture)
        ib_rotate_client_picture?.setOnClickListener {

            val originalBitmap = (show_client_picture?.drawable as? BitmapDrawable)?.bitmap
            originalBitmap?.rotateBitmap()?.let {
                show_client_picture?.loadImage(it)
            }
        }
        checkPermissions()
        fundingNodeInfor
        bitCoinValue
        val ha = Handler()
        ha.postDelayed(object : Runnable {
            override fun run() {
                //call function
                bitCoinValue
                ha.postDelayed(this, AppConstants.getLatestRateDelayTime)
            }
        }, AppConstants.getLatestRateDelayTime)
        initRPCResponse()
        //addInTransactionLog(90,0.00034,"klsjkljsd","Blackvirus1010111211gfg");
    }

    private fun imageOptions(index: Int) {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Image From")
        builder.setItems(items) { dialogInterface, i ->
            if (items[i] == "Camera") {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(intent, if (index == 0) ID_CAMERA_REQ else CLIENT_CAMERA_REQ)
            }
            if (items[i] == "Gallery") {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    if (index == 0) ID_GALLERY_REQ else CLIENT_GALLERY_REQ
                )
            }
            dialogInterface.dismiss()
        }
        builder.setCancelable(false)
        builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == ID_CAMERA_REQ) {
                val bundle = data!!.extras
                val bitmap = bundle!!["data"] as Bitmap?
                bitmap?.let {
                    show_id_picture?.loadImage(it)
                }
                show_id_picture!!.visibility = View.VISIBLE
                show_id_picture_text!!.visibility = View.GONE
                card_image_bitmap = bitmap
            } else if (requestCode == ID_GALLERY_REQ) {
                val uri = data!!.data
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    card_image_bitmap = bitmap
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                show_id_picture!!.setImageURI(uri)
                show_id_picture!!.visibility = View.VISIBLE
                show_id_picture_text!!.visibility = View.GONE
            } else if (requestCode == CLIENT_CAMERA_REQ) {
                val bundle = data!!.extras
                val bitmap = bundle!!["data"] as Bitmap?
                show_client_picture!!.setImageBitmap(bitmap)
                show_client_picture!!.visibility = View.VISIBLE
                show_client_picture_text!!.visibility = View.GONE
                client_image_bitmap = bitmap
            } else if (requestCode == CLIENT_GALLERY_REQ) {
                val uri = data!!.data
                show_client_picture!!.setImageURI(uri)
                show_client_picture!!.visibility = View.VISIBLE
                show_client_picture_text!!.visibility = View.GONE
                try {
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    client_image_bitmap = bitmap
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } else {
            show_id_picture!!.visibility = View.GONE
            show_id_picture_text!!.visibility = View.VISIBLE
        }
    }

    private fun nextLay() {
        if (verifySteps(selectedLay) == 0) {
            return
        }
        if (selectedLay == 4) {
            progressDialog!!.show()
            normal_registration(false)
        } else {
            selectedLay++
            contorlLay(selectedLay)
        }
    }

    private fun previousLay() {
        if (selectedLay == 5) {
            finish()
        } else {
            selectedLay--
            contorlLay(selectedLay)
        }
    }

    fun contorlLay(i: Int) {
//        i = 4;
        if (i == 0) {
            super.onBackPressed()
        } else if (i >= 6) {
            finish()
            openActivity(MerchantBoostTerminal::class.java)
            selectedLay = 5
            return
        }
        registration_layout_step_1!!.visibility = if (i == 1) View.VISIBLE else View.GONE
        registration_layout_step_2!!.visibility = if (i == 2) View.VISIBLE else View.GONE
        registration_layout_step_3!!.visibility =
            if (i == 3) View.VISIBLE else View.GONE
        registration_layout_step_4!!.visibility = if (i == 4) View.VISIBLE else View.GONE
        registration_layout_step_5!!.visibility =
            if (i == 5) View.VISIBLE else View.GONE
        tv_step_text!!.text = "Step $i Of 5"
        btn_back!!.visibility = if (i <= 1) View.GONE else View.VISIBLE
        if (i == 5) {
            tv_step_text!!.text = "Successful"
        }
        //        if(i==5){registration();}
    }

    private fun imageToBitmap(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val imgBytes = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(imgBytes, Base64.DEFAULT)
    }

    override fun onBackPressed() {
        previousLay()
    }

    var name: String? = null
    var nd: String? = null
    var address: String? = null
    var email: String? = null
    var date: String? = null
    var user_id = ""

    //steps verification
    private fun verifySteps(i: Int): Int {
        var i_return = 1
        // verify step 1
        if (i == 1) {
            if (isGamma) {
                name = et_client_name!!.text.toString()
                nd = et_client_national_id!!.text.toString()
                user_id = et_client_user_id!!.text.toString()
                address = et_client_address!!.text.toString()
                email = et_client_email!!.text.toString()
                date = et_client_date!!.text.toString()
                if (name!!.length == 0) {
                    st!!.toast("Please Enter Name")
                    i_return = 0
                } else if (user_id.length == 0) {
                    st!!.toast("Base ID is unavailable")
                    i_return = 0
                } else if (email!!.length == 0) {
                    st!!.toast("Please Enter Email")
                    i_return = 0
                } else if (address!!.length == 0) {
                    st!!.toast("Please Enter Address")
                    i_return = 0
                } else if (date!!.length == 0) {
                    st!!.toast("Please Enter Date")
                    i_return = 0
                } else if (!email!!.isEmpty()) {
                    if (!isValidEmail(email)) {
                        i_return = 0
                        st!!.toast("Invalid Email")
                        et_client_email!!.setText("")
                    } else if (user_id.length == 0) {
                        st!!.toast("Please Enter Date")
                        i_return = 0
                    } else if (!user_id.isEmpty()) {
                        try {
                            checkBaseID(user_id)
                            if (!isUserValid) {
                                i_return = 0
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }
                }
            } else {
                name = et_client_name!!.text.toString()
                nd = et_client_national_id!!.text.toString()
                user_id = et_client_user_id!!.text.toString()
                address = et_client_address!!.text.toString()
                email = et_client_email!!.text.toString()
                date = et_client_date!!.text.toString()
                if (name!!.length == 0) {
                    st!!.toast("Please Enter Name")
                    i_return = 0
                } /* else if (user_id.length() == 0) {
                    st.toast("Please Enter Email");
                    i_return = 0;
                }*/ else if (email!!.length == 0) {
                    st!!.toast("Please Enter Email")
                    i_return = 0
                } else if (address!!.length == 0) {
                    st!!.toast("Please Enter Address")
                    i_return = 0
                } else if (date!!.length == 0) {
                    st!!.toast("Please Enter Date")
                    i_return = 0
                } else if (!email!!.isEmpty()) {
                    if (!isValidEmail(email)) {
                        i_return = 0
                        st!!.toast("Invalid Email")
                        et_client_email!!.setText("")
                    }
                }
            }
        } else if (i == 2 && card_image_bitmap == null) {
            st!!.toast("Please Select Card Picture")
            i_return = 0
        } else if (i == 3 && client_image_bitmap == null) {
            st!!.toast("Please Select Client Picture")
            i_return = 0
        } else if (i == 4 && INSTANT_NORMAL == -1) {
            st!!.toast("Please Select At Least 1 Subscription")
            i_return = 0
        }
        //
        return i_return
    }

    @Throws(JSONException::class)
    private fun checkBaseID(baseId: String) {
        progressDialog!!.show()
        val accessToken = sp!!.getStringValue("accessToken")
        val token = "Bearer $accessToken"
        val paramObject = JsonObject()
        paramObject.addProperty("base_id", baseId)
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).merchant_check_baseID(token, paramObject)
        //Call<MerchantLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_Loging(id,password);
        call!!.enqueue(object : Callback<BaseIDRes?> {
            override fun onResponse(call: Call<BaseIDRes?>, response: Response<BaseIDRes?>) {
                progressDialog!!.dismiss()
                if (response.body() != null) {
                    if (response.body()!!.message!!.contains("base id is available")) {
                        st!!.toast("Success")
                        isUserValid = true
                        // verifySteps(1);
                        progressDialog!!.dismiss()
                    } else {
                        st!!.toast("Base ID is unavailable. Please try again.")
                        isUserValid = false
                        progressDialog!!.dismiss()
                    }
                } else {
                    // st.toast("Please Enter Another UserId This Userid Is Not Available");
                    st!!.toast("Base ID is unavailable. Please try again.")
                    progressDialog!!.dismiss()
                    isUserValid = false
                }
            }

            override fun onFailure(call: Call<BaseIDRes?>, t: Throwable) {
                isUserValid = false
                progressDialog!!.dismiss()
                st!!.toast(t.message.toString())
            }
        })
    }

    private fun checkPermissions() {
        statusCheck()
    }

    fun statusCheck() {
        val perm = arrayOfNulls<String>(6)
        val list = ArrayList<String>()
        if (checkAllowance(Manifest.permission.ACCESS_NETWORK_STATE)) {
            perm[0] = Manifest.permission.ACCESS_NETWORK_STATE
            list.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }
        if (checkAllowance(Manifest.permission.CAMERA)) {
            perm[1] = Manifest.permission.CAMERA
            list.add(Manifest.permission.CAMERA)
        }
        if (checkAllowance(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            perm[2] = Manifest.permission.READ_EXTERNAL_STORAGE
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (checkAllowance(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            perm[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (checkAllowance(Manifest.permission.MEDIA_CONTENT_CONTROL)) {
            perm[4] = Manifest.permission.MEDIA_CONTENT_CONTROL
            list.add(Manifest.permission.MEDIA_CONTENT_CONTROL)
        }
        if (checkAllowance(Manifest.permission.INTERNET)) {
            perm[5] = Manifest.permission.INTERNET
            list.add(Manifest.permission.INTERNET)
        }
        val rp = arrayOfNulls<String>(list.size)
        for (i in list.indices) {
            rp[i] = list[i]
        }
        if (list.size > 0) {
            ActivityCompat.requestPermissions(this, rp, 1)
        }
    }

    private fun checkAllowance(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) for (i in permissions.indices) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
            } else {
            }
        }
    }

    // Extra Dialog
    private fun showInstantDialog(ip: String, port: String, username: String, password: String) {
        val showInstantPayDialog: Dialog = Dialog(this@Registration)
        showInstantPayDialog.setContentView(R.layout.instant_pay_layout_1)
        Objects.requireNonNull(showInstantPayDialog.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        showInstantPayDialog.setCancelable(false)
        val btn_pay_with_lightning =
            showInstantPayDialog.findViewById<Button>(R.id.btn_pay_with_lightning)
        instant_pay_back_icon = showInstantPayDialog.findViewById(R.id.instant_pay_back_icon)
        val fee_from_db = showInstantPayDialog.findViewById<TextView>(R.id.fee_from_db)
        val perUsdBtc: Double = 1 / Companion.bitCoinValue
        var totalfee = perUsdBtc * (fundingNodeTemp!!.registration_fees.orEmpty().toDoubleOrNull()?: 0.0)
        totalfee = round(totalfee, 9)
        fee_from_db.text = excatFigure(totalfee) + " BTC / $" + String.format(
            "%.2f", round(
                fundingNodeTemp!!.registration_fees?.toDoubleOrNull()?:0.0, 2
            )
        ) + "USD"
        val layout1 = showInstantPayDialog.findViewById<LinearLayout>(R.id.layout1)
        val layout2 = showInstantPayDialog.findViewById<LinearLayout>(R.id.layout2)
        val layout3 = showInstantPayDialog.findViewById<LinearLayout>(R.id.layout3)
        val layout4 = showInstantPayDialog.findViewById<LinearLayout>(R.id.layout4)
        qr_scan_code_image = showInstantPayDialog.findViewById(R.id.qr_scan_code_image)
        val static_description =
            showInstantPayDialog.findViewById<EditText>(R.id.static_description)
        val static_label = showInstantPayDialog.findViewById<EditText>(R.id.static_label)
        val static_amount_in_satoshi =
            showInstantPayDialog.findViewById<EditText>(R.id.static_amount_in_satoshi)
        static_description.inputType = InputType.TYPE_NULL
        static_label.inputType = InputType.TYPE_NULL
        static_amount_in_satoshi.inputType = InputType.TYPE_NULL
        val invoiceRate = (fundingNodeTemp!!.registration_fees?.toDoubleOrNull() ?:0.0) * perUsdBtc
        val satoshiValue = invoiceRate * 100000000
        val satoshiValuemSat = satoshiValue * 1000
        val dmSatoshi: Double
        var dSatoshi: Double = invoiceRate * AppConstants.btcToSathosi
        dmSatoshi = dSatoshi * AppConstants.satoshiToMSathosi
        val formatter: NumberFormat = DecimalFormat("#0")
        val rMSatoshi = formatter.format(dmSatoshi)
        layouts.clear()
        layouts.add(layout1)
        layouts.add(layout2)
        layouts.add(layout3)
        layouts.add(layout4)
        controlDialogLayouts(1, layouts, instant_pay_back_icon)
        instant_pay_back_icon?.setOnClickListener {
            if (dialogLayout == 1) {
                showInstantPayDialog.dismiss()
            } else {
                dialogLayout--
                controlDialogLayouts(dialogLayout, layouts, instant_pay_back_icon)
            }
        }
        val description = sp!!.getStringValue("merchant_id")
        val label = "instant_register" + System.currentTimeMillis()
        static_description.setText(description)
        static_label.setText(label)
        static_amount_in_satoshi.setText(rMSatoshi)
        btn_pay_with_lightning.setOnClickListener { view ->
            hoverEffect(view)
            controlDialogLayouts(2, layouts, instant_pay_back_icon)
        }
        showInstantPayDialog.findViewById<View>(R.id.btn_create_invoice).setOnClickListener {
            //String query = "rpc-cmd,cli-node," + GlobalState.getInstance().getLattitude() + "_" + GlobalState.getInstance().getLongitude() + "," + System.currentTimeMillis() / 1000 + ",[ invoice " + amount + " " + label + " " + description + " ]";
            val query = "invoice $rMSatoshi $label $description"
            re_ConfirmProfile(query, ip, port, username, password)

            //doInBackgroundMethod(Utils.CREATE_INVOICE_RESPONSE, Utils.CREATE_INVOICE_RESPONSE_MESSAGE,new String[]{query});
        }
        showInstantPayDialog.findViewById<View>(R.id.btn_confirm_pay).setOnClickListener {
            //String query = "rpc-cmd,cli-node," + GlobalState.getInstance().getLattitude() + "_" + GlobalState.getInstance().getLongitude() + "," + System.currentTimeMillis() / 1000 + ",[ listinvoices " + label + " ]";
            val query = "listinvoices $label"
            confirmRegistrationInvoice2(query, ip, port, username, password)
            //confirmRegistrationInvoice(query);
            //doInBackgroundMethod( Utils.CONFIRM_INVOICE_RESPONSE, Utils.CONFIRM_INVOICE_RESPONSE_MESSAGE,new String[]{query});
        }
        showInstantPayDialog.findViewById<View>(R.id.btn_skip_invoice).setOnClickListener {
            INSTANT_NORMAL = 2
            nextLay()
            showInstantPayDialog.dismiss()
        }
        showInstantPayDialog.show()
    }

    private fun re_ConfirmProfile(
        query: String,
        ip: String,
        port: String,
        username: String,
        password: String
    ) {
        val re_ConfirmProfileDialog: Dialog
        re_ConfirmProfileDialog = Dialog(this@Registration)
        re_ConfirmProfileDialog.setContentView(R.layout.reconfier_layout_two)
        re_ConfirmProfileDialog.setCancelable(false)
        Objects.requireNonNull(re_ConfirmProfileDialog.window)?.setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        re_ConfirmProfileDialog.setCancelable(false)
        val nametxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.name)
        val emailtxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.email)
        val addresstxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.address)
        val nictxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.nic)
        val dobtxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.dob)
        val gammausertxt = re_ConfirmProfileDialog.findViewById<TextView>(R.id.gammauser)
        val backImagetxt = re_ConfirmProfileDialog.findViewById<ImageView>(R.id.backimage)
        val profileImagetxt = re_ConfirmProfileDialog.findViewById<ImageView>(R.id.profileimage)
        val nicImagetxt = re_ConfirmProfileDialog.findViewById<ImageView>(R.id.nicimage)
        val confirmtxt = re_ConfirmProfileDialog.findViewById<Button>(R.id.confirm)
        val clientImage = client_image_bitmap
        val nicImage2 = card_image_bitmap
        val clName2 = name
        val clId = client_id
        val nidd = nd
        val add = address
        val dbb = date
        val eml = email
        val regstrdt = currentDateInFormamt
        val isGam = if (isGamma) "Yes" else "No"
        nametxt.text = clName2
        nictxt.text = nidd
        emailtxt.text = eml
        addresstxt.text = add
        dobtxt.text = dbb
        gammausertxt.text = isGam
        profileImagetxt.setImageBitmap(clientImage)
        nicImagetxt.setImageBitmap(nicImage2)
        backImagetxt.setOnClickListener { re_ConfirmProfileDialog.dismiss() }
        confirmtxt.setOnClickListener {
            re_ConfirmProfileDialog.dismiss()
            reConfirmRegistrationInvoice2(query, ip, port, username, password)
            //createRegistartionInvoice1(query);
        }
        re_ConfirmProfileDialog.show()
    }

    private fun initRPCResponse() {
        rpcResponse = object : RPCResponse {
            override fun getRPCResponse(response: String?, responseType: Int) {
                response!!
                when (responseType) {
                    Utils.CREATE_INVOICE_RESPONSE -> createInvoiceResponse(response)
                    Utils.CONFIRM_INVOICE_RESPONSE -> confirmInvoiceResponse(response)
                    Utils.CONNECT_TO_NETWORK -> connectToNetworkResponse(response)
                    Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER -> validateUserResponse(response)
                    Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE -> getTaxInfoFromServer(response)
                    else -> {}
                }
            }
        }
    }

    private fun connectToNetworkResponse(response: String?) {
        val resp = if (response != null) java.lang.Boolean.parseBoolean(response) else false
        val resultstatus = toBooleanDefaultIfNull(resp)
        if (resultstatus) {
            ifServerConnectForCheckOut()
        } else {
            goAlertDialogwithOneBTn(1, "", "Server URL Is Not Exist", "Ok", "")
        }
    }

    private fun validateUserResponse(response: String) {
        val result = response.toInt()
        if (result == Utils.CHECKOUT_USER) {
            ifValidateCheckoutConnect()
        } else {
            st!!.toast("Not Connect with CheckOut")
        }
    }

    private fun getTaxInfoFromServer(response: String) {
        val result = response
        val resaray = response.split(",".toRegex()).toTypedArray()
        if (resaray[0].contains("resp")) {
            if (resaray[1] == "ok") {
                val splitresponse = response.split(",".toRegex()).toTypedArray()
                val jsonresponse = ""
                val temp = Tax()
                temp.taxpercent = resaray[4].toDouble()
                temp.taxInUSD = 1.0
                temp.taxInBTC = 0.00001
                instance!!.tax = temp
                //UncommentWhenCOmplete
                //showInstantDialog();
//                getTaxCheckout();
            } else {
                val temp = Tax()
                temp.taxpercent = 1.0
                temp.taxInBTC = 1.000
                temp.taxInUSD = 1.0
                instance!!.tax = temp
                st!!.toast("Get Tax  Failed")
            }
        }
    }

    private fun createInvoiceResponse(response: String) {
        try {
            if (response.contains("bolt11")) {
                val split = response.split(",".toRegex()).toTypedArray()
                var invoiceReponse = ""
                for (i in 4 until split.size) {
                    invoiceReponse += "," + split[i]
                }
                invoiceReponse = invoiceReponse.substring(1)
                var fInvoiceResponse = invoiceReponse.replace("[", "")
                fInvoiceResponse = fInvoiceResponse.replace("]", "")
                val temInvoice = parseJSONForCreatInvocie(fInvoiceResponse)
                if (temInvoice.bolt11 != null) {
//                        transactionID++;
                    val temHax = temInvoice.bolt11
                    val multiFormatWriter = MultiFormatWriter()
                    try {
                        val bitMatrix =
                            multiFormatWriter.encode(temHax, BarcodeFormat.QR_CODE, 600, 600)
                        val barcodeEncoder = BarcodeEncoder()
                        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                        progressDialog!!.dismiss()
                        qr_scan_code_image!!.setImageBitmap(bitmap)
                        controlDialogLayouts(3, layouts, instant_pay_back_icon)
                    } catch (e: WriterException) {
                        e.printStackTrace()
                    }
                }
            } else {
                goAlertDialogwithOneBTn(
                    1,
                    "",
                    "Please Check Your Funding/Receiving Node!",
                    "Retry",
                    ""
                )
                st!!.toast("Not contain bolt11")
            }
        } catch (e: Exception) {
            st!!.toast("createInvoiceResponse: " + e.message)
        }
    }

    private fun confirmInvoiceResponse(response: String) {
        try {
            if (response.contains("bolt11")) {
                val split = response.split(",".toRegex()).toTypedArray()
                var invoiceReponse = ""
                for (i in 4 until split.size) {
                    invoiceReponse += "," + split[i]
                }
                invoiceReponse = invoiceReponse.substring(1)
                var fInvoiceResponse = invoiceReponse.replace("[", "")
                fInvoiceResponse = fInvoiceResponse.replace("]", "")
                fInvoiceResponse = fInvoiceResponse.substring(16)
                fInvoiceResponse = fInvoiceResponse.substring(0, fInvoiceResponse.length - 2)
                val getInvoicefromServer = parseJSONForConfirmPayment(fInvoiceResponse)
                if (getInvoicefromServer != null) {
                    if (getInvoicefromServer.status == "paid") {
                        TRANSACTION_TIMESTAMP = getInvoicefromServer.paid_at.toString()
                        TRANSACTION_LABEL = getInvoicefromServer.label!!
                        TOTAL_AMOUNT_BTC = getInvoicefromServer.msatoshi
                        TOTAL_AMOUNT_BTC = TOTAL_AMOUNT_BTC / AppConstants.btcToSathosi
                        TOTAL_AMOUNT_BTC = TOTAL_AMOUNT_BTC / AppConstants.satoshiToMSathosi
                        TOTAL_AMOUNT_USD = getUsdFromBtc(TOTAL_AMOUNT_BTC)
                        USD_TO_BTC_RATE = TOTAL_AMOUNT_USD / TOTAL_AMOUNT_BTC
                        st!!.toast("paid")
                        instant_registration(true)
                    } else {
                        goAlertDialogwithOneBTn(1, "", "Payment Not Recieved", "Retry", "")
                    }
                }
            } else {
                goAlertDialogwithOneBTn(1, "", "Payment Not Recieved", "Retry", "")
            }
        } catch (e: Exception) {
            st!!.toast("confirmInvoiceRespone: " + e.message)
        }
    }

    private fun normal_registration(isActive: Boolean) {
        val clientImage = client_image_bitmap
        val nicImage = card_image_bitmap
        val clName = name
        val clId = client_id
        val nidd = nd
        val add = address
        val dbb = date
        val eml = email
        val regstrdt = currentDateInFormamt
        val isGam = if (isGamma) "1" else "0"
        val isAct = if (isActive) "1" else "0"
        val baseId = user_id
        val clientImageTitle = client_image_title
        val nicImageTitle = card_image_title
        var client_image_id: MultipartBody.Part? = null
        var card_image_id: MultipartBody.Part? = null
        var clientImageFil: File? = null
        try {
            clientImageFil = savebitmap(clientImage, unixTimeStamp)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var cardImageFile: File? = null
        try {
            cardImageFile = savebitmap(nicImage, unixTimeStamp + 1)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        client_image_id = MultipartBody.Part.createFormData(
            "client_image_id",
            clientImageFil!!.name,
            clientImageFil.asRequestBody("image/*".toMediaTypeOrNull())
        )
        card_image_id = MultipartBody.Part.createFormData(
            "card_image_id",
            cardImageFile!!.name,
            RequestBody.create("image/*".toMediaTypeOrNull(), cardImageFile)
        )
        val client_name2 = RequestBody.create("text/plain".toMediaTypeOrNull(), clName!!)
        val client_id2 = RequestBody.create("text/plain".toMediaTypeOrNull(), clId!!)
        val national_id2 = RequestBody.create("text/plain".toMediaTypeOrNull(), nidd!!)
        val address2 = RequestBody.create("text/plain".toMediaTypeOrNull(), add!!)
        val dob2 = RequestBody.create("text/plain".toMediaTypeOrNull(), dbb!!)
        val is_gamma_user2 = RequestBody.create("text/plain".toMediaTypeOrNull(), isGam)
        val registered_at2 = RequestBody.create("text/plain".toMediaTypeOrNull(), regstrdt)
        val is_active2 = RequestBody.create("text/plain".toMediaTypeOrNull(), isAct)
        val email2 = RequestBody.create("text/plain".toMediaTypeOrNull(), eml!!)
        val maxboost_limit2 = RequestBody.create("text/plain".toMediaTypeOrNull(), "25")
        val client_type = RequestBody.create("text/plain".toMediaTypeOrNull(), "normal")
        if (merchantData == null) {
            merchantData = instance!!.mainMerchantData
        }
        if (merchantData == null) {
            merchantData = globalState!!.mainMerchantData
        }
        val merchant_id = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            if (merchantData == null) "" else merchantData!!.merchant_name!!
        )
        val base_id = RequestBody.create("text/plain".toMediaTypeOrNull(), baseId)
        val per_boost_limit = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            if (merchantData == null) "" else merchantData!!.maxboost_limit!!
        )
        val max_daily_boost = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            if (merchantData == null) "" else merchantData!!.merchant_maxboost!!
        )
        val gson = GsonBuilder().setLenient().create()

        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).client_Registration(
            client_type,
            client_name2,
            client_id2,
            merchant_id,
            national_id2,
            address2,
            dob2,
            is_gamma_user2,
            registered_at2,
            is_active2,
            client_image_id,
            card_image_id,
            email2,
            maxboost_limit2,
            base_id,
            per_boost_limit,
            max_daily_boost
        )
        call!!.enqueue(object : Callback<RegistrationClientResp?> {
            override fun onResponse(
                call: Call<RegistrationClientResp?>,
                response: Response<RegistrationClientResp?>
            ) {
                val clientResp = response.body()
                if (clientResp != null) {
                    if (clientResp.message == "Register successfully") {
                        //client added
                        if (clientResp.clientData != null) {
                            val clientData = clientResp.clientData
                            client_id = clientData!!.client_id
                            //sendEmailFromClass(clientData);
                            AlertDialog.Builder(this@Registration)
                                .setTitle("Registered Successfully")
                                .setMessage("Thank you for registering for the Boost Terminal.Please allow 48 hours for your account to be activated Client ID:" + client_id)
                                .setCancelable(false)
                                .setPositiveButton("Copy To ClipBoard") { dialogInterface, i ->
                                    val clipboard =
                                        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("client id", client_id)
                                    clipboard.setPrimaryClip(clip)
                                    st!!.toast("Client ID Copied")
                                    dialogInterface.dismiss()
                                    thanksYou(
                                        client_id,
                                        clientData.next_layer_email!!,
                                        clientData.next_layer_email_password!!
                                    )
                                }
                                .setNegativeButton("Dismiss") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    thanksYou(
                                        client_id,
                                        clientData.next_layer_email!!,
                                        clientData.next_layer_email_password!!
                                    )
                                }.show()
                        } else {
                            val clientData = ClientData()
                            clientData.client_id = "Test123"
                            clientData.client_name = "Test User"
                            clientData.email = "test@test.com"
                        }
                    } else if (clientResp.message == "Client id already exist") {
                        //clientID already added
                        goAlertDialogwithOneBTn(1, "", "Client id is already exist", "Retry", "")
                    } else {
                        //client Not added
                        var cc: String? = "Error Occured"
                        if (clientResp.message != null) {
                            cc = clientResp.message
                        }
                        AlertDialog.Builder(this@Registration)
                            .setMessage(cc)
                            .setPositiveButton("Try Again") { dialogInterface, i ->
                                dialogInterface.dismiss()
                                //openActivity(MerchantLink.class);
                            }
                            .show()
                    }
                } else {
                    goAlertDialogwithOneBTn(1, "", "Server Side Error", "Retry", "")
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<RegistrationClientResp?>, t: Throwable) {
                goAlertDialogwithOneBTn(1, "", t.message, "Retry", "")
                progressDialog!!.dismiss()
            }
        })
    }

    private fun thanksYou(clientid: String?, email: String, password: String) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialogregisterfinal, null)
        val ivClientID = view.findViewById<ImageView>(R.id.ivClientID)
        val ivEmail = view.findViewById<ImageView>(R.id.ivEmailAddress)
        val ivSignInNextLayerUrl = view.findViewById<ImageView>(R.id.ivPassword)
        val bitmap = getBitMapFromHex(clientid)
        val emailAndPasswordBitmap = getBitMapFromHex("$email $password")
        val signInNextLayerUrlBitmap = getBitMapFromHex("https://mail.nextlayer.me/SOGo/so/")
        if (bitmap != null) {
            ivClientID.setImageBitmap(bitmap)
        } else {
            ivClientID.setImageResource(R.drawable.a)
        }
        if (emailAndPasswordBitmap != null) {
            ivEmail.setImageBitmap(emailAndPasswordBitmap)
        } else {
            ivEmail.setImageResource(R.drawable.a)
        }
        if (signInNextLayerUrlBitmap != null) {
            ivSignInNextLayerUrl.setImageBitmap(signInNextLayerUrlBitmap)
        } else {
            ivSignInNextLayerUrl.setImageResource(R.drawable.a)
        }
        val builder = AlertDialog.Builder(this).setCancelable(false)
            .setPositiveButton("OK") { dialog, which ->
                dialog.dismiss()
                sp!!.clearAll()
                openActivity(MerchantLink::class.java)
            }.setView(view)
        builder.create().show()
    }

    private fun instant_registration(isActive: Boolean) {
        progressDialog!!.show()
        val clientImage = client_image_bitmap
        val nicImage = card_image_bitmap
        val clName = name
        val clId = client_id
        val nidd = nd
        val add = address
        val dbb = date
        val eml = email
        val regstrdt = currentDateInFormamt
        val isGam = if (isGamma) "1" else "0"
        val isAct = if (isActive) "1" else "0"
        val clientImageTitle = client_image_title
        val nicImageTitle = card_image_title
        val baseId = user_id
        var client_image_id: MultipartBody.Part? = null
        var card_image_id: MultipartBody.Part? = null
        var clientImageFil: File? = null
        try {
            clientImageFil = savebitmap(clientImage, unixTimeStamp)
            client_image_id = MultipartBody.Part.createFormData(
                "client_image_id",
                clientImageFil!!.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), clientImageFil)
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        var cardImageFile: File? = null
        try {
            cardImageFile = savebitmap(nicImage, unixTimeStamp + 1)
            card_image_id = MultipartBody.Part.createFormData(
                "card_image_id",
                cardImageFile!!.name,
                RequestBody.create("image/*".toMediaTypeOrNull(), cardImageFile)
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val client_name2 = RequestBody.create("text/plain".toMediaTypeOrNull(), clName!!)
        val client_id2 = RequestBody.create("text/plain".toMediaTypeOrNull(), clId!!)
        val national_id2 = RequestBody.create("text/plain".toMediaTypeOrNull(), nidd!!)
        val address2 = RequestBody.create("text/plain".toMediaTypeOrNull(), add!!)
        val dob2 = RequestBody.create("text/plain".toMediaTypeOrNull(), dbb!!)
        val is_gamma_user2 = RequestBody.create("text/plain".toMediaTypeOrNull(), isGam)
        val registered_at2 = RequestBody.create("text/plain".toMediaTypeOrNull(), regstrdt)
        val is_active2 = RequestBody.create("text/plain".toMediaTypeOrNull(), isAct)
        val email2 = RequestBody.create("text/plain".toMediaTypeOrNull(), eml!!)
        var maxboost_limit2: RequestBody? =
            RequestBody.create("text/plain".toMediaTypeOrNull(), "25")
        val client_type = RequestBody.create("text/plain".toMediaTypeOrNull(), "instant")
        //        RequestBody merchant_id = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMerchant_name());
//        RequestBody base_id = RequestBody.create(MediaType.parse("text/plain"),baseId);
//        RequestBody per_boost_limit = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMaxboost_limit());
//        RequestBody max_daily_boost = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMerchant_maxboost());

        //quoc testing
        maxboost_limit2 = null
        val merchant_id =
            merchantData!!.merchant_name!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val base_id = baseId.toRequestBody("text/plain".toMediaTypeOrNull())
        val per_boost_limit = "25".toRequestBody("text/plain".toMediaTypeOrNull())
        val max_daily_boost = "25".toRequestBody("text/plain".toMediaTypeOrNull())
        //quoc testing
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).client_Registration(
            client_type,
            client_name2,
            client_id2,
            merchant_id,
            national_id2,
            address2,
            dob2,
            is_gamma_user2,
            registered_at2,
            is_active2,
            client_image_id,
            card_image_id,
            email2,
            maxboost_limit2,
            base_id,
            per_boost_limit,
            max_daily_boost
        )
        call!!.enqueue(object : Callback<RegistrationClientResp?> {
            override fun onResponse(
                call: Call<RegistrationClientResp?>,
                response: Response<RegistrationClientResp?>
            ) {
                val clientResp = response.body()
                if (clientResp != null) {
                    if (clientResp.message == "Register successfully") {
                        //client added
                        if (clientResp.clientData != null) {
                            val clientData = clientResp.clientData
                            client_id = clientData!!.client_id
                            addInTransactionLog(
                                TOTAL_AMOUNT_USD,
                                TOTAL_AMOUNT_BTC,
                                TRANSACTION_TIMESTAMP,
                                clientData.client_id!!
                            )
                            //sendEmailFromClass(clientData);
                            AlertDialog.Builder(this@Registration)
                                .setTitle("Registered Successfully")
                                .setMessage("Client ID:" + client_id)
                                .setCancelable(false)
                                .setPositiveButton("Copy To ClipBoard") { dialogInterface, i ->
                                    val clipboard =
                                        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("client id", client_id)
                                    clipboard.setPrimaryClip(clip)
                                    st!!.toast("Client ID Copied")
                                    dialogInterface.dismiss()
                                    thanksYou(
                                        client_id,
                                        clientData.next_layer_email!!,
                                        clientData.next_layer_email_password!!
                                    )
                                }
                                .setNegativeButton("Dismiss") { dialogInterface, i ->
                                    dialogInterface.dismiss()
                                    thanksYou(
                                        client_id,
                                        clientData.next_layer_email!!,
                                        clientData.next_layer_email_password!!
                                    )
                                }.show()
                        } else {
                            val clientData = ClientData()
                            clientData.client_id = "Test123"
                            clientData.client_name = "Test User"
                            clientData.email = "test@test.com"
                        }
                    } else if (clientResp.message == "Client id is already exist") {
                        //clientID already  added
                        goAlertDialogwithOneBTn(1, "", "Client id is already exist", "Retry", "")
                    } else {
                        //client Not  added
                        var cc: String? = "Error Occured"
                        if (clientResp.message != null) {
                            cc = clientResp.message
                        }
                        goAlertDialogwithOneBTn(1, "", "Client id is already exist", "Retry", "")
                        AlertDialog.Builder(this@Registration)
                            .setMessage(cc)
                            .setPositiveButton("Try Again") { dialogInterface, i ->
                                dialogInterface.dismiss()
                                openActivity(MerchantLink::class.java)
                            }
                            .show()
                    }
                } else {
                    //error
                    AlertDialog.Builder(this@Registration)
                        .setMessage("Unkown Error Occured")
                        .setPositiveButton("Try Again") { dialogInterface, i ->
                            dialogInterface.dismiss()
                            openActivity(MerchantLink::class.java)
                        }
                        .show()
                }
                Log.e("tes", "pass")
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<RegistrationClientResp?>, t: Throwable) {
                Log.e("tes", "fail")
                // showToast("Network Error:"+t.getMessage().toString());
                AlertDialog.Builder(this@Registration)
                    .setMessage(t.message.toString())
                    .setPositiveButton("Try Again") { dialogInterface, i ->
                        dialogInterface.dismiss()
                        openActivity(MerchantLink::class.java)
                    }
                    .show()
                progressDialog!!.dismiss()
            }
        })
    }

    //TODO:Add new Field in db for registartion fees
    // fundingNodeTemp.setRegistration_fees(10);
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
                        fundingNodeTemp = response.body()!!.fundingNodesList?.get(0)!!
                        //TODO:Add new Field in db for registartion fees
                        // fundingNodeTemp.setRegistration_fees(10);
                        instance!!.fundingNode = fundingNodeTemp
                        var fundingNode: FundingNode?
                        fundingNode = instance!!.fundingNode
                        if (fundingNode != null) {
                            if (fundingNode.company_email != null) {
                                val before = Utils.toEmail
                                Utils.toEmail = fundingNode.company_email!!
                                val after = Utils.toEmail
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<FundingNodeListResp?>, t: Throwable) {
                    st!!.toast("RPC Failed Try Again")
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
                    Companion.bitCoinValue = bitRate2!!.uSD!!.last!!
                }

                override fun onFailure(call: Call<CurrentAllRate?>, t: Throwable) {
                    st!!.toast("bitcoin fail: " + t.message)
                }
            })
        }

    private fun controlDialogLayouts(
        i: Int,
        layouts: ArrayList<LinearLayout>,
        instant_pay_back_icon: ImageView?
    ) {
        dialogLayout = i
        layouts[0].visibility = if (i == 1) View.VISIBLE else View.GONE
        layouts[1].visibility =
            if (i == 2 || i == 3) View.VISIBLE else View.GONE
        layouts[2].visibility = if (i == 3) View.VISIBLE else View.GONE
        layouts[3].visibility = if (i == 4) View.VISIBLE else View.GONE
        instant_pay_back_icon!!.visibility = if (i == 4) View.GONE else View.VISIBLE
    }

    private fun sendEmailFromClass(clientData: ClientData?) {
        var fundingNode: FundingNode?
        fundingNode = instance!!.fundingNode
        if (fundingNode != null) {
            if (fundingNode.company_email != null) {
                Utils.toEmail = fundingNode.company_email!!
            }
        }
        val subject = "Registered Client"
        var x = "Deactivate Account"
        x = if (clientData!!.is_active == "0") {
            "Deactivate Account"
        } else {
            "Activate Account"
        }
        val message = """
            Client ID:${clientData.client_id}
            Client Name:${clientData.client_name}
            Client Email:${clientData.email}
            Client Status:$x
            """.trimIndent()
        val javaMailAPI = JavaMailAPI(this, Utils.toEmail, subject, message)
        javaMailAPI.execute()
        var clientMail: String? = "khuwajhassan15@gmail.com"
        if (clientData != null) {
            if (clientData.email != null) {
                clientMail = clientData.email
                val javaMailAPI2 = JavaMailAPI(this, clientMail!!, subject, message)
                javaMailAPI2.execute()
            } else {
                val javaMailAPI2 = JavaMailAPI(this, clientMail!!, subject, message)
                javaMailAPI2.execute()
            }
        } else {
            val javaMailAPI2 = JavaMailAPI(this, clientMail!!, subject, message)
            javaMailAPI2.execute()
        }
    }

    fun ifServerConnectForCheckOut() {
        if (instance!!.rpcInfo != null) {
            val strEmail = instance!!.rpcInfo!!.check_out_user
            val strPassword = instance!!.rpcInfo!!.check_out_password
            // doInBackgroundMethod(Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER,Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER_MESSAGE,new String[]{strEmail,strPassword});
        }
    }

    fun ifValidateCheckoutConnect() {
        //  doInBackgroundMethod( Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE,Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE_MESSAGE,new String[]{"control,get-tax"});
    }

    private fun doInBackgroundMethod(responseType: Int, tag: String, params: Array<String?>) {
        val doInBackground = DoInBackground(this@Registration, rpcResponse!!, responseType, tag)
        doInBackground.initExecute(params)
        //kdr ho yar
    }

    private val dateTime: String
        private get() {
            val dateFormat: DateFormat = SimpleDateFormat("yyyy-mm-dd")
            val date = Date()
            return dateFormat.format(date)
        }
    private val currentDateInFormamt: String
        private get() {
            val c = Calendar.getInstance().time
            Log.e("CurrentTime", c.toString())
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c)
        }

    fun showToast(message: String?) {
        Toast.makeText(this@Registration, message, Toast.LENGTH_SHORT).show()
    }

    @Throws(IOException::class)
    fun savebitmap(bmp: Bitmap?, fileName: String): File? {
        var f: File? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try {
                var fos: FileOutputStream?
                val valuesvideos = ContentValues()
                valuesvideos.put(MediaStore.MediaColumns.DISPLAY_NAME, "/myscreen_$fileName.jpg")
                valuesvideos.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                valuesvideos.put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    Environment.DIRECTORY_PICTURES + File.separator + "DemoScreenShots"
                )
                valuesvideos.put(MediaStore.Images.Media.TITLE, "DemoScreenShots")
                val resolver = contentResolver
                val uriSavedVideo =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valuesvideos)
                fos =
                    resolver.openOutputStream(uriSavedVideo!!) as FileOutputStream?
                bmp!!.compress(Bitmap.CompressFormat.JPEG, 100, fos!!)
                Objects.requireNonNull(fos)
                f =
                    File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/DemoScreenShots/_myscreen_" + fileName + ".jpg")
            } catch (e: Exception) {
                println("Failed to create photo")
                e.printStackTrace()
            }
        } else {
            val bytes = ByteArrayOutputStream()
            bmp!!.compress(Bitmap.CompressFormat.JPEG, 60, bytes)
            f = File(
                Environment.getExternalStorageDirectory()
                    .toString() + File.separator + fileName + ".jpg"
            )
            f.createNewFile()
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            fo.close()
        }
        return f
    }

    private fun connectWithThorAndLoginAPi(
        ip: String,
        port: String,
        username: String,
        password: String
    ) {
        showInstantDialog(ip, port, username, password)
    }

    private fun connectWithThorAndLogin(
        ip: String,
        port: String,
        username: String,
        password: String
    ) {
        val connectLoginThor = ConnectLoginThor(this@Registration)
        if (Build.VERSION.SDK_INT >= 11 /*HONEYCOMB*/) {
            connectLoginThor.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                *arrayOf<String>((ip), (port), (username), (password))
            )
        } else {
            connectLoginThor.execute(
                *arrayOf<String>(
                    (ip),
                    (port),
                    (username),
                    (password)
                )
            )
        }
    }

    private inner class ConnectLoginThor(private val parent: Activity) :
        AsyncTask<String?, Int?, String>() {
        var dialog2: ProgressDialog?

        init {
            // record the calling activity, to use in showing/hiding dialogs
            dialog2 = ProgressDialog(parent)
            dialog2!!.setMessage("Connecting...")
        }

        override fun onPreExecute() {
            dialog2!!.show()
            dialog2!!.setCancelable(false)
            dialog2!!.setCanceledOnTouchOutside(false)
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected override fun doInBackground(vararg urls: String?): String? {
            // called on the background thread
            var response = ""
            val count = urls.size
            val ip = urls[0]
            val port = Integer.valueOf(urls[1])
            val user = urls[2]
            val pass = urls[3]
            val status =
                java.lang.Boolean.valueOf(NetworkManager.instance.connectClient(ip, port))
            response = if (status) {
                val role = NetworkManager.instance.validateUser(user!!, pass!!)
                if (role == AppConstants.ADMIN) {
                    "Login As Admin"
                } else {
                    "Invalid Receiving Node Login"
                }
            } else {
                "Sovereign Doesn't Exist"
            }
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
            if (result == "Sovereign Doesn't Exist" || result == "Invalid Receiving Node Login") {
                goAlertDialogwithOneBTn(1, "", result, "Retry", "")
            } else {
                //createRegistrationInvocie();
            }
            try {
                if (dialog2 != null && dialog2!!.isShowing) {
                    dialog2!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            Log.e(Registration.TAG, "ConnectLoginThorResult:$result")
        }


    }

    private fun createRegistrationInvocie() {
        //showInstantDialog();
    }

    private fun reConfirmRegistrationInvoice2(
        query: String,
        ip: String,
        port: String,
        username: String,
        password: String
    ) {
        val merchantId = sp!!.getStringValue("merchant_id")
        val merchantPwd = sp!!.getStringValue("merchant_password")
        val url = "$ip:$port"
        //        reConfirmRegistrationInvoiceApi(merchantId,url,password,query);

        //quoc testing
        getARoutingAPIAuth1(merchantId, merchantPwd, url, query)
        //        getARoutingAPIAuth2(merchantId,"123456");
        //quoc testing
    }

    private fun reConfirmRegistrationInvoiceApiWithExecute(
        merchantId: String?,
        url: String,
        pass: String?,
        commad: String
    ) {
        val dialog2: ProgressDialog
        dialog2 = ProgressDialog(this)
        dialog2.setMessage("Confirming...")
        dialog2.show()
        dialog2.setCancelable(false)
        dialog2.setCanceledOnTouchOutside(false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["cmd"] = commad
            val call = apiInterface.getDecodeBolt112WithExecute(
                "Bearer $authLevel1", requestBody.toMap()
            )
            call!!.enqueue(object : Callback<DecodeBolt112WithExecuteResponse?> {
                override fun onResponse(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    response: Response<DecodeBolt112WithExecuteResponse?>
                ) {
                    try {
//                        JSONObject object=new JSONObject(new Gson().toJson(response.body()));
//                        Iterator<String> keys = object.keys();
//                        JSONObject object1=null;
//                        HashMap<String, Object> map = new HashMap<>();
//                        if( keys.hasNext() ){
//                            String key = (String)keys.next();
//                            Object object22=object.get(key);
//                            map.put(key, object.get(key));
//                        }
                        var firstName: String? = null
                        if (response.body() != null) {
                            firstName = response.body()!!.decodeBolt112WithExecuteData!!.stdout
                        }
                        //{"code":900,"message":"Duplicatelabel\instant_register1640356814346\"}
//                        firstName = firstName.replaceAll("\\s", "");
//                        firstName= firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
//                        firstName=firstName.substring(2);
//                        firstName= firstName.replaceAll("b'", "");
//                        firstName= firstName.replaceAll("',", "");
//                        firstName= firstName.replaceAll("'", "");
//                        firstName = firstName.substring(0,firstName.length() -1);
//                        if (!firstName.contains("code")){
                        try {
//                                if (firstName.contains("bolt11")) {
                            val object2 = JSONObject(firstName)
                            val gson = Gson()
                            val type = object : TypeToken<CreateInvoice?>() {}.type
                            val createInvoice =
                                gson.fromJson<CreateInvoice>(object2.toString(), type)
                            instance!!.createInvoice = createInvoice
                            if (createInvoice != null) {
                                if (createInvoice.bolt11 != null) {
                                    //transactionID++;
                                    val temHax = createInvoice.bolt11
                                    val multiFormatWriter = MultiFormatWriter()
                                    try {
                                        val bitMatrix = multiFormatWriter.encode(
                                            temHax,
                                            BarcodeFormat.QR_CODE,
                                            600,
                                            600
                                        )
                                        val barcodeEncoder = BarcodeEncoder()
                                        val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                                        //progressDialog.dismiss();
                                        dialog2.dismiss()
                                        qr_scan_code_image!!.setImageBitmap(bitmap)
                                        controlDialogLayouts(3, layouts, instant_pay_back_icon)
                                    } catch (e: WriterException) {
                                        e.printStackTrace()
                                        dialog2.dismiss()
                                        st!!.toast("createInvoiceResponse: " + e.message.toString())
                                    }
                                } else {
                                    dialog2.dismiss()
                                }
                            } else {
                                dialog2.dismiss()
                            }

//                                }
//                                else {
//                                    goAlertDialogwithOneBTn(1,"","Please Check Your Funding/Receiving Node!","Retry","");
//                                    st.toast("Not contain bolt11");
//                                    dialog2.dismiss();
//                                }
                        } catch (e: Exception) {
                            st!!.toast("createInvoiceResponse: " + e.message)
                            dialog2.dismiss()
                        }

//                        }else {
//                            firstName= firstName.replaceAll("\\\\", "");
//                            JSONObject object2=new JSONObject(firstName);
//                            String message=object2.getString("message");
//                            st.toast(message);
//                            dialog2.dismiss();
//                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        dialog2.dismiss()
                        st!!.toast("createInvoiceResponse: " + e.message.toString())
                    }
                }

                override fun onFailure(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    t: Throwable
                ) {
                    Log.e("TAG", "onResponse: " + t.message.toString())
                    dialog2.dismiss()
                    st!!.toast("createInvoiceResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            st!!.toast("createInvoiceResponse: " + e.message)
            dialog2.dismiss()
        }
    }

    private fun reConfirmRegistrationInvoiceApi(
        merchantId: String,
        url: String,
        pass: String,
        commad: String
    ) {
        val dialog2: ProgressDialog = ProgressDialog(this)
        dialog2.setMessage("Confirming...")
        dialog2.show()
        dialog2.setCancelable(false)
        dialog2.setCanceledOnTouchOutside(false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["merchantId"] = merchantId
            requestBody["merchantBackendPassword"] = ""
            requestBody["boost2faPassword"] = ""
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
                        //{"code":900,"message":"Duplicatelabel\instant_register1640356814346\"}
                        firstName = firstName!!.replace("\\s".toRegex(), "")
                        firstName = firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "")
                        firstName = firstName.substring(2)
                        firstName = firstName.replace("b'".toRegex(), "")
                        firstName = firstName.replace("',".toRegex(), "")
                        firstName = firstName.replace("'".toRegex(), "")
                        firstName = firstName.substring(0, firstName.length - 1)
                        if (!firstName.contains("code")) {
                            try {
                                if (firstName.contains("bolt11")) {
                                    val object2 = JSONObject(firstName)
                                    val gson = Gson()
                                    val type = object : TypeToken<CreateInvoice?>() {}.type
                                    val createInvoice =
                                        gson.fromJson<CreateInvoice>(object2.toString(), type)
                                    instance!!.createInvoice = createInvoice
                                    if (createInvoice != null) {
                                        if (createInvoice.bolt11 != null) {
                                            //transactionID++;
                                            val temHax = createInvoice.bolt11
                                            val multiFormatWriter = MultiFormatWriter()
                                            try {
                                                val bitMatrix = multiFormatWriter.encode(
                                                    temHax,
                                                    BarcodeFormat.QR_CODE,
                                                    600,
                                                    600
                                                )
                                                val barcodeEncoder = BarcodeEncoder()
                                                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                                                //progressDialog.dismiss();
                                                dialog2.dismiss()
                                                qr_scan_code_image!!.setImageBitmap(bitmap)
                                                controlDialogLayouts(
                                                    3,
                                                    layouts,
                                                    instant_pay_back_icon
                                                )
                                            } catch (e: WriterException) {
                                                e.printStackTrace()
                                                dialog2.dismiss()
                                                st!!.toast("createInvoiceResponse: " + e.message.toString())
                                            }
                                        } else {
                                            dialog2.dismiss()
                                        }
                                    } else {
                                        dialog2.dismiss()
                                    }
                                } else {
                                    goAlertDialogwithOneBTn(
                                        1,
                                        "",
                                        "Please Check Your Funding/Receiving Node!",
                                        "Retry",
                                        ""
                                    )
                                    st!!.toast("Not contain bolt11")
                                    dialog2.dismiss()
                                }
                            } catch (e: Exception) {
                                st!!.toast("createInvoiceResponse: " + e.message)
                                dialog2.dismiss()
                            }
                        } else {
                            firstName = firstName.replace("\\\\".toRegex(), "")
                            val object2 = JSONObject(firstName)
                            val message = object2.getString("message")
                            st!!.toast(message)
                            dialog2.dismiss()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        dialog2.dismiss()
                        st!!.toast("createInvoiceResponse: " + e.message.toString())
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    Log.e("TAG", "onResponse: " + t.message.toString())
                    dialog2.dismiss()
                    st!!.toast("createInvoiceResponse: " + t.message.toString())
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()
            st!!.toast("createInvoiceResponse: " + e.message)
            dialog2.dismiss()
        }
    }

    private fun createRegistartionInvoice1(query: String) {
        val createRegistartionInvice = CreateRegistartionInvice(this@Registration)
        if (Build.VERSION.SDK_INT >= 11 /*HONEYCOMB*/) {
            createRegistartionInvice.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                *arrayOf<String>((query))
            )
        } else {
            createRegistartionInvice.execute(*arrayOf<String>((query)))
        }
    }

    private inner class CreateRegistartionInvice(private val parent: Activity) :
        AsyncTask<String?, Int?, String>() {
        var dialog2: ProgressDialog?

        init {
            // record the calling activity, to use in showing/hiding dialogs
            dialog2 = ProgressDialog(parent)
            dialog2!!.setMessage("Creating...")
        }

        override fun onPreExecute() {
            dialog2!!.show()
            dialog2!!.setCancelable(false)
            dialog2!!.setCanceledOnTouchOutside(false)
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected override fun doInBackground(vararg urls: String?): String? {
            // called on the background thread
            var response = ""
            val query = urls[0]
            try {
                NetworkManager.instance.sendToServer(query)
            } catch (e: Exception) {
                Log.e(Registration.TAG, e.localizedMessage)
            }
            try {
                // Try now
                response = NetworkManager.instance.recvFromServer()
            } catch (e: Exception) {
                Log.e(Registration.TAG, e.localizedMessage)
            }
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
            createInvoiceResponse(result)
            try {
                if (dialog2 != null && dialog2!!.isShowing) {
                    dialog2!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    private fun confirmRegistrationInvoice2(
        query: String,
        ip: String,
        port: String,
        username: String,
        password: String
    ) {
        val merchantId = sp!!.getStringValue("merchant_id")
        val url = "$ip:$port"
        //        ConfirmRegistrationInvoiceApi(merchantId,url,password,query);

        //quoc testing
        ConfirmRegistrationInvoiceApiWithExecute(merchantId, url, password, query)
        //quoc testing
    }

    private fun getARoutingAPIAuth1(
        merchantId: String?,
        merchantBackendPassword: String?,
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
                    reConfirmRegistrationInvoiceApiWithExecute(
                        merchantId,
                        url,
                        merchantBackendPassword,
                        clientNodeId2
                    )
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ARoutingAPIAuthResponse?>, t: Throwable) {
                progressDialog!!.dismiss()
                st!!.toast("Error: " + t.message)
            }
        })
    }

    private fun getARoutingAPIAuth2(merchantId: String, boost2FAPassword: String) {
        progressDialog!!.show()
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.setCancelable(false)
        val requestBody: MutableMap<String, String> = HashMap()
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
                }
                progressDialog!!.dismiss()
            }

            override fun onFailure(call: Call<ARoutingAPIAuthResponse?>, t: Throwable) {
                progressDialog!!.dismiss()
                st!!.toast("Error: " + t.message)
            }
        })
    }

    private fun ConfirmRegistrationInvoiceApiWithExecute(
        merchantId: String?,
        url: String,
        pass: String,
        commad: String
    ) {
        val dialog2: ProgressDialog
        dialog2 = ProgressDialog(this)
        dialog2.setMessage("Confirming...")
        dialog2.show()
        dialog2.setCancelable(false)
        dialog2.setCanceledOnTouchOutside(false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["cmd"] = commad
            val call = apiInterface.getDecodeBolt112WithExecute(
                "Bearer $authLevel1", requestBody.toMap()
            )
            call!!.enqueue(object : Callback<DecodeBolt112WithExecuteResponse?> {
                override fun onResponse(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    response: Response<DecodeBolt112WithExecuteResponse?>
                ) {
                    try {
                        val firstName = response.body()!!.decodeBolt112WithExecuteData!!.stdout
                        val object2 = JSONObject(firstName)
                        try {
                            val jsonArray = object2.getJSONArray("invoices")
                            val object3 = JSONObject(jsonArray.getJSONObject(0).toString())
                            // {"invoices":[{"label":"instant_register1640095049835","bolt11":"lnbc10227040p1psurh26pp5r9tkees3sn4w8axx3ykkhssn7nkm6fyq5h50jd5zk0yjrvz7gauqdqgdahx7enxxqyjw5qcqpjsp5jz9zm9k73v7626f2rkghuprrasw8yga3tpwj59sjkgmngge2fsdsrzjq00n7z3066l22s56t9jxrnncfjfzk2vp4ks6lzw0alxeena3dst2wz5nsyqqgusqqyqqqqlgqqqqqqgq9q9qyyssqgfcysvla950u2yh8rga588c4ddn6ljx8y5v24q2mqpyjgza7d59jc42atfc59wxe66xk88wg05lc0ae9lwxkamemccp89c37z83a6mgp5fr8c0","payment_hash":"19576ce61184eae3f4c6892d6bc213f4edbd2480a5e8f93682b3c921b05e4778","msatoshi":1022704,"amount_msat":"1022704msat","status":"unpaid","description":"onoff","expires_at":1640699866}]}
                            val gson = Gson()
                            val type = object : TypeToken<Invoice?>() {}.type
                            val invoice = gson.fromJson<Invoice>(object3.toString(), type)
                            instance!!.invoice = invoice
                            if (invoice != null) {
                                if (invoice.status == "paid") {
                                    TRANSACTION_TIMESTAMP = invoice.paid_at.toString()
                                    TRANSACTION_LABEL = invoice.label!!
                                    TOTAL_AMOUNT_BTC = invoice.msatoshi
                                    TOTAL_AMOUNT_BTC = TOTAL_AMOUNT_BTC / AppConstants.btcToSathosi
                                    TOTAL_AMOUNT_BTC =
                                        TOTAL_AMOUNT_BTC / AppConstants.satoshiToMSathosi
                                    TOTAL_AMOUNT_USD = getUsdFromBtc(TOTAL_AMOUNT_BTC)
                                    USD_TO_BTC_RATE = TOTAL_AMOUNT_USD / TOTAL_AMOUNT_BTC
                                    st!!.toast("paid")
                                    instant_registration(true)
                                    dialog2.dismiss()
                                } else {
                                    dialog2.dismiss()
                                    goAlertDialogwithOneBTn(
                                        1,
                                        "",
                                        "Payment Not Recieved",
                                        "Retry",
                                        ""
                                    )
                                }
                            } else {
                                dialog2.dismiss()
                            }
                        } catch (e: Exception) {
                            st!!.toast("confirmInvoiceRespone: " + e.message)
                            dialog2.dismiss()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        st!!.toast("confirmInvoiceRespone: " + e.message)
                        dialog2.dismiss()
                    }
                }

                override fun onFailure(
                    call: Call<DecodeBolt112WithExecuteResponse?>,
                    t: Throwable
                ) {
                    Log.e("TAG", "onResponse: " + t.message.toString())
                    st!!.toast("confirmInvoiceRespone: " + t.message.toString())
                    dialog2.dismiss()
                }
            })
        } catch (e: Exception) {
            dialog2.dismiss()
            e.printStackTrace()
        }
    }

    private fun ConfirmRegistrationInvoiceApi(
        merchantId: String,
        url: String,
        pass: String,
        commad: String
    ) {
        val dialog2: ProgressDialog
        dialog2 = ProgressDialog(this)
        dialog2.setMessage("Confirming...")
        dialog2.show()
        dialog2.setCancelable(false)
        dialog2.setCanceledOnTouchOutside(false)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://$url/")
            .client(okHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiInterface = retrofit.create(
            ApiInterfaceForNodes::class.java
        )
        try {
            val requestBody: MutableMap<String, String> = HashMap()
            requestBody["merchantId"] = merchantId
            requestBody["merchantBackendPassword"] = ""
            requestBody["boost2faPassword"] = ""
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
                        val object2 = JSONObject(firstName)
                        try {
                            if (firstName.contains("bolt11")) {
                                val jsonArray = object2.getJSONArray("invoices")
                                val object3 = JSONObject(jsonArray.getJSONObject(0).toString())
                                // {"invoices":[{"label":"instant_register1640095049835","bolt11":"lnbc10227040p1psurh26pp5r9tkees3sn4w8axx3ykkhssn7nkm6fyq5h50jd5zk0yjrvz7gauqdqgdahx7enxxqyjw5qcqpjsp5jz9zm9k73v7626f2rkghuprrasw8yga3tpwj59sjkgmngge2fsdsrzjq00n7z3066l22s56t9jxrnncfjfzk2vp4ks6lzw0alxeena3dst2wz5nsyqqgusqqyqqqqlgqqqqqqgq9q9qyyssqgfcysvla950u2yh8rga588c4ddn6ljx8y5v24q2mqpyjgza7d59jc42atfc59wxe66xk88wg05lc0ae9lwxkamemccp89c37z83a6mgp5fr8c0","payment_hash":"19576ce61184eae3f4c6892d6bc213f4edbd2480a5e8f93682b3c921b05e4778","msatoshi":1022704,"amount_msat":"1022704msat","status":"unpaid","description":"onoff","expires_at":1640699866}]}
                                val gson = Gson()
                                val type = object : TypeToken<Invoice?>() {}.type
                                val invoice = gson.fromJson<Invoice>(object3.toString(), type)
                                instance!!.invoice = invoice
                                if (invoice != null) {
                                    if (invoice.status == "paid") {
                                        TRANSACTION_TIMESTAMP = invoice.paid_at.toString()
                                        TRANSACTION_LABEL = invoice.label!!
                                        TOTAL_AMOUNT_BTC = invoice.msatoshi
                                        TOTAL_AMOUNT_BTC =
                                            TOTAL_AMOUNT_BTC / AppConstants.btcToSathosi
                                        TOTAL_AMOUNT_BTC =
                                            TOTAL_AMOUNT_BTC / AppConstants.satoshiToMSathosi
                                        TOTAL_AMOUNT_USD = getUsdFromBtc(TOTAL_AMOUNT_BTC)
                                        USD_TO_BTC_RATE = TOTAL_AMOUNT_USD / TOTAL_AMOUNT_BTC
                                        st!!.toast("paid")
                                        instant_registration(true)
                                        dialog2.dismiss()
                                    } else {
                                        dialog2.dismiss()
                                        goAlertDialogwithOneBTn(
                                            1,
                                            "",
                                            "Payment Not Recieved",
                                            "Retry",
                                            ""
                                        )
                                    }
                                } else {
                                    dialog2.dismiss()
                                }
                            } else {
                                dialog2.dismiss()
                                goAlertDialogwithOneBTn(1, "", "Payment Not Recieved", "Retry", "")
                            }
                        } catch (e: Exception) {
                            st!!.toast("confirmInvoiceRespone: " + e.message)
                            dialog2.dismiss()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        st!!.toast("confirmInvoiceRespone: " + e.message)
                        dialog2.dismiss()
                    }
                }

                override fun onFailure(call: Call<Any?>, t: Throwable) {
                    Log.e("TAG", "onResponse: " + t.message.toString())
                    st!!.toast("confirmInvoiceRespone: " + t.message.toString())
                    dialog2.dismiss()
                }
            })
        } catch (e: Exception) {
            dialog2.dismiss()
            e.printStackTrace()
        }
    }

    private fun confirmRegistrationInvoice(query: String) {
        val confirmRegistartionInvice = ConfirmRegistartionInvice(this@Registration)
        if (Build.VERSION.SDK_INT >= 11 /*HONEYCOMB*/) {
            confirmRegistartionInvice.executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR,
                *arrayOf<String>((query))
            )
        } else {
            confirmRegistartionInvice.execute(*arrayOf<String>((query)))
        }
    }

    private inner class ConfirmRegistartionInvice(private val parent: Activity) :
        AsyncTask<String?, Int?, String>() {
        var dialog2: ProgressDialog?

        init {
            // record the calling activity, to use in showing/hiding dialogs
            dialog2 = ProgressDialog(parent)
            dialog2!!.setMessage("Confirming...")
        }

        override fun onPreExecute() {
            dialog2!!.show()
            dialog2!!.setCancelable(false)
            dialog2!!.setCanceledOnTouchOutside(false)
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected override fun doInBackground(vararg urls: String?): String? {
            // called on the background thread
            var response = ""
            val query = urls[0]
            try {
                NetworkManager.instance!!.sendToServer(query)
            } catch (e: Exception) {
                Log.e(Registration.TAG, e.localizedMessage)
            }
            try {
                // Try now
                response = NetworkManager.instance.recvFromServer()
            } catch (e: Exception) {
                Log.e(Registration.TAG, e.localizedMessage)
            }
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
            confirmInvoiceResponse(result)
            try {
                if (dialog2 != null && dialog2!!.isShowing) {
                    dialog2!!.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


    }

    override fun excatFigure(value: Double): String? {
        val d = BigDecimal(value.toString())
        return d.toPlainString()
    }

    private fun addInTransactionLog(
        amount_usd: Double,
        amount_btc: Double,
        transactionTimeStamp: String,
        client_id2: String
    ) {
        val amountUsd = amount_usd.toString()
        val amountBtc = amount_btc.toString()
        val label = TRANSACTION_LABEL
        //String clientId=client_id2;  //clientData.getClient_id()
        val clientId = client_id
        val merchantID = merchantData!!.merchant_name
        val call = ApiClient.getRetrofit(this)!!.create(
            ApiInterface::class.java
        ).instance_transction_add(
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
                            var transactionInfo: TransactionInfo? = TransactionInfo()
                            transactionInfo = transactionResp.transactionInfo
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

    private fun goAlertDialogwithOneBTn(
        i: Int,
        alertTitleMessage: String,
        alertMessage: String?,
        alertBtn1Message: String,
        alertBtn2Message: String
    ) {
        val goAlertDialogwithOneBTnDialog: Dialog
        goAlertDialogwithOneBTnDialog = Dialog(this@Registration)
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

    override fun onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction()
        stopHandler() //stop first and then start
        startHandler()
    }

    fun stopHandler() {
        handler!!.removeCallbacks(r!!)
    }

    fun startHandler() {
        handler!!.postDelayed(r!!, (5 * 60 * 1000).toLong()) //for 5 minutes
    }

    private inner class ScreenReceiver : BroadcastReceiver() {
        init {
            // register receiver that handles screen on and screen off logic
            val filter = IntentFilter()
            filter.addAction(Intent.ACTION_SCREEN_ON)
            filter.addAction(Intent.ACTION_SCREEN_OFF)
            registerReceiver(this, filter)
        }

        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_OFF) {
                sp!!.clearAll()
                openActivity(MerchantLink::class.java)
                finishAffinity()
            } else if (intent.action == Intent.ACTION_SCREEN_ON) {
                //isScreenOff = false;
            }
        }
    }

    companion object {
        var TAG = "tag"
        var isGamma = false
        private var INSTANT_NORMAL = -1
        private var bitCoinValue = 0.0
        private const val ID_CAMERA_REQ = 10
        private const val ID_GALLERY_REQ = 11
        private const val CLIENT_CAMERA_REQ = 12
        private const val CLIENT_GALLERY_REQ = 13
        var timeRand: String? = null
        var client_id: String? = null
        var client_image_title: String? = null
        var card_image_title: String? = null
        private var client_image_bitmap: Bitmap? = null
        private var card_image_bitmap: Bitmap? = null
        fun round(value: Double, places: Int): Double {
            var value = value
            require(places >= 0)
            val factor = Math.pow(10.0, places.toDouble()).toLong()
            value = value * factor
            val tmp = Math.round(value)
            return tmp.toDouble() / factor
        }
    }
}