package com.sis.clighteningboost.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sis.clighteningboost.Activities.MerchantBoostTerminal;
import com.sis.clighteningboost.Interface.OnCancelListener;
import com.sis.clighteningboost.Models.REST.MerchantNearbyClientsData;
import com.sis.clighteningboost.R;
import com.sis.clighteningboost.Utills.Acknowledgement;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class BoostNodeAdapter extends RecyclerView.Adapter<BoostNodeAdapter.ViewHolder> {
    Activity mActivity;
    ArrayList<MerchantNearbyClientsData> mMerchantNearbyClientsDataArrayList;
    String mClient_id = "",mReceivingNodeId="";
    Socket mSocket;
    boolean mOnMsgReceived = false;
    Dialog mAlertDialog;
    OnCancelListener cancelListener;

    public BoostNodeAdapter(Activity activity, ArrayList<MerchantNearbyClientsData> mMerchantNearbyClientsDataArrayList, Socket socket, OnCancelListener cancelListener) {
        this.mMerchantNearbyClientsDataArrayList = mMerchantNearbyClientsDataArrayList;
        this.mActivity = activity;
        this.mSocket = socket;
        this.cancelListener = cancelListener;
        createAlertInstance();


    }
    @NonNull
    @Override
    public BoostNodeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_boost_node, parent, false);
        return new ViewHolder(listItem);    }

    @Override
    public void onBindViewHolder(@NonNull BoostNodeAdapter.ViewHolder holder, int position) {
        try
        {
            if(mMerchantNearbyClientsDataArrayList.get(position).getClient_image_url()!=null && !mMerchantNearbyClientsDataArrayList.get(position).getClient_image_url().isEmpty())
                Glide.with(mActivity).load(mMerchantNearbyClientsDataArrayList.get(position).getClient_image_url()).into(holder.mImageView);

        }catch (NullPointerException e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(view -> {

            if(mMerchantNearbyClientsDataArrayList.get(position).getClient_id()!=null && !mMerchantNearbyClientsDataArrayList.get(position).getClient_id().isEmpty()) {
                mClient_id = mMerchantNearbyClientsDataArrayList.get(position).getClient_id();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.accumulate("to",mClient_id);
                jsonObject.accumulate("type","fp_receiving_node_id_req");
                jsonObject.accumulate("payload",1);

            } catch (JSONException e) {
                e.printStackTrace();
            }

           mSocket.emit("msg", jsonObject,new Acknowledgement(){
               @Override
               public void call(Object... args) {
                   super.call(args);
                   mActivity.runOnUiThread(new Runnable() {
                       @Override
                       public void run() {
                           if(args.length!=0){
                               Log.d("Socket","Acknowledgement of zero element");
                               if(onStartReceive()){
                                   mActivity.startActivity(new Intent(mActivity,MerchantBoostTerminal.class).putExtra("node_id",mReceivingNodeId));
                               }
                               else {
                                   showAlert();
                               }
                           }else {
                               Toast.makeText(mActivity, "Message is not sent", Toast.LENGTH_SHORT).show();
                           }
                       }
                   });


              }
           });


        });


    }

    private void showAlert() {
        mAlertDialog.show();

    }
    private void createAlertInstance() {
        mAlertDialog =new Dialog(mActivity);
        mAlertDialog.setContentView(R.layout.alert_dialog_layout);
        Objects.requireNonNull(mAlertDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final TextView alertTitle_tv= mAlertDialog.findViewById(R.id.alertTitle);
        final TextView alertMessage_tv= mAlertDialog.findViewById(R.id.alertMessage);
        final Button yesbtn= mAlertDialog.findViewById(R.id.yesbtn);
        final Button nobtn= mAlertDialog.findViewById(R.id.nobtn);
        nobtn.setVisibility(View.GONE);
        yesbtn.setText("Cancel Request");
        alertTitle_tv.setText("Flashpay Request Initiated");
        alertMessage_tv.setText("Please wait for Client info request auto-response");


        yesbtn.setOnClickListener(v -> {
            mAlertDialog.dismiss();
            cancelListener.onCancel();
        });

    }
    @Override
    public int getItemCount() {
        return mMerchantNearbyClientsDataArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CircleImageView mImageView;
        AppCompatImageView mIvBackground;
        public ViewHolder(View itemView){
            super(itemView);
            mImageView = itemView.findViewById(R.id.ivImageView);
            mIvBackground = itemView.findViewById(R.id.ivBackground);


        }
    }

    public  boolean onStartReceive(){
        mOnMsgReceived = false;
        mSocket.on("msg", args -> mActivity.runOnUiThread(() -> {
            JSONObject data = (JSONObject) args[0];
            Log.d("Socket",data.toString());
            try {
                mReceivingNodeId = data.getString("payload");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(mReceivingNodeId!=null && !mReceivingNodeId.isEmpty()){
                mOnMsgReceived = true;
            }
        }));
        return mOnMsgReceived;

    }




}
