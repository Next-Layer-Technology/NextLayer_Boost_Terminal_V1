package com.sis.clighteningboost.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.sis.clighteningboost.Api.ApiClient;
import com.sis.clighteningboost.Api.ApiInterface;
import com.sis.clighteningboost.Api.ApiInterfaceForNodes;
import com.sis.clighteningboost.BitCoinPojo.CurrentAllRate;
import com.sis.clighteningboost.Interface.RPCResponse;
import com.sis.clighteningboost.Models.ARoutingAPIAuthResponse;
import com.sis.clighteningboost.Models.DecodeBolt112WithExecuteResponse;
import com.sis.clighteningboost.Models.REST.BaseIDRes;
import com.sis.clighteningboost.Models.REST.ClientData;
import com.sis.clighteningboost.Models.REST.FundingNode;
import com.sis.clighteningboost.Models.REST.FundingNodeListResp;
import com.sis.clighteningboost.Models.REST.MerchantData;
import com.sis.clighteningboost.Models.REST.MerchantLoginResp;
import com.sis.clighteningboost.Models.REST.RegistrationClientResp;
import com.sis.clighteningboost.Models.REST.TransactionInfo;
import com.sis.clighteningboost.Models.REST.TransactionResp;
import com.sis.clighteningboost.Models.RPC.NodeLineInfo;
import com.sis.clighteningboost.R;
import com.sis.clighteningboost.RPC.CreateInvoice;
import com.sis.clighteningboost.Utills.GlobalState;
import com.sis.clighteningboost.RPC.Invoice;
import com.sis.clighteningboost.RPC.NetworkManager;
import com.sis.clighteningboost.RPC.Tax;
import com.sis.clighteningboost.Utills.AppConstants;
import com.sis.clighteningboost.Utills.JavaMailAPI;
import com.sis.clighteningboost.Utills.SharedPreference;
import com.sis.clighteningboost.Utills.StaticClass;
import com.sis.clighteningboost.Utills.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import java.util.Map;
import java.util.Objects;
import java.util.Random;
import de.hdodenhof.circleimageview.CircleImageView;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class Registration extends BaseActivity {
    public static String TAG = "tag";
    Button btn_next, btn_back;
    LinearLayout registration_layout_step_1, registration_layout_step_2, registration_layout_step_3, registration_layout_step_4, registration_layout_step_5;
    RelativeLayout is_gamma_user_check;
    int selectedLay = 0;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView tv_step_text;
    static boolean isGamma = false;
    boolean isUserValid=false;
    ImageView iv_gamma_user, show_id_picture, show_client_picture;
    LinearLayout select_picture_of_id, select_client_picture;
    TextView show_id_picture_text, show_client_picture_text;
    SharedPreference sp;
    RelativeLayout select_instant_option, select_normal_option;
    EditText et_client_date;
    StaticClass st;
    private static int INSTANT_NORMAL = -1;
    private static double bitCoinValue = 0.0;
    private static final int ID_CAMERA_REQ = 10;
    private static final int ID_GALLERY_REQ = 11;
    private static final int CLIENT_CAMERA_REQ = 12;
    private static final int CLIENT_GALLERY_REQ = 13;
    private MerchantData merchantData;
    CircleImageView iv_instant_selected, iv_normal_selected;
    static String timeRand, client_id, client_image_title, card_image_title;
    ProgressDialog progressDialog;
    FundingNode fundingNodeTemp;
    private String TRANSACTION_LABEL="";
    private String TRANSACTION_TIMESTAMP="";
    private double TOTAL_AMOUNT_BTC=0;
    private double TOTAL_AMOUNT_USD=0;
    private  double USD_TO_BTC_RATE=0;

    //registration call
    EditText et_client_name, et_client_national_id,et_client_user_id ,et_client_address, et_client_email;
    // showDialog
    int dialogLayout = 0;
    final ArrayList<LinearLayout> layouts = new ArrayList<>();

    ImageView instant_pay_back_icon;
    ImageView qr_scan_code_image;
    RPCResponse rpcResponse;

    private String authLevel1;
    private String authLevel2;

    Handler handler;
    Runnable r;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
        TRANSACTION_LABEL="test";
        USD_TO_BTC_RATE=15441;
        isGamma = false;
        st = new StaticClass(this);
        sp = new SharedPreference(this, "local_data");
        merchantData= GlobalState.getInstance().getMainMerchantData();
        GlobalState.getInstance().setLattitude(sp.getStringValue("latitude"));
        GlobalState.getInstance().setLongitude(sp.getStringValue("longitude"));
        Log.e("MainPe:",String.valueOf(isGamma));
        timeRand = System.currentTimeMillis() + "" + (new Random().nextInt(1000) + 1);
        client_id = (isGamma ? "NC" : "C") + timeRand;
        Log.e("MainPe1:",String.valueOf(isGamma));
        Log.e("MainPe1:",client_id);
        client_image_title = "ClientIMG" + timeRand;
        card_image_title = "CardIMG" + timeRand;
        card_image_bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.bg_small_btn));
        client_image_bitmap = drawableToBitmap(getResources().getDrawable(R.drawable.bg_small_btn));
        btn_next = findViewById(R.id.btn_next);
        btn_back = findViewById(R.id.btn_back);
        tv_step_text = findViewById(R.id.tv_step_text);
        iv_instant_selected = findViewById(R.id.iv_instant_selected);
        iv_normal_selected = findViewById(R.id.iv_normal_selected);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering...");
        progressDialog.setCancelable(false);
        //input fields
        handler = new Handler();
        r = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                sp.clearAll();
                openActivity(MerchantLink.class);
                finishAffinity();
            }
        };
        startHandler();
        new ScreenReceiver();

        et_client_name = findViewById(R.id.et_client_name);
        et_client_national_id = findViewById(R.id.et_client_national_id);
        et_client_user_id=findViewById(R.id.et_client_user_id);
        et_client_address = findViewById(R.id.et_client_address);
        et_client_email = findViewById(R.id.et_client_email);
        et_client_date = findViewById(R.id.et_client_date);
        et_client_date.setInputType(InputType.TYPE_NULL);
        String e=et_client_email.getText().toString().trim();
        et_client_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(et_client_date);
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                DatePickerDialog picker = new DatePickerDialog(Registration.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Log.e("Date2:::",month+"-"+day);
                                String m=String.valueOf(month+1);
                                String d=String.valueOf(day);
                                if(month+1<10){m="0"+m;}
                                if(day<10){d="0"+day;}
                                et_client_date.setText(m + "-" + d + "-" + year);
                            }
                        }, year, month, day);
                picker.getDatePicker().setMaxDate(System.currentTimeMillis());
                picker.show();
            }
        });
        et_client_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    et_client_date.performClick();
                }
            }
        });
        registration_layout_step_1 = findViewById(R.id.registration_layout_step_1);
        registration_layout_step_2 = findViewById(R.id.registration_layout_step_2);
        registration_layout_step_3 = findViewById(R.id.registration_layout_step_3);
        registration_layout_step_4 = findViewById(R.id.registration_layout_step_4);
        registration_layout_step_5 = findViewById(R.id.registration_layout_step_5);
        is_gamma_user_check = findViewById(R.id.is_gamma_user_check);
        select_picture_of_id = findViewById(R.id.select_picture_of_id);
        iv_gamma_user = findViewById(R.id.iv_gamma_user);
        show_id_picture = findViewById(R.id.show_id_picture);
        show_id_picture_text = findViewById(R.id.show_id_picture_text);
        select_client_picture = findViewById(R.id.select_client_picture);
        show_client_picture_text = findViewById(R.id.show_client_picture_text);
        show_client_picture = findViewById(R.id.show_client_picture);
        select_normal_option = findViewById(R.id.select_normal_option);
        select_instant_option = findViewById(R.id.select_instant_option);
        show_id_picture.setVisibility(View.GONE);
        show_id_picture_text.setVisibility(View.VISIBLE);
        show_client_picture.setVisibility(View.GONE);
        show_client_picture_text.setVisibility(View.VISIBLE);
        iv_gamma_user.setVisibility(View.GONE);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(view);
                nextLay();

            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(view);
                previousLay();
            }
        });
        nextLay();
        select_instant_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(select_instant_option);

                if (fundingNodeTemp != null) {
                    connectWithThorAndLoginAPi(fundingNodeTemp.getIp(),fundingNodeTemp.getPort(),fundingNodeTemp.getUsername(),fundingNodeTemp.getPassword());
                   //connectWithThorAndLogin(fundingNodeTemp.getIp(),fundingNodeTemp.getPort(),fundingNodeTemp.getUsername(),fundingNodeTemp.getPassword());
                   // registration(true);
                    //doInBackgroundMethod( Utils.CONNECT_TO_NETWORK,Utils.CONNECT_TO_NETWORK_MESSAGE,new String[]{fundingNodeTemp.getIp(), fundingNodeTemp.getPort()});
                } else {
                    getFundingNodeInfor();
                    st.toast("Try again");
                }
            }
        });
        select_normal_option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(select_normal_option);
                INSTANT_NORMAL = 1;
                iv_normal_selected.setVisibility(INSTANT_NORMAL == 1 ? View.VISIBLE : View.GONE);
                iv_instant_selected.setVisibility(INSTANT_NORMAL == 2 ? View.VISIBLE : View.GONE);
            }
        });
        is_gamma_user_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(is_gamma_user_check);
                Log.e("Before:",String.valueOf(isGamma));
                Log.e("Before:",client_id);
                isGamma = !isGamma;
                timeRand = System.currentTimeMillis() + "" + (new Random().nextInt(1000) + 1);
                client_id = (isGamma ? "NC" : "C") + timeRand;
                Log.e("After:",String.valueOf(isGamma));
                Log.e("After:",client_id);
                iv_gamma_user.setVisibility(isGamma ? View.VISIBLE : View.GONE);
                if (isGamma){
                    et_client_user_id.setVisibility(View.VISIBLE);
                }else {
                    et_client_user_id.setVisibility(View.GONE);
                }
            }
        });
        select_picture_of_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(is_gamma_user_check);
                imageOptions(0);
            }
        });


        select_client_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(is_gamma_user_check);
                imageOptions(1);
            }
        });

        checkPermissions();
        getFundingNodeInfor();
        getBitCoinValue();
        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                //call function
                getBitCoinValue();
                ha.postDelayed(this, AppConstants.getLatestRateDelayTime);
            }
        }, AppConstants.getLatestRateDelayTime);

        initRPCResponse();
      //addInTransactionLog(90,0.00034,"klsjkljsd","Blackvirus1010111211gfg");
    }
    private void imageOptions(final int index) {
        final CharSequence items[] = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Image From");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, index == 0 ? ID_CAMERA_REQ : CLIENT_CAMERA_REQ);
                }
                if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select Image"), index == 0 ? ID_GALLERY_REQ : CLIENT_GALLERY_REQ);
                }
                dialogInterface.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == ID_CAMERA_REQ) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                show_id_picture.setImageBitmap(bitmap);
                show_id_picture.setVisibility(View.VISIBLE);
                show_id_picture_text.setVisibility(View.GONE);
                card_image_bitmap = bitmap;
            } else if (requestCode == ID_GALLERY_REQ) {
                Uri uri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    card_image_bitmap = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                show_id_picture.setImageURI(uri);
                show_id_picture.setVisibility(View.VISIBLE);
                show_id_picture_text.setVisibility(View.GONE);
            } else if (requestCode == CLIENT_CAMERA_REQ) {
                Bundle bundle = data.getExtras();
                Bitmap bitmap = (Bitmap) bundle.get("data");
                show_client_picture.setImageBitmap(bitmap);
                show_client_picture.setVisibility(View.VISIBLE);
                show_client_picture_text.setVisibility(View.GONE);
                client_image_bitmap = bitmap;
            } else if (requestCode == CLIENT_GALLERY_REQ) {
                Uri uri = data.getData();
                show_client_picture.setImageURI(uri);
                show_client_picture.setVisibility(View.VISIBLE);
                show_client_picture_text.setVisibility(View.GONE);
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                    client_image_bitmap = bitmap;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } else {
            show_id_picture.setVisibility(View.GONE);
            show_id_picture_text.setVisibility(View.VISIBLE);
        }

    }
    private static Bitmap client_image_bitmap = null, card_image_bitmap = null;
    private void nextLay() {
        if (verifySteps(selectedLay) == 0) {
            return;
        }
        if (selectedLay == 4) {
            progressDialog.show();
            normal_registration(false);
        } else {
            selectedLay++;
            contorlLay(selectedLay);
        }
    }
    private void previousLay() {
        if (selectedLay == 5) {
            finish();
        } else {
            selectedLay--;
            contorlLay(selectedLay);
        }
    }
    public void contorlLay(int i) {
//        i = 4;
        if (i == 0) {
            super.onBackPressed();
        } else if (i >= 6) {
            finish();
            openActivity(MerchantBoostTerminal.class);
            selectedLay = 5;
            return;
        }
        registration_layout_step_1.setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        registration_layout_step_2.setVisibility(i == 2 ? View.VISIBLE : View.GONE);
        registration_layout_step_3.setVisibility(i == 3 ? View.VISIBLE : View.GONE);
        registration_layout_step_4.setVisibility(i == 4 ? View.VISIBLE : View.GONE);
        registration_layout_step_5.setVisibility(i == 5 ? View.VISIBLE : View.GONE);
        tv_step_text.setText("Step " + i + " Of 5");
        btn_back.setVisibility(i <= 1 ? View.GONE : View.VISIBLE);
        if (i == 5) {
            tv_step_text.setText("Successful");
        }
//        if(i==5){registration();}
    }
    private String imageToBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }
    @Override
    public void onBackPressed() {
        previousLay();
    }
    String name, nd, address, email, date,user_id="";
    //steps verification
    private int verifySteps(int i) {
        int i_return = 1;
        // verify step 1
        if (i == 1) {
            if (isGamma){
                name = et_client_name.getText().toString();
                nd = et_client_national_id.getText().toString();
                user_id=et_client_user_id.getText().toString();
                address = et_client_address.getText().toString();
                email = et_client_email.getText().toString();
                date = et_client_date.getText().toString();
                if (name.length() == 0) {
                    st.toast("Please Enter Name");
                    i_return = 0;
                } else if (user_id.length() == 0) {
                    st.toast("Base ID is unavailable");
                    i_return = 0;
                } else if (email.length() == 0) {
                    st.toast("Please Enter Email");
                    i_return = 0;
                } else if (address.length() == 0) {
                    st.toast("Please Enter Address");
                    i_return = 0;
                } else if (date.length() == 0) {
                    st.toast("Please Enter Date");
                    i_return = 0;
                }else if(!email.isEmpty()){
                    if(!isValidEmail(email)){
                        i_return=0;
                        st.toast("Invalid Email");
                        et_client_email.setText("");
                    }
                    else if (user_id.length() == 0) {
                        st.toast("Please Enter Date");
                        i_return = 0;
                    }
                    else if (!user_id.isEmpty()){
                        try {
                            checkBaseID(user_id);
                            if (!isUserValid){

                                i_return = 0;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }else {
                name = et_client_name.getText().toString();
                nd = et_client_national_id.getText().toString();
                user_id=et_client_user_id.getText().toString();
                address = et_client_address.getText().toString();
                email = et_client_email.getText().toString();
                date = et_client_date.getText().toString();
                if (name.length() == 0) {
                    st.toast("Please Enter Name");
                    i_return = 0;
                }/* else if (user_id.length() == 0) {
                    st.toast("Please Enter Email");
                    i_return = 0;
                }*/ else if (email.length() == 0) {
                    st.toast("Please Enter Email");
                    i_return = 0;
                } else if (address.length() == 0) {
                    st.toast("Please Enter Address");
                    i_return = 0;
                } else if (date.length() == 0) {
                    st.toast("Please Enter Date");
                    i_return = 0;
                }else if(!email.isEmpty()){
                    if(!isValidEmail(email)){
                        i_return=0;
                        st.toast("Invalid Email");
                        et_client_email.setText("");
                    }
                }

            }

        }
        // step 2
        else if (i == 2 && card_image_bitmap == null) {
            st.toast("Please Select Card Picture");
            i_return = 0;
        }
        //step 3
        else if (i == 3 && client_image_bitmap == null) {
            st.toast("Please Select Client Picture");
            i_return = 0;
        } else if (i == 4 && INSTANT_NORMAL == -1) {
            st.toast("Please Select At Least 1 Subscription");
            i_return = 0;
        }
        //
        return i_return;
    }
    private void checkBaseID(final String  baseId) throws JSONException {
        progressDialog.show();
        String accessToken=sp.getStringValue("accessToken");
        String token="Bearer"+" "+accessToken;
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("base_id", baseId);
        Call<BaseIDRes> call = ApiClient.getRetrofit(this).create(ApiInterface.class).merchant_check_baseID(token,paramObject);
        //Call<MerchantLoginResp> call = ApiClient.getRetrofit().create(ApiInterface.class).merchant_Loging(id,password);
        call.enqueue(new Callback<BaseIDRes>() {
            @Override
            public void onResponse(Call<BaseIDRes> call, Response<BaseIDRes> response) {
                progressDialog.dismiss();
                if(response.body()!=null){
                    if (response.body().getMessage().contains("base id is available")){
                        st.toast("Success");
                        isUserValid=true;
                       // verifySteps(1);
                        progressDialog.dismiss();
                    }else {
                        st.toast("Base ID is unavailable. Please try again.");
                        isUserValid=false;
                        progressDialog.dismiss();
                    }
                }
                else {
                    // st.toast("Please Enter Another UserId This Userid Is Not Available");
                    st.toast("Base ID is unavailable. Please try again.");
                    progressDialog.dismiss();
                    isUserValid=false;
                }
            }
            @Override
            public void onFailure(Call<BaseIDRes> call, Throwable t) {
                isUserValid=false;
                progressDialog.dismiss();
                st.toast(t.getMessage().toString());
            }
        });

    }
    private void checkPermissions() {
        statusCheck();
    }
    public void statusCheck() {
        String perm[] = new String[6];
        ArrayList<String> list = new ArrayList<>();
        if (checkAllowance(Manifest.permission.ACCESS_NETWORK_STATE)) {
            perm[0] = Manifest.permission.ACCESS_NETWORK_STATE;
            list.add(Manifest.permission.ACCESS_NETWORK_STATE);
        }
        if (checkAllowance(Manifest.permission.CAMERA)) {
            perm[1] = Manifest.permission.CAMERA;
            list.add(Manifest.permission.CAMERA);
        }
        if (checkAllowance(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            perm[2] = Manifest.permission.READ_EXTERNAL_STORAGE;
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (checkAllowance(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            perm[3] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
            list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (checkAllowance(Manifest.permission.MEDIA_CONTENT_CONTROL)) {
            perm[4] = Manifest.permission.MEDIA_CONTENT_CONTROL;
            list.add(Manifest.permission.MEDIA_CONTENT_CONTROL);
        }
        if (checkAllowance(Manifest.permission.INTERNET)) {
            perm[5] = Manifest.permission.INTERNET;
            list.add(Manifest.permission.INTERNET);
        }
        String rp[] = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rp[i] = list.get(i);
        }
        if (list.size() > 0) {
            ActivityCompat.requestPermissions(this, rp, 1);
        }
    }
    private boolean checkAllowance(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1)
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
    }
    // Extra Dialog
    private void showInstantDialog(String ip, String port, String username, String password) {

        final Dialog showInstantPayDialog;
        showInstantPayDialog=new Dialog(Registration.this);
        showInstantPayDialog.setContentView(R.layout.instant_pay_layout_1);
        Objects.requireNonNull(showInstantPayDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        showInstantPayDialog.setCancelable(false);

        Button btn_pay_with_lightning = showInstantPayDialog.findViewById(R.id.btn_pay_with_lightning);
        instant_pay_back_icon = showInstantPayDialog.findViewById(R.id.instant_pay_back_icon);
        final TextView fee_from_db = showInstantPayDialog.findViewById(R.id.fee_from_db);
        double perUsdBtc=bitCoinValue;
        perUsdBtc=1/bitCoinValue;
        double totalfee=perUsdBtc * fundingNodeTemp.getRegistration_fees();
        totalfee=round(totalfee,9);
        fee_from_db.setText( excatFigure(totalfee)+ " BTC / $" + String.format("%.2f",round(fundingNodeTemp.getRegistration_fees(),2)) + "USD");
        final LinearLayout layout1 = showInstantPayDialog.findViewById(R.id.layout1);
        final LinearLayout layout2 = showInstantPayDialog.findViewById(R.id.layout2);
        final LinearLayout layout3 = showInstantPayDialog.findViewById(R.id.layout3);
        final LinearLayout layout4 = showInstantPayDialog.findViewById(R.id.layout4);
        qr_scan_code_image = showInstantPayDialog.findViewById(R.id.qr_scan_code_image);
        final EditText static_description = showInstantPayDialog.findViewById(R.id.static_description);
        final EditText static_label = showInstantPayDialog.findViewById(R.id.static_label);
        final EditText static_amount_in_satoshi = showInstantPayDialog.findViewById(R.id.static_amount_in_satoshi);
        static_description.setInputType(InputType.TYPE_NULL);
        static_label.setInputType(InputType.TYPE_NULL);
        static_amount_in_satoshi.setInputType(InputType.TYPE_NULL);
        double invoiceRate= fundingNodeTemp.getRegistration_fees() * perUsdBtc;
        double satoshiValue = invoiceRate * 100000000;
        final double satoshiValuemSat = satoshiValue * 1000;
        double dmSatoshi=0;
        double dSatoshi=0;
        dSatoshi= invoiceRate*AppConstants.btcToSathosi;
        dmSatoshi=dSatoshi*AppConstants.satoshiToMSathosi;
        NumberFormat formatter = new DecimalFormat("#0");
        String rMSatoshi=formatter.format(dmSatoshi);
        layouts.clear();
        layouts.add(layout1);
        layouts.add(layout2);
        layouts.add(layout3);
        layouts.add(layout4);
        controlDialogLayouts(1, layouts, instant_pay_back_icon);
        instant_pay_back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogLayout == 1) {
                    showInstantPayDialog.dismiss();
                } else {
                    dialogLayout--;
                    controlDialogLayouts(dialogLayout, layouts, instant_pay_back_icon);
                }
            }
        });
        final String amount =rMSatoshi;
        final String description = sp.getStringValue("merchant_id");
        final String label = "instant_register" + System.currentTimeMillis();
        static_description.setText(description);
        static_label.setText(label);
        static_amount_in_satoshi.setText(amount);
        btn_pay_with_lightning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoverEffect(view);
                controlDialogLayouts(2, layouts, instant_pay_back_icon);
            }
        });
        showInstantPayDialog.findViewById(R.id.btn_create_invoice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String query = "rpc-cmd,cli-node," + GlobalState.getInstance().getLattitude() + "_" + GlobalState.getInstance().getLongitude() + "," + System.currentTimeMillis() / 1000 + ",[ invoice " + amount + " " + label + " " + description + " ]";
                 String query = "invoice " + amount + " " + label + " " + description;
                 re_ConfirmProfile(query,ip,port,username,password);

               //doInBackgroundMethod(Utils.CREATE_INVOICE_RESPONSE, Utils.CREATE_INVOICE_RESPONSE_MESSAGE,new String[]{query});
            }
        });
        showInstantPayDialog.findViewById(R.id.btn_confirm_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //String query = "rpc-cmd,cli-node," + GlobalState.getInstance().getLattitude() + "_" + GlobalState.getInstance().getLongitude() + "," + System.currentTimeMillis() / 1000 + ",[ listinvoices " + label + " ]";
                String query = "listinvoices " + label;
                confirmRegistrationInvoice2(query,ip,port,username,password);
                //confirmRegistrationInvoice(query);
                //doInBackgroundMethod( Utils.CONFIRM_INVOICE_RESPONSE, Utils.CONFIRM_INVOICE_RESPONSE_MESSAGE,new String[]{query});
            }
        });
        showInstantPayDialog.findViewById(R.id.btn_skip_invoice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                INSTANT_NORMAL = 2;
                nextLay();
                showInstantPayDialog.dismiss();
            }
        });
        showInstantPayDialog.show();

    }
    private void re_ConfirmProfile(String query,String ip, String port, String username, String password) {
        final Dialog re_ConfirmProfileDialog;
        re_ConfirmProfileDialog=new Dialog(Registration.this);
        re_ConfirmProfileDialog.setContentView(R.layout.reconfier_layout_two);
        re_ConfirmProfileDialog.setCancelable(false);
        Objects.requireNonNull(re_ConfirmProfileDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        re_ConfirmProfileDialog.setCancelable(false);
        final TextView nametxt=re_ConfirmProfileDialog.findViewById(R.id.name);
        final TextView emailtxt=re_ConfirmProfileDialog.findViewById(R.id.email);
        final TextView addresstxt=re_ConfirmProfileDialog.findViewById(R.id.address);
        final TextView nictxt=re_ConfirmProfileDialog.findViewById(R.id.nic);
        final TextView dobtxt=re_ConfirmProfileDialog.findViewById(R.id.dob);
        final TextView gammausertxt=re_ConfirmProfileDialog.findViewById(R.id.gammauser);
        final ImageView backImagetxt=re_ConfirmProfileDialog.findViewById(R.id.backimage);
        final ImageView profileImagetxt=re_ConfirmProfileDialog.findViewById(R.id.profileimage);
        final ImageView nicImagetxt=re_ConfirmProfileDialog.findViewById(R.id.nicimage);
        final Button confirmtxt=re_ConfirmProfileDialog.findViewById(R.id.confirm);
        Bitmap clientImage=client_image_bitmap;
        Bitmap nicImage2=card_image_bitmap;
        String clName2=name;
        String clId=client_id;
        String nidd=nd;
        String add=address;
        String dbb=date;
        String eml=email;
        String regstrdt=getCurrentDateInFormamt();
        String isGam=isGamma ? "Yes":"No";
        nametxt.setText(clName2);
        nictxt.setText(nidd);
        emailtxt.setText(eml);
        addresstxt.setText(add);
        dobtxt.setText(dbb);
        gammausertxt.setText(isGam);
        profileImagetxt.setImageBitmap(clientImage);
        nicImagetxt.setImageBitmap(nicImage2);
        backImagetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                re_ConfirmProfileDialog.dismiss();
            }
        });
        confirmtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                re_ConfirmProfileDialog.dismiss();
                reConfirmRegistrationInvoice2(query,ip,port,username,password);
                //createRegistartionInvoice1(query);
            }
        });
        re_ConfirmProfileDialog.show();


    }
    private void initRPCResponse() {
        rpcResponse = new RPCResponse() {
            @Override
            public void getRPCResponse(String response, int responseType) {
                switch (responseType) {
                    case Utils.CREATE_INVOICE_RESPONSE: createInvoiceResponse(response);break;
                    case Utils.CONFIRM_INVOICE_RESPONSE: confirmInvoiceResponse(response);break;
                    case Utils.CONNECT_TO_NETWORK: connectToNetworkResponse(response);break;
                    case Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER: validateUserResponse(response); break;
                    case Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE: getTaxInfoFromServer(response); break;
                    default: break;
                }
            }
        };
    }
    private void connectToNetworkResponse(String response){
        boolean resp = response!=null?Boolean.parseBoolean(response):false;

        boolean resultstatus = toBooleanDefaultIfNull(resp);

        if (resultstatus) {
            ifServerConnectForCheckOut();
        } else {
            goAlertDialogwithOneBTn(1,"","Server URL Is Not Exist","Ok","");
        }

    }
    private void validateUserResponse(String response){

        int result = Integer.parseInt(response);

        if (result == Utils.CHECKOUT_USER) {
            ifValidateCheckoutConnect();
        } else {
            st.toast("Not Connect with CheckOut");
        }
    }
    private void getTaxInfoFromServer(String response){

        String result = response;
        String[] resaray = response.split(",");
        if (resaray[0].contains("resp")) {
            if (resaray[1].equals("ok")) {
                String[] splitresponse = response.split(",");
                String jsonresponse = "";
                Tax temp = new Tax();
                temp.setTaxpercent(Double.parseDouble(resaray[4]));
                temp.setTaxInUSD(1.0);
                temp.setTaxInBTC(0.00001);
                GlobalState.getInstance().setTax(temp);
                //UncommentWhenCOmplete
                //showInstantDialog();
//                getTaxCheckout();
            } else {
                Tax temp = new Tax();
                temp.setTaxpercent(1.0);
                temp.setTaxInBTC(1.000);
                temp.setTaxInUSD(1.0);
                GlobalState.getInstance().setTax(temp);
                st.toast("Get Tax  Failed");
            }
        }

    }
    private void createInvoiceResponse(String response) {

        try {
            if (response.contains("bolt11")) {
                String[] split = response.split(",");
                String invoiceReponse = "";
                for (int i = 4; i < split.length; i++) {
                    invoiceReponse += "," + split[i];
                }
                invoiceReponse = invoiceReponse.substring(1);

                String fInvoiceResponse = invoiceReponse.replace("[", "");
                fInvoiceResponse = fInvoiceResponse.replace("]", "");
                CreateInvoice temInvoice = parseJSONForCreatInvocie(fInvoiceResponse);

                if (temInvoice != null) {
                    if (temInvoice.getBolt11() != null) {
//                        transactionID++;
                        String temHax = temInvoice.getBolt11();
                        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                        try {
                            BitMatrix bitMatrix = multiFormatWriter.encode(temHax, BarcodeFormat.QR_CODE, 600, 600);
                            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                            progressDialog.dismiss();
                            qr_scan_code_image.setImageBitmap(bitmap);
                            controlDialogLayouts(3, layouts, instant_pay_back_icon);
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
            else {
                goAlertDialogwithOneBTn(1,"","Please Check Your Funding/Receiving Node!","Retry","");
                st.toast("Not contain bolt11");
            }

        } catch (Exception e) {
            st.toast("createInvoiceResponse: " + e.getMessage());
        }

    }
    private void confirmInvoiceResponse(String response) {
        try {
            if (response.contains("bolt11")) {

                String[] split = response.split(",");
                String invoiceReponse = "";
                for (int i = 4; i < split.length; i++) {
                    invoiceReponse += "," + split[i];
                }
                invoiceReponse = invoiceReponse.substring(1);

                String fInvoiceResponse = invoiceReponse.replace("[", "");
                fInvoiceResponse = fInvoiceResponse.replace("]", "");
                fInvoiceResponse = fInvoiceResponse.substring(16);
                fInvoiceResponse = fInvoiceResponse.substring(0, fInvoiceResponse.length() - 2);

                Invoice getInvoicefromServer = parseJSONForConfirmPayment(fInvoiceResponse);

                if (getInvoicefromServer != null) {
                    if (getInvoicefromServer.getStatus().equals("paid")) {

                        TRANSACTION_TIMESTAMP=String.valueOf(getInvoicefromServer.getPaid_at());
                        TRANSACTION_LABEL=getInvoicefromServer.getLabel();
                        TOTAL_AMOUNT_BTC= getInvoicefromServer.getMsatoshi();
                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.btcToSathosi;
                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.satoshiToMSathosi;
                        TOTAL_AMOUNT_USD=getUsdFromBtc(TOTAL_AMOUNT_BTC);
                        USD_TO_BTC_RATE=TOTAL_AMOUNT_USD/TOTAL_AMOUNT_BTC;
                        st.toast("paid");
                        instant_registration(true);
                    } else {
                        goAlertDialogwithOneBTn(1,"","Payment Not Recieved","Retry","");
                    }
                }

            } else {
                goAlertDialogwithOneBTn(1,"","Payment Not Recieved","Retry","");
            }
        } catch (Exception e) {
            st.toast("confirmInvoiceRespone: " + e.getMessage());
        }
    }
    private void normal_registration(boolean isActive) {
        Bitmap clientImage=client_image_bitmap;
        Bitmap nicImage=card_image_bitmap;
        String clName=name;
        String clId=client_id;
        String nidd=nd;
        String add=address;
        String dbb=date;
        String eml=email;
        String regstrdt=getCurrentDateInFormamt();
        String isGam=isGamma ? "1":"0";
        String isAct=isActive ? "1" : "0";
        String baseId=user_id;
        String clientImageTitle=client_image_title;
        String nicImageTitle=card_image_title;
        MultipartBody.Part client_image_id=null;
        MultipartBody.Part card_image_id=null;
        File clientImageFil= null;
        try {
            clientImageFil = savebitmap(clientImage,getUnixTimeStamp());
        } catch (IOException e) {
            e.printStackTrace();
        }
        File cardImageFile= null;
        try {
            cardImageFile = savebitmap(nicImage,getUnixTimeStamp()+1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client_image_id = MultipartBody.Part.createFormData("client_image_id", clientImageFil.getName(), RequestBody.create(MediaType.parse("image/*"), clientImageFil));
        card_image_id= MultipartBody.Part.createFormData("card_image_id", cardImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), cardImageFile));
        RequestBody client_name2 = RequestBody.create(MediaType.parse("text/plain"), clName);
        RequestBody client_id2 = RequestBody.create(MediaType.parse("text/plain"), clId);
        RequestBody national_id2 = RequestBody.create(MediaType.parse("text/plain"), nidd );
        RequestBody address2 = RequestBody.create(MediaType.parse("text/plain"), add);
        RequestBody dob2 = RequestBody.create(MediaType.parse("text/plain"),dbb);
        RequestBody is_gamma_user2 = RequestBody.create(MediaType.parse("text/plain"),isGam);
        RequestBody registered_at2 = RequestBody.create(MediaType.parse("text/plain"),regstrdt);
        RequestBody is_active2 = RequestBody.create(MediaType.parse("text/plain"),isAct);
        RequestBody email2 = RequestBody.create(MediaType.parse("text/plain"), eml);
        RequestBody maxboost_limit2 = RequestBody.create(MediaType.parse("text/plain"),"25");
        RequestBody client_type = RequestBody.create(MediaType.parse("text/plain"),"normal");

        if(merchantData == null){
            merchantData = GlobalState.getInstance().getMainMerchantData();
        }

        if(merchantData == null){
            merchantData = GlobalState.getGlobalState().getMainMerchantData();
        }

        RequestBody merchant_id = RequestBody.create(MediaType.parse("text/plain"),merchantData == null? "":merchantData.getMerchant_name());
        RequestBody base_id = RequestBody.create(MediaType.parse("text/plain"),baseId);
        RequestBody per_boost_limit = RequestBody.create(MediaType.parse("text/plain"),merchantData == null? "":merchantData.getMaxboost_limit());
        RequestBody max_daily_boost = RequestBody.create(MediaType.parse("text/plain"),merchantData == null? "":merchantData.getMerchant_maxboost());

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();
        Call<RegistrationClientResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).client_Registration(client_type,client_name2,client_id2,merchant_id,national_id2,address2,dob2,is_gamma_user2,registered_at2,is_active2,client_image_id,card_image_id,email2,maxboost_limit2,base_id,per_boost_limit,max_daily_boost);
         call.enqueue(new Callback<RegistrationClientResp>() {
            @Override
            public void onResponse(Call<RegistrationClientResp> call, Response<RegistrationClientResp> response) {
                RegistrationClientResp clientResp=response.body();
                if(clientResp!=null){
                    if(clientResp.getMessage().equals("Register successfully")){
                        //client added
                        if(clientResp.getClientData()!=null){
                            ClientData clientData=clientResp.getClientData();
                            client_id = clientData.getClient_id();
                            //sendEmailFromClass(clientData);
                            new AlertDialog.Builder(Registration.this)
                                    .setTitle("Registered Successfully")
                                    .setMessage("Thank you for registering for the Boost Terminal.Please allow 48 hours for your account to be activated Client ID:"+client_id)
                                    .setCancelable(false)
                                    .setPositiveButton("Copy To ClipBoard", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("client id", client_id);
                                            clipboard.setPrimaryClip(clip);
                                            st.toast("Client ID Copied");
                                            dialogInterface.dismiss();
                                             thanksYou(client_id, clientData.getNext_layer_email(),clientData.getNext_layer_email_password());
                                        }
                                    }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    thanksYou(client_id,clientData.getNext_layer_email(),clientData.getNext_layer_email_password());
                                }
                            }).show();
                        }else{
                            ClientData clientData=new ClientData();
                            clientData.setClient_id("Test123");
                            clientData.setClient_name("Test User");
                            clientData.setEmail("test@test.com");
                        }
                    }else if(clientResp.getMessage().equals("Client id already exist")){
                        //clientID already added
                        goAlertDialogwithOneBTn(1,"","Client id is already exist","Retry","");
                    }else{
                        //client Not added
                        String cc="Error Occured";
                        if(clientResp.getMessage()!=null){
                            cc=clientResp.getMessage();
                        }
                        new androidx.appcompat.app.AlertDialog.Builder(Registration.this)
                                .setMessage(cc)
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                      //openActivity(MerchantLink.class);
                                    }
                                })
                                .show();
                    }
                }else {
                    goAlertDialogwithOneBTn(1,"","Server Side Error","Retry","");
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<RegistrationClientResp> call, Throwable t) {

                goAlertDialogwithOneBTn(1,"",t.getMessage(),"Retry","");
                progressDialog.dismiss();

            }
        });
    }
    private void thanksYou(String clientid, String email, String password) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialogregisterfinal,null);
        ImageView ivClientID = view.findViewById(R.id.ivClientID);
        ImageView ivEmail = view.findViewById(R.id.ivEmailAddress);
        ImageView ivSignInNextLayerUrl = view.findViewById(R.id.ivPassword);
        Bitmap bitmap=getBitMapFromHex(clientid);
        Bitmap emailAndPasswordBitmap = getBitMapFromHex(email + " " + password);
        Bitmap signInNextLayerUrlBitmap  = getBitMapFromHex("https://mail.nextlayer.me/SOGo/so/");
         if(bitmap!=null){
             ivClientID.setImageBitmap(bitmap);
         }else {
             ivClientID.setImageResource(R.drawable.a);
         }
        if(emailAndPasswordBitmap!=null){
            ivEmail.setImageBitmap(emailAndPasswordBitmap);
        }else {
            ivEmail.setImageResource(R.drawable.a);
        }
        if(signInNextLayerUrlBitmap!=null){
            ivSignInNextLayerUrl.setImageBitmap(signInNextLayerUrlBitmap);
        }else {
            ivSignInNextLayerUrl.setImageResource(R.drawable.a);
        }
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this).setCancelable(false).
                        setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                sp.clearAll();
                                openActivity(MerchantLink.class);
                            }
                        }).
                        setView(view);
        builder.create().show();
    }
    private void instant_registration(boolean isActive) {
        Bitmap clientImage=client_image_bitmap;
        Bitmap nicImage=card_image_bitmap;
        String clName=name;
        String clId=client_id;
        String nidd=nd;
        String add=address;
        String dbb=date;
        String eml=email;
        String regstrdt=getCurrentDateInFormamt();
        String isGam=isGamma ? "1":"0";
        String isAct=isActive ? "1":"0";
        String clientImageTitle=client_image_title;
        String nicImageTitle=card_image_title;
        String baseId=user_id;
        MultipartBody.Part client_image_id=null;
        MultipartBody.Part card_image_id=null;
        File clientImageFil= null;
        try {
            clientImageFil = savebitmap(clientImage,getUnixTimeStamp());
            client_image_id = MultipartBody.Part.createFormData("client_image_id", clientImageFil.getName(), RequestBody.create(MediaType.parse("image/*"), clientImageFil));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File cardImageFile= null;
        try {
            cardImageFile = savebitmap(nicImage,getUnixTimeStamp()+1);
            card_image_id= MultipartBody.Part.createFormData("card_image_id", cardImageFile.getName(), RequestBody.create(MediaType.parse("image/*"), cardImageFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody client_name2 = RequestBody.create(MediaType.parse("text/plain"), clName);
        RequestBody client_id2 = RequestBody.create(MediaType.parse("text/plain"), clId);
        RequestBody national_id2 = RequestBody.create(MediaType.parse("text/plain"), nidd );
        RequestBody address2 = RequestBody.create(MediaType.parse("text/plain"), add);
        RequestBody dob2 = RequestBody.create(MediaType.parse("text/plain"),dbb);
        RequestBody is_gamma_user2 = RequestBody.create(MediaType.parse("text/plain"),isGam);
        RequestBody registered_at2 = RequestBody.create(MediaType.parse("text/plain"),regstrdt);
        RequestBody is_active2 = RequestBody.create(MediaType.parse("text/plain"),isAct);
        RequestBody email2 = RequestBody.create(MediaType.parse("text/plain"), eml);
        RequestBody maxboost_limit2 = RequestBody.create(MediaType.parse("text/plain"),"25");
        RequestBody client_type = RequestBody.create(MediaType.parse("text/plain"),"instant");
//        RequestBody merchant_id = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMerchant_name());
//        RequestBody base_id = RequestBody.create(MediaType.parse("text/plain"),baseId);
//        RequestBody per_boost_limit = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMaxboost_limit());
//        RequestBody max_daily_boost = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMerchant_maxboost());

        //quoc testing
        maxboost_limit2 = null;
        RequestBody merchant_id = RequestBody.create(MediaType.parse("text/plain"),merchantData.getMerchant_name());
        RequestBody base_id = RequestBody.create(MediaType.parse("text/plain"),baseId);
        RequestBody per_boost_limit = RequestBody.create(MediaType.parse("text/plain"),"25");
        RequestBody max_daily_boost = RequestBody.create(MediaType.parse("text/plain"),"25");
        //quoc testing

        Call<RegistrationClientResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).client_Registration(client_type,client_name2,client_id2,merchant_id,national_id2,address2,dob2,is_gamma_user2,registered_at2,is_active2,client_image_id,card_image_id,email2,maxboost_limit2,base_id,per_boost_limit,max_daily_boost);
        call.enqueue(new Callback<RegistrationClientResp>() {
            @Override
            public void onResponse(Call<RegistrationClientResp> call, Response<RegistrationClientResp> response) {
                RegistrationClientResp clientResp=response.body();
                if(clientResp!=null){
                    if(clientResp.getMessage().equals("Register successfully")){
                        //client added
                        if(clientResp.getClientData()!=null){
                            ClientData clientData=clientResp.getClientData();
                            client_id = clientData.getClient_id();
                            addInTransactionLog(TOTAL_AMOUNT_USD,TOTAL_AMOUNT_BTC,TRANSACTION_TIMESTAMP,clientData.getClient_id());
                            //sendEmailFromClass(clientData);
                            new AlertDialog.Builder(Registration.this)
                                    .setTitle("Registered Successfully")
                                    .setMessage("Client ID:"+client_id)
                                    .setCancelable(false)
                                    .setPositiveButton("Copy To ClipBoard", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                            ClipData clip = ClipData.newPlainText("client id", client_id);
                                            clipboard.setPrimaryClip(clip);
                                            st.toast("Client ID Copied");
                                            dialogInterface.dismiss();
                                            thanksYou(client_id,clientData.getNext_layer_email(),clientData.getNext_layer_email_password());

                                        }
                                    }).setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                    thanksYou(client_id,clientData.getNext_layer_email(),clientData.getNext_layer_email_password());
                                }
                            }).show();
                        }else{
                            ClientData clientData=new ClientData();
                            clientData.setClient_id("Test123");
                            clientData.setClient_name("Test User");
                            clientData.setEmail("test@test.com");

                        }

                    }else if(clientResp.getMessage().equals("Client id is already exist")){
                        //clientID already  added
                        goAlertDialogwithOneBTn(1,"","Client id is already exist","Retry","");
                    }else{
                        //client Not  added
                        String cc="Error Occured";
                        if(clientResp.getMessage()!=null){
                            cc=clientResp.getMessage();
                        }
                        goAlertDialogwithOneBTn(1,"","Client id is already exist","Retry","");

                        new androidx.appcompat.app.AlertDialog.Builder(Registration.this)
                                .setMessage(cc)
                                .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        openActivity(MerchantLink.class);
                                    }
                                })
                                .show();
                    }

                }else {
                    //error
                    new androidx.appcompat.app.AlertDialog.Builder(Registration.this)
                            .setMessage("Unkown Error Occured")
                            .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    openActivity(MerchantLink.class);
                                }
                            })
                            .show();
                }
                Log.e("tes","pass");
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<RegistrationClientResp> call, Throwable t) {
                Log.e("tes","fail");
                // showToast("Network Error:"+t.getMessage().toString());
                new androidx.appcompat.app.AlertDialog.Builder(Registration.this)
                        .setMessage(t.getMessage().toString())
                        .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                openActivity(MerchantLink.class);
                            }
                        })
                        .show();
                progressDialog.dismiss();

            }
        });



    }
    private void getFundingNodeInfor() {
        Call<FundingNodeListResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).get_Funding_Node_List();

        call.enqueue(new Callback<FundingNodeListResp>() {
            @Override
            public void onResponse(Call<FundingNodeListResp> call, Response<FundingNodeListResp> response) {
                if(response.body()!=null){
                    fundingNodeTemp  = response.body().getFundingNodesList().get(0);
                    //TODO:Add new Field in db for registartion fees
                    // fundingNodeTemp.setRegistration_fees(10);
                    GlobalState.getInstance().setFundingNode(fundingNodeTemp);
                    FundingNode fundingNode=new FundingNode();
                    fundingNode=GlobalState.getInstance().getFundingNode();
                    if(fundingNode!=null){
                        if(fundingNode.getCompany_email()!=null){
                            String before=Utils.toEmail;
                            Utils.toEmail=fundingNode.getCompany_email();
                            String after=Utils.toEmail;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<FundingNodeListResp> call, Throwable t) {
                st.toast("RPC Failed Try Again");
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

                CurrentAllRate bitRate2=new CurrentAllRate();
                bitRate2=response.body();
                GlobalState.getInstance().setCurrentAllRate(bitRate2);
                CurrentAllRate tem=GlobalState.getInstance().getCurrentAllRate();
                bitCoinValue = bitRate2.getUSD().getLast();
            }

            @Override
            public void onFailure(Call<CurrentAllRate> call, Throwable t) {
                st.toast("bitcoin fail: " + t.getMessage());
            }
        });
    }
    private void controlDialogLayouts(int i, ArrayList<LinearLayout> layouts, ImageView instant_pay_back_icon) {
        dialogLayout = i;
        layouts.get(0).setVisibility(i == 1 ? View.VISIBLE : View.GONE);
        layouts.get(1).setVisibility((i == 2 || i == 3) ? View.VISIBLE : View.GONE);
        layouts.get(2).setVisibility(i == 3 ? View.VISIBLE : View.GONE);
        layouts.get(3).setVisibility(i == 4 ? View.VISIBLE : View.GONE);
        instant_pay_back_icon.setVisibility(i == 4 ? View.GONE : View.VISIBLE);
    }
    private void sendEmailFromClass(ClientData clientData) {
        FundingNode fundingNode=new FundingNode();
        fundingNode=GlobalState.getInstance().getFundingNode();
        if(fundingNode!=null){
         if(fundingNode.getCompany_email()!=null){
             Utils.toEmail=fundingNode.getCompany_email();
         }
        }
        String subject = "Registered Client";
        String x="Deactivate Account";
        if(clientData.getIs_active().equals("0")){x="Deactivate Account";}else {x="Activate Account";}
        String message = "Client ID:"+clientData.getClient_id()+"\nClient Name:"+clientData.getClient_name()+"\nClient Email:"+clientData.getEmail()+"\nClient Status:"+x;
        JavaMailAPI javaMailAPI = new JavaMailAPI(this, Utils.toEmail, subject, message);
        javaMailAPI.execute();
        String clientMail="khuwajhassan15@gmail.com";
        if(clientData!=null){
            if(clientData.getEmail()!=null){
                clientMail=clientData.getEmail();
                JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, clientMail, subject, message);
                javaMailAPI2.execute();
            }else{
                JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, clientMail, subject, message);
                javaMailAPI2.execute();
            }
        }else {
            JavaMailAPI javaMailAPI2 = new JavaMailAPI(this, clientMail, subject, message);
            javaMailAPI2.execute();
        }



    }
    public void ifServerConnectForCheckOut() {

        if (GlobalState.getInstance().getRpcInfo() != null) {

            String strEmail = GlobalState.getInstance().getRpcInfo().getCheck_out_user();
            String strPassword = GlobalState.getInstance().getRpcInfo().getCheck_out_password();
           // doInBackgroundMethod(Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER,Utils.CONNECT_TO_NETWORK_FOR_VALIDATE_USER_MESSAGE,new String[]{strEmail,strPassword});
        }

    }
    public void ifValidateCheckoutConnect() {
      //  doInBackgroundMethod( Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE,Utils.GET_TAX_INFO_FROM_SERVER_RESPONSE_MESSAGE,new String[]{"control,get-tax"});
    }
    private void doInBackgroundMethod(int responseType,String tag, String params[]){
        DoInBackground doInBackground = new DoInBackground(Registration.this,rpcResponse, responseType,tag);
        doInBackground.initExecute(params);
        //kdr ho yar
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        return dateFormat.format(date);

    }
    private  String getCurrentDateInFormamt(){
        Date c = Calendar.getInstance().getTime();
        Log.e("CurrentTime" ,c.toString());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(c);
        return date;
    }
    public void showToast(String message) {
        Toast.makeText(Registration.this, message, Toast.LENGTH_SHORT).show();
    }
    public File savebitmap(Bitmap bmp, String fileName) throws IOException {
        File f=null;
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            try {
                FileOutputStream fos = null;
                ContentValues valuesvideos;
                valuesvideos = new ContentValues();
                valuesvideos.put(MediaStore.MediaColumns.DISPLAY_NAME,"/myscreen_"+fileName+".jpg");
                valuesvideos.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg");
                valuesvideos.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES+File.separator+"DemoScreenShots");
                valuesvideos.put(MediaStore.Images.Media.TITLE, "DemoScreenShots");

                ContentResolver resolver = getContentResolver();
                Uri uriSavedVideo = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, valuesvideos);
                fos=(FileOutputStream) resolver.openOutputStream(Objects.requireNonNull(uriSavedVideo));
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                Objects.requireNonNull(fos);

                f=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/DemoScreenShots/_myscreen_"+fileName+".jpg");
            }catch (Exception e){
                System.out.println("Failed to create photo");
                e.printStackTrace();

            }

        }else {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
            f = new File(Environment.getExternalStorageDirectory() + File.separator + fileName + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            fo.close();

        }
        return f;
    }
    private void connectWithThorAndLoginAPi(String ip, String port, String username, String password) {
        showInstantDialog(ip,port,username,password);

    }
    private void connectWithThorAndLogin(String ip, String port, String username, String password) {
        ConnectLoginThor connectLoginThor=new ConnectLoginThor(Registration.this);
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            connectLoginThor.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[] { new String(ip),new String(port),new String(username),new String(password)});
        } else {
            connectLoginThor.execute(new String[] { new String(ip),new String(port),new String(username),new String(password)});
        }
    }
    private class ConnectLoginThor extends AsyncTask<String, Integer, String> {
        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;
        ProgressDialog dialog2;

        public ConnectLoginThor(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
            dialog2=new ProgressDialog(parent);
            dialog2.setMessage("Connecting...");
        }

        protected void onPreExecute () {
            dialog2.show();
            dialog2.setCancelable(false);
            dialog2.setCanceledOnTouchOutside(false);
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            String response="";
            int count = urls.length;
            String ip=urls[0];
            int port=Integer.valueOf(urls[1]);
            String user=urls[2];
            String pass=urls[3];
            Boolean status= Boolean.valueOf(NetworkManager.getInstance().connectClient(ip, port));
            if(status){
                int role= NetworkManager.getInstance().validateUser(user, pass);
                if(role==AppConstants.ADMIN){

                    response="Login As Admin";

                }else {
                    response="Invalid Receiving Node Login";
                }
            }else {
                response="Sovereign Doesn't Exist";
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
            if(result.equals("Sovereign Doesn't Exist")||result.equals("Invalid Receiving Node Login")){
                goAlertDialogwithOneBTn(1,"",result,"Retry","");
            }else {
                //createRegistrationInvocie();
            }
            try{
                if(dialog2!=null && dialog2.isShowing()){

                    dialog2.dismiss();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

            Log.e(TAG,"ConnectLoginThorResult:"+result);

        }

    }
    private void createRegistrationInvocie() {
        //showInstantDialog();
    }
    private void reConfirmRegistrationInvoice2(String query,String ip, String port, String username, String password){
        String merchantId=sp.getStringValue("merchant_id");
        String url=ip+":"+port;
//        reConfirmRegistrationInvoiceApi(merchantId,url,password,query);

        //quoc testing
        getARoutingAPIAuth1(merchantId,"abc123",url,query);
//        getARoutingAPIAuth2(merchantId,"123456");
        //quoc testing
    }

    private void reConfirmRegistrationInvoiceApiWithExecute(String merchantId, String url, String pass,String commad){
        ProgressDialog dialog2;
        dialog2=new ProgressDialog(this);
        dialog2.setMessage("Confirming...");
        dialog2.show();
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+url+"/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("cmd", commad);
            Call<DecodeBolt112WithExecuteResponse> call=apiInterface.getDecodeBolt112WithExecute("Bearer "+ authLevel1, requestBody);
            call.enqueue(new Callback<DecodeBolt112WithExecuteResponse>() {
                @Override
                public void onResponse(Call<DecodeBolt112WithExecuteResponse> call, Response<DecodeBolt112WithExecuteResponse> response) {
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
                        String firstName = response.body().decodeBolt112WithExecuteData.stdout;
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
                                    JSONObject object2=new JSONObject(firstName);
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<CreateInvoice>(){}.getType();
                                    CreateInvoice createInvoice = gson.fromJson(object2.toString(), type);
                                    GlobalState.getInstance().setCreateInvoice(createInvoice);

                                    if (createInvoice != null) {
                                        if (createInvoice.getBolt11() != null) {
                                            //transactionID++;
                                            String temHax = createInvoice.getBolt11();
                                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                                            try {
                                                BitMatrix bitMatrix = multiFormatWriter.encode(temHax, BarcodeFormat.QR_CODE, 600, 600);
                                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                                //progressDialog.dismiss();
                                                dialog2.dismiss();
                                                qr_scan_code_image.setImageBitmap(bitmap);
                                                controlDialogLayouts(3, layouts, instant_pay_back_icon);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                                dialog2.dismiss();
                                                st.toast("createInvoiceResponse: " + e.getMessage().toString());
                                            }
                                        }else {
                                            dialog2.dismiss();
                                        }

                                    }else {
                                        dialog2.dismiss();
                                    }

//                                }
//                                else {
//                                    goAlertDialogwithOneBTn(1,"","Please Check Your Funding/Receiving Node!","Retry","");
//                                    st.toast("Not contain bolt11");
//                                    dialog2.dismiss();
//                                }

                            } catch (Exception e) {
                                st.toast("createInvoiceResponse: " + e.getMessage());
                                dialog2.dismiss();
                            }

//                        }else {
//                            firstName= firstName.replaceAll("\\\\", "");
//                            JSONObject object2=new JSONObject(firstName);
//                            String message=object2.getString("message");
//                            st.toast(message);
//                            dialog2.dismiss();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialog2.dismiss();
                        st.toast("createInvoiceResponse: " + e.getMessage().toString());
                    }
                }
                @Override
                public void onFailure(Call<DecodeBolt112WithExecuteResponse> call, Throwable t) {
                    Log.e("TAG", "onResponse: "+t.getMessage().toString() );
                    dialog2.dismiss();
                    st.toast("createInvoiceResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            st.toast("createInvoiceResponse: " + e.getMessage());
            dialog2.dismiss();
        }
    }


    private void reConfirmRegistrationInvoiceApi(String merchantId, String url, String pass,String commad){
        ProgressDialog dialog2;
        dialog2=new ProgressDialog(this);
        dialog2.setMessage("Confirming...");
        dialog2.show();
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+url+"/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("merchantBackendPassword", "");
            requestBody.put("boost2faPassword", "");
            requestBody.put("command", commad);
            Call<Object> call=apiInterface.getNodes(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject object=new JSONObject(new Gson().toJson(response.body()));
                        Iterator<String> keys = object.keys();
                        JSONObject object1=null;
                        HashMap<String, Object> map = new HashMap<>();
                        if( keys.hasNext() ){
                            String key = (String)keys.next();
                            Object object22=object.get(key);
                            map.put(key, object.get(key));
                        }
                        String firstName = (String) map.get("output");
                        //{"code":900,"message":"Duplicatelabel\instant_register1640356814346\"}
                            firstName = firstName.replaceAll("\\s", "");
                            firstName= firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
                            firstName=firstName.substring(2);
                            firstName= firstName.replaceAll("b'", "");
                            firstName= firstName.replaceAll("',", "");
                            firstName= firstName.replaceAll("'", "");
                            firstName = firstName.substring(0,firstName.length() -1);
                        if (!firstName.contains("code")){

                            try {
                                if (firstName.contains("bolt11")) {
                                    JSONObject object2=new JSONObject(firstName);
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<CreateInvoice>(){}.getType();
                                    CreateInvoice createInvoice = gson.fromJson(object2.toString(), type);
                                    GlobalState.getInstance().setCreateInvoice(createInvoice);

                                    if (createInvoice != null) {
                                        if (createInvoice.getBolt11() != null) {
                                            //transactionID++;
                                            String temHax = createInvoice.getBolt11();
                                            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

                                            try {
                                                BitMatrix bitMatrix = multiFormatWriter.encode(temHax, BarcodeFormat.QR_CODE, 600, 600);
                                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                                                //progressDialog.dismiss();
                                                dialog2.dismiss();
                                                qr_scan_code_image.setImageBitmap(bitmap);
                                                controlDialogLayouts(3, layouts, instant_pay_back_icon);
                                            } catch (WriterException e) {
                                                e.printStackTrace();
                                                dialog2.dismiss();
                                                st.toast("createInvoiceResponse: " + e.getMessage().toString());
                                            }
                                        }else {
                                            dialog2.dismiss();
                                        }

                                    }else {
                                        dialog2.dismiss();
                                    }

                                }
                                else {
                                    goAlertDialogwithOneBTn(1,"","Please Check Your Funding/Receiving Node!","Retry","");
                                    st.toast("Not contain bolt11");
                                    dialog2.dismiss();
                                }

                            } catch (Exception e) {
                                st.toast("createInvoiceResponse: " + e.getMessage());
                                dialog2.dismiss();
                            }

                        }else {
                            firstName= firstName.replaceAll("\\\\", "");
                            JSONObject object2=new JSONObject(firstName);
                            String message=object2.getString("message");
                            st.toast(message);
                            dialog2.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dialog2.dismiss();
                        st.toast("createInvoiceResponse: " + e.getMessage().toString());
                    }
                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("TAG", "onResponse: "+t.getMessage().toString() );
                    dialog2.dismiss();
                    st.toast("createInvoiceResponse: " + t.getMessage().toString());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            st.toast("createInvoiceResponse: " + e.getMessage());
            dialog2.dismiss();
        }
    }
    private void createRegistartionInvoice1(String query) {
        CreateRegistartionInvice createRegistartionInvice=new CreateRegistartionInvice(Registration.this);
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            createRegistartionInvice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[] {new String(query)});
        } else {
            createRegistartionInvice.execute(new String[] {new String(query)});
        }
    }
    private class CreateRegistartionInvice extends AsyncTask<String, Integer, String> {
        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;
        ProgressDialog dialog2;

        public CreateRegistartionInvice(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
            dialog2=new ProgressDialog(parent);
            dialog2.setMessage("Creating...");
        }

        protected void onPreExecute () {
            dialog2.show();
            dialog2.setCancelable(false);
            dialog2.setCanceledOnTouchOutside(false);
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            String response="";
            String query = urls[0];
            try {
                NetworkManager.getInstance().sendToServer(query);
            } catch (Exception e) {
                Log.e(TAG,e.getLocalizedMessage());
            }
            try {
                // Try now
                response= NetworkManager.getInstance().recvFromServer();
            } catch (Exception e) {
                Log.e(TAG,e.getLocalizedMessage());
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
          createInvoiceResponse(result);

            try{
                if(dialog2!=null && dialog2.isShowing()){

                    dialog2.dismiss();
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        }

    }
    private void confirmRegistrationInvoice2(String query,String ip, String port, String username, String password){
        String merchantId=sp.getStringValue("merchant_id");
        String url=ip+":"+port;
//        ConfirmRegistrationInvoiceApi(merchantId,url,password,query);

        //quoc testing
        ConfirmRegistrationInvoiceApiWithExecute(merchantId,url,password,query);
        //quoc testing
    }

    private void getARoutingAPIAuth1(String merchantId, String merchantBackendPassword, String url, String clientNodeId2 ) {
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
                if(response.body()!=null){
                    Registration.this.authLevel1 = response.body().aRoutingAPIAuthData.accessToken;
                    reConfirmRegistrationInvoiceApiWithExecute(merchantId,url,merchantBackendPassword,clientNodeId2);
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ARoutingAPIAuthResponse> call, Throwable t) {
                progressDialog.dismiss();
                st.toast("Error: "+t.getMessage());
            }
        });
    }

    private void getARoutingAPIAuth2(String merchantId, String boost2FAPassword) {
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
                if(response.body()!=null){
                    Registration.this.authLevel2 = response.body().aRoutingAPIAuthData.accessToken;
                }
                progressDialog.dismiss();
            }
            @Override
            public void onFailure(Call<ARoutingAPIAuthResponse> call, Throwable t) {
                progressDialog.dismiss();

                st.toast("Error: "+t.getMessage());
            }
        });
    }


    private void ConfirmRegistrationInvoiceApiWithExecute(String merchantId, String url, String pass,String commad){
        ProgressDialog dialog2;
        dialog2=new ProgressDialog(this);
        dialog2.setMessage("Confirming...");
        dialog2.show();
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+url+"/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("cmd", commad);
            Call<DecodeBolt112WithExecuteResponse> call=apiInterface.getDecodeBolt112WithExecute("Bearer "+ authLevel1, requestBody);
            call.enqueue(new Callback<DecodeBolt112WithExecuteResponse>() {
                @Override
                public void onResponse(Call<DecodeBolt112WithExecuteResponse> call, Response<DecodeBolt112WithExecuteResponse> response) {
                    try {

                        String firstName = response.body().decodeBolt112WithExecuteData.stdout;

                        JSONObject object2=new JSONObject(firstName);
                        try {
                                JSONArray jsonArray=object2.getJSONArray("invoices");
                                JSONObject object3=new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
                                // {"invoices":[{"label":"instant_register1640095049835","bolt11":"lnbc10227040p1psurh26pp5r9tkees3sn4w8axx3ykkhssn7nkm6fyq5h50jd5zk0yjrvz7gauqdqgdahx7enxxqyjw5qcqpjsp5jz9zm9k73v7626f2rkghuprrasw8yga3tpwj59sjkgmngge2fsdsrzjq00n7z3066l22s56t9jxrnncfjfzk2vp4ks6lzw0alxeena3dst2wz5nsyqqgusqqyqqqqlgqqqqqqgq9q9qyyssqgfcysvla950u2yh8rga588c4ddn6ljx8y5v24q2mqpyjgza7d59jc42atfc59wxe66xk88wg05lc0ae9lwxkamemccp89c37z83a6mgp5fr8c0","payment_hash":"19576ce61184eae3f4c6892d6bc213f4edbd2480a5e8f93682b3c921b05e4778","msatoshi":1022704,"amount_msat":"1022704msat","status":"unpaid","description":"onoff","expires_at":1640699866}]}
                                Gson gson = new Gson();
                                Type type = new TypeToken<Invoice>(){}.getType();
                                Invoice invoice = gson.fromJson(object3.toString(), type);
                                GlobalState.getInstance().setInvoice(invoice);

                                if (invoice != null) {
                                    if (invoice.getStatus().equals("paid")) {

                                        TRANSACTION_TIMESTAMP=String.valueOf(invoice.getPaid_at());
                                        TRANSACTION_LABEL=invoice.getLabel();
                                        TOTAL_AMOUNT_BTC= invoice.getMsatoshi();
                                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.btcToSathosi;
                                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.satoshiToMSathosi;
                                        TOTAL_AMOUNT_USD=getUsdFromBtc(TOTAL_AMOUNT_BTC);
                                        USD_TO_BTC_RATE=TOTAL_AMOUNT_USD/TOTAL_AMOUNT_BTC;
                                        st.toast("paid");
                                        instant_registration(true);
                                        dialog2.dismiss();

                                    } else {
                                        dialog2.dismiss();
                                        goAlertDialogwithOneBTn(1,"","Payment Not Recieved","Retry","");
                                    }
                                }else {
                                    dialog2.dismiss();
                                }

                        } catch (Exception e) {
                            st.toast("confirmInvoiceRespone: " + e.getMessage());
                            dialog2.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        st.toast("confirmInvoiceRespone: " + e.getMessage());
                        dialog2.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<DecodeBolt112WithExecuteResponse> call, Throwable t) {
                    Log.e("TAG", "onResponse: "+t.getMessage().toString() );
                    st.toast("confirmInvoiceRespone: " + t.getMessage().toString());
                    dialog2.dismiss();
                }
            });
        } catch (Exception e) {
            dialog2.dismiss();
            e.printStackTrace();
        }
    }



    private void ConfirmRegistrationInvoiceApi(String merchantId, String url, String pass,String commad){
        ProgressDialog dialog2;
        dialog2=new ProgressDialog(this);
        dialog2.setMessage("Confirming...");
        dialog2.show();
        dialog2.setCancelable(false);
        dialog2.setCanceledOnTouchOutside(false);

        Gson gson = new GsonBuilder().setLenient().create();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://"+url+"/")
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterfaceForNodes apiInterface = retrofit.create(ApiInterfaceForNodes.class);
        try {
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("merchantId", merchantId);
            requestBody.put("merchantBackendPassword", "");
            requestBody.put("boost2faPassword", "");
            requestBody.put("command", commad);
            Call<Object> call=apiInterface.getNodes(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        JSONObject object=new JSONObject(new Gson().toJson(response.body()));
                        Iterator<String> keys = object.keys();
                        JSONObject object1=null;
                        HashMap<String, Object> map = new HashMap<>();
                        if( keys.hasNext() ){
                            String key = (String)keys.next();
                            Object object22=object.get(key);
                            map.put(key, object.get(key));
                        }
                        String firstName = (String) map.get("output");
                        firstName = firstName.replaceAll("\\s", "");
                        firstName= firstName.toString().replace(",<$#EOT#$>", "").replace("\n", "");
                        firstName=firstName.substring(2);
                        firstName= firstName.replaceAll("b'", "");
                        firstName= firstName.replaceAll("',", "");
                        firstName= firstName.replaceAll("'", "");
                        firstName = firstName.substring(0,firstName.length() -1);

                        JSONObject object2=new JSONObject(firstName);
                        try {
                            if (firstName.contains("bolt11")) {
                                JSONArray jsonArray=object2.getJSONArray("invoices");
                                JSONObject object3=new JSONObject(String.valueOf(jsonArray.getJSONObject(0)));
                               // {"invoices":[{"label":"instant_register1640095049835","bolt11":"lnbc10227040p1psurh26pp5r9tkees3sn4w8axx3ykkhssn7nkm6fyq5h50jd5zk0yjrvz7gauqdqgdahx7enxxqyjw5qcqpjsp5jz9zm9k73v7626f2rkghuprrasw8yga3tpwj59sjkgmngge2fsdsrzjq00n7z3066l22s56t9jxrnncfjfzk2vp4ks6lzw0alxeena3dst2wz5nsyqqgusqqyqqqqlgqqqqqqgq9q9qyyssqgfcysvla950u2yh8rga588c4ddn6ljx8y5v24q2mqpyjgza7d59jc42atfc59wxe66xk88wg05lc0ae9lwxkamemccp89c37z83a6mgp5fr8c0","payment_hash":"19576ce61184eae3f4c6892d6bc213f4edbd2480a5e8f93682b3c921b05e4778","msatoshi":1022704,"amount_msat":"1022704msat","status":"unpaid","description":"onoff","expires_at":1640699866}]}
                                Gson gson = new Gson();
                                Type type = new TypeToken<Invoice>(){}.getType();
                                Invoice invoice = gson.fromJson(object3.toString(), type);
                                GlobalState.getInstance().setInvoice(invoice);

                                if (invoice != null) {
                                    if (invoice.getStatus().equals("paid")) {

                                        TRANSACTION_TIMESTAMP=String.valueOf(invoice.getPaid_at());
                                        TRANSACTION_LABEL=invoice.getLabel();
                                        TOTAL_AMOUNT_BTC= invoice.getMsatoshi();
                                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.btcToSathosi;
                                        TOTAL_AMOUNT_BTC=TOTAL_AMOUNT_BTC/AppConstants.satoshiToMSathosi;
                                        TOTAL_AMOUNT_USD=getUsdFromBtc(TOTAL_AMOUNT_BTC);
                                        USD_TO_BTC_RATE=TOTAL_AMOUNT_USD/TOTAL_AMOUNT_BTC;
                                        st.toast("paid");
                                        instant_registration(true);
                                        dialog2.dismiss();

                                    } else {
                                        dialog2.dismiss();
                                        goAlertDialogwithOneBTn(1,"","Payment Not Recieved","Retry","");
                                    }
                                }else {
                                    dialog2.dismiss();
                                }

                            } else {
                                dialog2.dismiss();
                                goAlertDialogwithOneBTn(1,"","Payment Not Recieved","Retry","");
                            }
                        } catch (Exception e) {
                            st.toast("confirmInvoiceRespone: " + e.getMessage());
                            dialog2.dismiss();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        st.toast("confirmInvoiceRespone: " + e.getMessage());
                        dialog2.dismiss();
                    }
                }
                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    Log.e("TAG", "onResponse: "+t.getMessage().toString() );
                    st.toast("confirmInvoiceRespone: " + t.getMessage().toString());
                    dialog2.dismiss();
                }
            });
        } catch (Exception e) {
            dialog2.dismiss();
            e.printStackTrace();
        }
    }
    private void confirmRegistrationInvoice(String query) {
        ConfirmRegistartionInvice confirmRegistartionInvice=new ConfirmRegistartionInvice(Registration.this);
        if(Build.VERSION.SDK_INT >= 11/*HONEYCOMB*/) {
            confirmRegistartionInvice.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,new String[] {new String(query)});
        } else {
            confirmRegistartionInvice.execute(new String[] {new String(query)});
        }
    }
    private class ConfirmRegistartionInvice extends AsyncTask<String, Integer, String> {
        // Constant for identifying the dialog
        private static final int LOADING_DIALOG = 100;
        private Activity parent;
        ProgressDialog dialog2;

        public ConfirmRegistartionInvice(Activity parent) {
            // record the calling activity, to use in showing/hiding dialogs
            this.parent = parent;
            dialog2=new ProgressDialog(parent);
            dialog2.setMessage("Confirming...");
        }

        protected void onPreExecute () {
            dialog2.show();
            dialog2.setCancelable(false);
            dialog2.setCanceledOnTouchOutside(false);
            // called on UI thread
            // parent.showDialog(LOADING_DIALOG);
        }

        protected String doInBackground(String... urls) {
            // called on the background thread
            String response="";
            String query = urls[0];
            try {
                NetworkManager.getInstance().sendToServer(query);
            } catch (Exception e) {
                Log.e(TAG,e.getLocalizedMessage());
            }
            try {
                // Try now
                response= NetworkManager.getInstance().recvFromServer();
            } catch (Exception e) {
                Log.e(TAG,e.getLocalizedMessage());
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
            confirmInvoiceResponse(result);
            try{
                if(dialog2!=null && dialog2.isShowing()){

                    dialog2.dismiss();
                }
            }catch(Exception e){
                e.printStackTrace();
            }


        }

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    public String excatFigure(double value) {
        BigDecimal d = new BigDecimal(String.valueOf(value));
        return  d.toPlainString();
    }
    private void addInTransactionLog(double amount_usd, double amount_btc,String  transactionTimeStamp,String client_id2) {
        String amountUsd=String.valueOf(amount_usd);
        String amountBtc=String.valueOf(amount_btc);
        String label=TRANSACTION_LABEL;
       //String clientId=client_id2;  //clientData.getClient_id()
        String clientId=client_id;
        String merchantID=merchantData.getMerchant_name();
        String transactionID=label;

        Call<TransactionResp> call = ApiClient.getRetrofit(this).create(ApiInterface.class).instance_transction_add(label,transactionID,amountBtc,amountUsd,clientId,merchantID,transactionTimeStamp,String.valueOf(USD_TO_BTC_RATE));
        call.enqueue(new Callback<TransactionResp>() {
            @Override
            public void onResponse(Call<TransactionResp> call, Response<TransactionResp> response) {
                if(response!=null){
                    if(response.body()!=null){
                        TransactionResp transactionResp=response.body();
                        if (transactionResp.getMessage().equals("successfully done")&&transactionResp.getTransactionInfo()!=null) {
                            TransactionInfo transactionInfo=new TransactionInfo();
                            transactionInfo=transactionResp.getTransactionInfo();

                        }else {
                            showToast("Not Done!!");
                        }
                        Log.e("Test","Test");
                    }else {
                        showToast(response.message());
                        Log.e("AddTransactionLog",response.message());
                    }
                }
                Log.e("AddTransactionLog",response.message());
            }
            @Override
            public void onFailure(Call<TransactionResp> call, Throwable t) {
                Log.e("AddTransactionLog",t.getMessage().toString());
            }
        });
    }
    private void goAlertDialogwithOneBTn(int i, String alertTitleMessage,String alertMessage,String alertBtn1Message,String alertBtn2Message) {
        final Dialog goAlertDialogwithOneBTnDialog;
        goAlertDialogwithOneBTnDialog=new Dialog(Registration.this);
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
    @Override
    public void onUserInteraction() {
        // TODO Auto-generated method stub
        super.onUserInteraction();
        stopHandler();//stop first and then start
        startHandler();
    }
    public void stopHandler() {
        handler.removeCallbacks(r);
    }
    public void startHandler() {
        handler.postDelayed(r, 5*60*1000); //for 5 minutes
    }

    private class ScreenReceiver extends BroadcastReceiver {

        protected ScreenReceiver() {
            // register receiver that handles screen on and screen off logic
            IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(this, filter);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                sp.clearAll();
                openActivity(MerchantLink.class);
                finishAffinity();

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                //isScreenOff = false;
            }
        }
    }

}